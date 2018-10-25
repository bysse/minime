package com.tazadum.glsl;

import com.tazadum.glsl.compression.Compressor;
import com.tazadum.glsl.input.GLSLSource;
import com.tazadum.glsl.input.Shader;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.output.OutputRenderer;
import com.tazadum.glsl.output.IdentifierOutput;
import com.tazadum.glsl.output.OutputConfig;
import com.tazadum.glsl.output.generator.FileGenerator;
import com.tazadum.glsl.output.generator.HeaderFileGenerator;
import com.tazadum.glsl.output.generator.PackedHeaderFileGenerator;
import com.tazadum.glsl.output.generator.PassThroughGenerator;
import com.tazadum.glsl.parser.GLSLLexer;
import com.tazadum.glsl.parser.GLSLParser;
import com.tazadum.glsl.parser.optimizer.*;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.*;
import java.util.stream.Collectors;

import static com.tazadum.glsl.Option.*;

/**
 * Created by Erik on 2016-10-24.
 */
public class GLSLOptimizer {
    private final OutputWriter outputWriter;
    private final OutputProfile outputProfile;
    private final OutputConfig outputConfig;
    private final OutputRenderer output;
    private final HashMap<Option, Boolean> optionsMap;

    private ContextBasedMultiIdentifierShortener identifierShortener;

    public GLSLOptimizer(OutputWriter outputWriter, OutputProfile outputProfile) {
        this.outputWriter = outputWriter;
        this.outputProfile = outputProfile;

        this.outputConfig = new OutputConfig();
        this.outputConfig.setIdentifiers(IdentifierOutput.Replaced);
        this.outputConfig.setIndentation(0);
        this.outputConfig.setOutputConst(false);

        this.output = new OutputRenderer();

        this.identifierShortener = new ContextBasedMultiIdentifierShortener(false);

        this.optionsMap = new HashMap<>();
    }

    public void setOptions(Map<Option, Boolean> options) {
        optionsMap.putAll(options);
    }

    public void processFiles(List<String> shaderFiles) {
        List<GLSLSource> sources = shaderFiles.stream()
                .map(Shader::load)
                .map(Shader::getSource)
                .collect(Collectors.toList());
        process(sources);
    }

    public void process(List<GLSLSource> shaderSources) {
        List<GLSLOptimizerContext> contexts = new ArrayList<>();
        for (GLSLSource source : shaderSources) {
            output("--------------------------------------------------\n");

            if (!source.hasContent()) {
                source = Shader.load(source.getFilename()).getSource();
            }

            contexts.add(execute(source));
        }

        if (option(NoIdentifierRenaming)) {
            outputConfig.setIdentifiers(IdentifierOutput.Original);
        } else {
            if (!option(PassThrough)) {
                output("--------------------------------------------------\n");

                output("Shortening identifiers\n");
                identifierShortener.apply();
            }
        }

        for (GLSLOptimizerContext context : contexts) {
            output("--------------------------------------------------\n");
            writeShader(context, contexts.size() > 1);
        }
        output("--------------------------------------------------\n");
    }

    private GLSLOptimizerContext execute(GLSLSource glslSource) {
        String shaderFilename = glslSource.getFilename();
        String shaderSource = glslSource.getContent();

        final GLSLOptimizerContext context = new GLSLOptimizerContext(shaderFilename);


        outputConfig.setNewlines(option(OutputLineBreaks));
        outputConfig.setImplicitConversionToFloat(true);

        if (shaderSource.startsWith("#version")) {
            int firstLine = shaderSource.indexOf("\n");
            context.setHeader(shaderSource.substring(0, firstLine));
            shaderSource = shaderSource.substring(firstLine + 1);
        }
        final CommonTokenStream tokenStream = tokenStream(shaderSource);
        final GLSLParser parser = new GLSLParser(tokenStream);

        context.setSource(shaderSource);

        output("Filename:     %s\n", shaderFilename);
        final int sourceSize = shaderSource.length();
        output("Input size:   %d bytes\n", sourceSize);


        // TODO: iron out this class
        // updateIdentifiers the parser
        //final ContextVisitor visitor = new ContextVisitor(context.parserContext(), glslSource.getFileMapper());
        //final Node shaderNode = parser.translation_unit().accept(visitor);
        Node shaderNode = null;

        // perform type checking
        //context.typeChecker().check(context.parserContext(), shaderNode);

        if (option(PassThrough)){
            context.setNode(shaderNode);
        } else {
            // optimize the shader
            final Node node = optimize(context, shaderNode);
            context.setNode(node);

            // register the ast with the identifier shortener
            shortenIdentifiers(context);
        }

        return context;
    }

    private void writeShader(GLSLOptimizerContext context, boolean multipleShaders) {
        final Node node = context.getNode();

        String outputShader = output.render(node, outputConfig).trim();
        int compressedLength = Compressor.compress(outputShader);

        boolean passThrough = option(PassThrough);
        boolean bitPack = option(BitPackSource);

        if (!option(NoIdentifierRenaming)) {
            // iterate on the symbol allocation
            while (true) {
                final boolean loop = !passThrough && identifierShortener.permutateIdentifiers();
                final String iteration = output.render(node, outputConfig).trim();
                final int length = Compressor.compress(outputShader);

                if (length > 0 && length < compressedLength) {
                    output("Re-allocating identifiers\n");
                    output("  - %d bytes compressed\n", length);
                    outputShader = iteration;
                    compressedLength = length;
                }

                if (!loop) {
                    break;
                }
            }
        }

        /*
        if (!preferences.contains(Preference.NO_MACRO)) {
            output("Macro replacements\n");
            final String macroShader = identifierShortener.updateTokens(outputShader);
            output("  - %d bytes\n", macroShader.length());

            final int macroLength = Compressor.compress(macroShader);
            if (macroLength > 0) {
                output("  - %d bytes compressed\n", macroLength);
            }
            if (macroLength < compressedLength) {
                outputShader = macroShader;
                compressedLength = macroLength;
                output("Using macro replacements for output");
            }
        }
        */

        // output a summary
        final int sourceSize = context.getSource().length();
        final int outputSize = outputShader.length();
        output("Filename:   %s\n", context.getShaderName());
        output("Input Size: %d bytes\n", sourceSize);
        output("Output:     %d bytes (%.1f%%)\n", outputSize, 100f * outputSize / sourceSize);
        output("Removed:    %d bytes (%.1f%%)\n", sourceSize - outputSize, 100f * (sourceSize - outputSize) / sourceSize);
        output("Compressed: %d bytes (%.1f%%)\n", compressedLength, 100f * compressedLength / sourceSize);

        // transform the output
        FileGenerator generator = createGenerator(outputProfile, context.getShaderName(), multipleShaders, bitPack);
        String content = generator.generate(context, outputShader);

        // output the result
        try {
            OutputStream outputStream = outputWriter.outputStream();
            OutputStreamWriter writer = new OutputStreamWriter(outputStream);
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private FileGenerator createGenerator(OutputProfile outputProfile, String shaderFilename, boolean multipleShaders, boolean bitPack) {
        switch (outputProfile) {
            case GLSL:
                return new PassThroughGenerator();
            case C:
                if (bitPack) {
                    return new PackedHeaderFileGenerator(shaderFilename, outputConfig, multipleShaders);
                }
                return new HeaderFileGenerator(shaderFilename, outputConfig, multipleShaders, option(NoPragmaOnce));
        }
        throw new IllegalArgumentException("Unknown OutputProfile!");
    }

    private Node optimize(GLSLOptimizerContext optimizerContext, Node shaderNode) {
        EnumSet<OptimizerType> optimizerTypes = EnumSet.allOf(OptimizerType.class);

        if (option(NoArithmeticSimplifications)) {
            optimizerTypes.remove(OptimizerType.ArithmeticOptimizerType);
        }

        //final OptimizerPipeline pipeline = new SingleShaderOptimizerPipeline(outputConfig, optimizerTypes);

        TreePruner pruner = TreePruner.byIterationDepth(2);
        final OptimizerPipeline pipeline = new BranchingOptimizerPipeline(pruner, outputConfig, optimizerTypes);
        return pipeline.optimize(optimizerContext, shaderNode, !option(SilentOutput));
    }

    private void shortenIdentifiers(GLSLOptimizerContext context) {
        final OutputConfig config = new OutputConfig();
        config.setIdentifiers(IdentifierOutput.None);
        config.setNewlines(false);
        config.setIndentation(0);
        config.setOutputConst(false);

        identifierShortener.register(context.parserContext(), context.getNode(), config);
    }

    private CommonTokenStream tokenStream(String source) {
        ANTLRInputStream inputStream = new ANTLRInputStream(source);
        GLSLLexer lexer = new GLSLLexer(inputStream);
        return new CommonTokenStream(lexer);
    }

    private void output(String format, Object... args) {
        if (!option(SilentOutput)) {
            System.out.format(format, args);
        }
    }

    private boolean option(Option option) {
        return optionsMap.getOrDefault(option, option.getDefault());
    }
}


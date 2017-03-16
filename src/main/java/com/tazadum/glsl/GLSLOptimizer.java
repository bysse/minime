package com.tazadum.glsl;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.compresion.Compressor;
import com.tazadum.glsl.language.BuiltInType;
import com.tazadum.glsl.language.GLSLLexer;
import com.tazadum.glsl.language.GLSLParser;
import com.tazadum.glsl.language.TypeQualifier;
import com.tazadum.glsl.output.IdentifierOutput;
import com.tazadum.glsl.output.Output;
import com.tazadum.glsl.output.OutputConfig;
import com.tazadum.glsl.output.OutputSizeDecider;
import com.tazadum.glsl.output.generator.FileGenerator;
import com.tazadum.glsl.output.generator.HeaderFileGenerator;
import com.tazadum.glsl.output.generator.PassThroughGenerator;
import com.tazadum.glsl.parser.GLSLContext;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.ParserContextImpl;
import com.tazadum.glsl.parser.function.FunctionRegistryImpl;
import com.tazadum.glsl.parser.optimizer.*;
import com.tazadum.glsl.parser.optimizer.Optimizer;
import com.tazadum.glsl.parser.type.FullySpecifiedType;
import com.tazadum.glsl.parser.type.TypeChecker;
import com.tazadum.glsl.parser.type.TypeRegistryImpl;
import com.tazadum.glsl.parser.variable.VariableRegistry;
import com.tazadum.glsl.parser.variable.VariableRegistryImpl;
import com.tazadum.glsl.parser.visitor.ContextVisitor;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Erik on 2016-10-24.
 */
public class GLSLOptimizer {
    private final OutputWriter outputWriter;
    private final OutputProfile outputProfile;
    private final OutputConfig outputConfig;
    private final Output output;


    private boolean showStatistics = false;
    private boolean shaderToySupport = false;
    private Set<Preference> preferences = new HashSet<>();

    public GLSLOptimizer(OutputWriter outputWriter, OutputProfile outputProfile) {
        this.outputWriter = outputWriter;
        this.outputProfile = outputProfile;

        this.outputConfig = new OutputConfig();
        this.outputConfig.setIdentifiers(IdentifierOutput.Replaced);
        this.outputConfig.setIndentation(0);
        this.outputConfig.setOutputConst(false);

        this.output = new Output();
    }

    public void addShaderToySupport() {
        shaderToySupport = true;
    }

    void addShaderToySupport(GLSLOptimizerContext optimizerContext) {
        final ParserContext parserContext = optimizerContext.getParserContext();
        final VariableRegistry variableRegistry = parserContext.getVariableRegistry();
        final GLSLContext global = parserContext.globalContext();

        variableRegistry.declare(global, uniform(BuiltInType.VEC3, "iResolution"));
        variableRegistry.declare(global, uniform(BuiltInType.FLOAT, "iGlobalTime"));
        variableRegistry.declare(global, uniform(BuiltInType.FLOAT, "iTimeDelta"));
        variableRegistry.declare(global, uniform(BuiltInType.INT, "iFrame"));
        // uniform float     iChannelTime[4];       // channel playback time (in seconds)
        //uniform vec3      iChannelResolution[4]; // channel resolution (in pixels)
        variableRegistry.declare(global, uniform(BuiltInType.VEC4, "iMouse"));
        //uniform samplerXX iChannel0..3;          // input channel. XX = 2D/Cube
        variableRegistry.declare(global, uniform(BuiltInType.VEC4, "iDate"));
        variableRegistry.declare(global, uniform(BuiltInType.FLOAT, "iSampleRate"));
    }

    public void showStatistics() {
        this.showStatistics = true;
    }

    public void setPreferences(Set<Preference> preferences) {
        this.preferences = preferences;
    }

    public void execute(List<String> shaderFiles) {
        for (String shaderFile : shaderFiles) {
            execute(shaderFile);
        }
    }

    private VariableDeclarationNode uniform(BuiltInType type, String identifier) {
        final FullySpecifiedType fst = new FullySpecifiedType(TypeQualifier.UNIFORM, null, type);
        return new VariableDeclarationNode(true, fst, identifier, null, null);
    }

    private void execute(String shaderFilename) {
        final GLSLOptimizerContext optimizerContext = createOptimizerContext(shaderFilename);

        if (shaderToySupport) {
            addShaderToySupport(optimizerContext);
        }

        outputConfig.setNewlines(preferences.contains(Preference.LINE_BREAKS));

        final String shaderSource = loadFile(new File(shaderFilename));
        final CommonTokenStream tokenStream = tokenStream(shaderSource);
        final GLSLParser parser = new GLSLParser(tokenStream);

        output("--------------------------------------------------\n");
        final int sourceSize = shaderSource.length();
        output("Input shader: %d bytes\n", sourceSize);

        // updateIdentifiers the parser
        final ContextVisitor visitor = new ContextVisitor(optimizerContext.getParserContext());
        final Node shaderNode = parser.translation_unit().accept(visitor);

        // perform type checking
        optimizerContext.getTypeChecker().check(optimizerContext.getParserContext(), shaderNode);

        // updateIdentifiers the optimizers
        final Node node = optimize(optimizerContext, shaderNode);

        output("Shortening identifiers\n");
        shortenIdentifiers(optimizerContext, node);

        String outputShader = output.render(node, outputConfig).trim();
        output("  - %d bytes\n", outputShader.length());

        int compressedLength = Compressor.compress(outputShader);
        if (compressedLength > 0) {
            output("  - %d bytes compressed\n", compressedLength);
        }

        // iterate on the symbol allocation
        IdentifierShortener identifierShortener = optimizerContext.getIdentifierShortener();
        while (true) {
            final boolean loop = identifierShortener.iterateOnIdentifiers();
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

        // output a summary
        final int outputSize = outputShader.length();
        output("--------------------------------------------------\n");
        output("Input: %d bytes\n", sourceSize);
        output("Output: %d bytes (%.1f%%)\n", outputSize, 100f * outputSize / sourceSize);
        output("Removed: %d bytes (%.1f%%)\n", sourceSize - outputSize, 100f * (sourceSize - outputSize) / sourceSize);
        output("Compressed: %d bytes (%.1f%%)\n", compressedLength, 100f * compressedLength / sourceSize);
        output("--------------------------------------------------\n");

        // transform the output
        FileGenerator generator = createGenerator(outputProfile, shaderFilename);
        String content = generator.generate(optimizerContext, shaderNode, outputShader);

        // output the result
        try (OutputStream outputStream = outputWriter.outputStream()) {
            OutputStreamWriter writer = new OutputStreamWriter(outputStream);
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private FileGenerator createGenerator(OutputProfile outputProfile, String shaderFilename) {
        switch (outputProfile) {
            case GLSL:
                return new PassThroughGenerator();
            case C:
                return new HeaderFileGenerator(shaderFilename);
        }
        throw new IllegalArgumentException("Unknown OutputProfile!");
    }

    private Node optimize(GLSLOptimizerContext optimizerContext, Node shaderNode) {
        final OutputSizeDecider decider = new OutputSizeDecider();

        // instantiate all the optimizers
        final DeadCodeElimination deadCodeElimination = new DeadCodeElimination();
        final ConstantFolding constantFolding = new ConstantFolding();
        final ConstantPropagation constantPropagation = new ConstantPropagation();
        final DeclarationSqueeze declarationSqueeze = new DeclarationSqueeze();

        final ParserContext parserContext = optimizerContext.getParserContext();

        Node node = shaderNode;

        int changes, iteration = 0;
        do {
            final int size = output.render(node, outputConfig).length();
            output("Iteration #%d: %d bytes\n", iteration++, size);

            // apply dead code elimination
            final Optimizer.OptimizerResult deadCodeResult = deadCodeElimination.run(parserContext, decider, node);
            changes = deadCodeResult.getChanges();
            node = deadCodeResult.getNode();
            if (changes > 0) {
                output("  - %d dead code eliminations\n", changes);
            }

            // apply constant folding
            final Optimizer.OptimizerResult foldResult = constantFolding.run(parserContext, decider, node);
            changes = foldResult.getChanges();
            node = foldResult.getNode();
            if (changes > 0) {
                output("  - %d constant folding replacements\n", changes);
            }

            // apply constant propagation
            final Optimizer.OptimizerResult propagationResult = constantPropagation.run(parserContext, decider, node);
            changes = propagationResult.getChanges();
            node = propagationResult.getNode();
            if (changes > 0) {
                output("  - %d constant propagation\n", changes);
            }

            // apply declaration squeeze
            final Optimizer.OptimizerResult squeezeResult = declarationSqueeze.run(parserContext, decider, node);
            changes = squeezeResult.getChanges();
            node = squeezeResult.getNode();
            if (changes > 0) {
                output("  - %d declaration squeezes\n", changes);
            }
        } while (changes > 0);
        return node;
    }

    private void shortenIdentifiers(GLSLOptimizerContext optimizerContext, Node node) {
        final OutputConfig config = new OutputConfig();
        config.setIdentifiers(IdentifierOutput.None);
        config.setNewlines(false);
        config.setIndentation(0);
        config.setOutputConst(false);

        optimizerContext.getIdentifierShortener().updateIdentifiers(optimizerContext.getParserContext(), node, config);
    }

    private CommonTokenStream tokenStream(String source) {
        ANTLRInputStream inputStream = new ANTLRInputStream(source);
        GLSLLexer lexer = new GLSLLexer(inputStream);
        return new CommonTokenStream(lexer);
    }

    private String loadFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            StringBuilder content = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                content.append(line).append('\n');
            }
            return content.toString();
        } catch (FileNotFoundException e) {
            System.err.format("Can't open file : %s\n", file.toString());
            System.exit(-1);
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void output(String format, Object... args) {
        if (showStatistics) {
            System.out.format(format, args);
        }
    }

    private GLSLOptimizerContext createOptimizerContext(String filename) {
        TypeChecker typeChecker = new TypeChecker();
        ParserContext parserContext = new ParserContextImpl(new TypeRegistryImpl(), new VariableRegistryImpl(), new FunctionRegistryImpl());
        IdentifierShortener identifierShortener = new ContextBasedIdentifierShortener(false);

        GLSLOptimizerContext optimizerContext = new GLSLOptimizerContext(typeChecker, parserContext, identifierShortener);
        return optimizerContext;
    }
}


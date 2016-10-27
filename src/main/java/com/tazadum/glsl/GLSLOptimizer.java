package com.tazadum.glsl;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.language.BuiltInType;
import com.tazadum.glsl.language.GLSLLexer;
import com.tazadum.glsl.language.GLSLParser;
import com.tazadum.glsl.language.TypeQualifier;
import com.tazadum.glsl.output.Output;
import com.tazadum.glsl.output.OutputConfig;
import com.tazadum.glsl.output.OutputSizeDecider;
import com.tazadum.glsl.parser.GLSLContext;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.ParserContextImpl;
import com.tazadum.glsl.parser.function.FunctionRegistryImpl;
import com.tazadum.glsl.parser.optimizer.ConstantFolding;
import com.tazadum.glsl.parser.optimizer.DeclarationSqueeze;
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

/**
 * Created by Erik on 2016-10-24.
 */
public class GLSLOptimizer {
    private final OutputStreamProvider outputStreamProvider;
    private final OutputConfig outputConfig;
    private final Output output;
    private final TypeChecker typeChecker;
    private final ParserContext parserContext;

    public GLSLOptimizer(OutputStreamProvider outputStreamProvider, OutputProfile profile) {
        this.outputStreamProvider = outputStreamProvider;

        this.outputConfig = new OutputConfig();
        this.output = new Output();
        this.typeChecker = new TypeChecker();
        this.parserContext = new ParserContextImpl(new TypeRegistryImpl(), new VariableRegistryImpl(), new FunctionRegistryImpl());

        switch (profile) {
            case GLSL:
                outputConfig.setIndentation(0);
                outputConfig.setNewlines(true);
                break;
        }
    }

    public void addShaderToySupport() {
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

    private VariableDeclarationNode uniform(BuiltInType type, String identifier) {
        final FullySpecifiedType fst = new FullySpecifiedType(TypeQualifier.UNIFORM, null, type);
        return new VariableDeclarationNode(fst, identifier, null, null);
    }

    public void execute(String shaderFilename) {
        final String shaderSource = loadFile(new File(shaderFilename));
        final CommonTokenStream tokenStream = tokenStream(shaderSource);
        final GLSLParser parser = new GLSLParser(tokenStream);

        System.out.format("Source: %d bytes\n", shaderSource.length());

        // run the parser
        final ContextVisitor visitor = new ContextVisitor(parserContext);
        final Node shaderNode = parser.translation_unit().accept(visitor);

        // perform type checking
        typeChecker.check(parserContext, shaderNode);

        // run the optimizer
        final Node node = optimize(shaderNode);
        final String outputShader = output.render(node, outputConfig).trim();

        System.out.format("Result: %d bytes\n", outputShader.length());

        // output the result
        try (OutputStream outputStream = outputStreamProvider.get()) {
            OutputStreamWriter writer = new OutputStreamWriter(outputStream);
            writer.write(outputShader);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Node optimize(Node shaderNode) {
        final OutputSizeDecider decider = new OutputSizeDecider();

        // instantiate all the optimizers
        final ConstantFolding constantFolding = new ConstantFolding();
        final DeclarationSqueeze declarationSqueeze = new DeclarationSqueeze();

        Node node = shaderNode;

        int changes, iteration = 0;
        do {
            final int size = output.render(node, outputConfig).length();
            System.out.format("# Iteration #%d: %d bytes\n", iteration++, size);

            // apply constant folding
            System.out.println("# Running constant folding");
            final Optimizer.OptimizerResult foldResult = constantFolding.run(parserContext, decider, node);
            changes = foldResult.getChanges();
            node = foldResult.getNode();
            if (changes > 0) {
                System.out.println("  - changes: " + changes);
                continue;
            }

            // apply declaration squeeze
            System.out.println("# Running declaration squeeze");
            final Optimizer.OptimizerResult squeezeResult = declarationSqueeze.run(parserContext, decider, node);
            changes = foldResult.getChanges();
            node = foldResult.getNode();
            if (changes > 0) {
                System.out.println("  - Changes: " + changes);
                continue;
            }

        } while (changes > 0);

        return node;
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


package com.tazadum.glsl.cli.builder;

import com.tazadum.glsl.exception.SourcePositionException;
import com.tazadum.glsl.language.ast.ASTConverter;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.function.BuiltInFunctionRegistry;
import com.tazadum.glsl.language.function.BuiltInFunctionRegistryImpl;
import com.tazadum.glsl.language.function.FunctionRegistry;
import com.tazadum.glsl.language.function.FunctionRegistryImpl;
import com.tazadum.glsl.language.type.TypeRegistry;
import com.tazadum.glsl.language.type.TypeRegistryImpl;
import com.tazadum.glsl.language.variable.VariableRegistry;
import com.tazadum.glsl.language.variable.VariableRegistryImpl;
import com.tazadum.glsl.parser.*;
import com.tazadum.glsl.parser.functions.FunctionSets;
import com.tazadum.glsl.preprocessor.Preprocessor;
import com.tazadum.glsl.preprocessor.language.GLSLProfile;
import com.tazadum.glsl.stage.StageData;
import com.tazadum.glsl.stage.StageException;
import com.tazadum.glsl.util.Pair;
import com.tazadum.glsl.util.SourcePosition;
import com.tazadum.glsl.util.SourcePositionId;
import com.tazadum.glsl.util.SourcePositionMapper;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class CompilerExecutor implements ProcessorExecutor<CompilerExecutor.Result> {
    private final Logger logger = LoggerFactory.getLogger(CompilerExecutor.class);

    private final TypeRegistry typeRegistry;
    private final VariableRegistry variableRegistry;
    private final FunctionRegistryImpl functionRegistry;
    private final ParserContextImpl parserContext;
    private Function<GLSLParser, ParserRuleContext> parserRuleProvider = GLSLParser::translation_unit;

    private String source;
    private SourcePositionMapper mapper;

    public static CompilerExecutor create() {
        return new CompilerExecutor(ShaderType.FRAGMENT, GLSLProfile.COMPATIBILITY);
    }

    public static CompilerExecutor create(ShaderType shaderType, GLSLProfile profile) {
        return new CompilerExecutor(shaderType, profile);
    }

    private CompilerExecutor(ShaderType shaderType, GLSLProfile profile) {
        final BuiltInFunctionRegistry builtInRegistry = FunctionSets.applyFunctions(
                new BuiltInFunctionRegistryImpl(),
                shaderType,
                profile
        );

        typeRegistry = new TypeRegistryImpl();
        variableRegistry = new VariableRegistryImpl();
        functionRegistry = new FunctionRegistryImpl(builtInRegistry);
        parserContext = new ParserContextImpl(typeRegistry, variableRegistry, functionRegistry);

        parserContext.initializeVariables(shaderType, profile);
    }

    /**
     * Sets the function that extracts the parser rule that is going to be applied.
     * This is different if complete shaders are parsed as opposed to expressions.
     */
    public CompilerExecutor parserRuleProvider(Function<GLSLParser, ParserRuleContext> parserRuleProvider) {
        this.parserRuleProvider = parserRuleProvider;
        return this;
    }

    public CompilerExecutor source(Path path) {
        validateSource();
        try {
            source = Files.readString(path);
            mapper = new SourcePositionMapper();

            final String filename = path.toFile().getName();
            mapper.remap(SourcePosition.TOP, SourcePositionId.create(filename, SourcePosition.TOP));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return this;
    }

    public CompilerExecutor source(String source) {
        validateSource();
        this.source = source;
        this.mapper = new SourcePositionMapper();
        this.mapper.remap(SourcePosition.TOP, SourcePositionId.DEFAULT);
        return this;
    }

    public CompilerExecutor source(Preprocessor.Result result) {
        this.source = result.getSource();
        this.mapper = result.getMapper();
        return this;
    }

    public CompilerExecutor withTypeRegistry(BiConsumer<ParserContext, TypeRegistry> consumer) {
        consumer.accept(parserContext, typeRegistry);
        return this;
    }

    public CompilerExecutor withVariableRegistry(BiConsumer<ParserContext, VariableRegistry> consumer) {
        consumer.accept(parserContext, variableRegistry);
        return this;
    }

    public CompilerExecutor withFunctionRegistry(BiConsumer<ParserContext, FunctionRegistry> consumer) {
        consumer.accept(parserContext, functionRegistry);
        return this;
    }

    private void validateSource() {
        if (source != null) {
            throw new IllegalStateException("Compiler source already set!");
        }
    }

    @Override
    public Result process() {
        try {
            final GLSLLexer lexer = new GLSLLexer(CharStreams.fromString(source));
            final CommonTokenStream tokenStream = new CommonTokenStream(lexer);
            final GLSLParser parser = new GLSLParser(tokenStream);
            parser.setErrorHandler(new ParserBailStrategy());

            logger.trace("- Converting source to AST");
            final ASTConverter astConverter = new ASTConverter(mapper, parserContext);
            Node node = parserRuleProvider.apply(parser).accept(astConverter);

            logger.trace("- Checking types");
            node.accept(parserContext.getTypeVisitor());

            return new Result(mapper, node, parserContext);
        } catch (SourcePositionException e) {
            final SourcePositionId sourcePositionId = mapper.map(e.getSourcePosition());
            final String message = sourcePositionId.format() + ": " + e.getMessage();
            throw new StageException(message, e);
        }
    }

    public static class Result implements StageData<Pair<Node, ParserContext>> {
        private final SourcePositionMapper mapper;
        private Node node;
        private ParserContext context;

        Result(SourcePositionMapper mapper, Node node, ParserContext context) {
            this.mapper = mapper;
            this.node = node;
            this.context = context;
        }

        public Node getNode() {
            return node;
        }

        public ParserContext getContext() {
            return context;
        }

        @Override
        public Pair<Node, ParserContext> getData() {
            return Pair.create(node, context);
        }

        @Override
        public SourcePositionMapper getMapper() {
            return mapper;
        }
    }
}

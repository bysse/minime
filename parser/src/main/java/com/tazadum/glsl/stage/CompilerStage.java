package com.tazadum.glsl.stage;

import com.tazadum.glsl.exception.SourcePositionException;
import com.tazadum.glsl.language.ast.ASTConverter;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.function.BuiltInFunctionRegistry;
import com.tazadum.glsl.language.function.BuiltInFunctionRegistryImpl;
import com.tazadum.glsl.language.function.FunctionRegistryImpl;
import com.tazadum.glsl.language.type.TypeRegistryImpl;
import com.tazadum.glsl.language.variable.VariableRegistryImpl;
import com.tazadum.glsl.parser.*;
import com.tazadum.glsl.parser.functions.FunctionSets;
import com.tazadum.glsl.preprocessor.language.GLSLProfile;
import com.tazadum.glsl.util.Pair;
import com.tazadum.glsl.util.SourcePositionId;
import com.tazadum.glsl.util.SourcePositionMapper;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by erikb on 2018-10-24.
 */
public class CompilerStage implements Stage<String, Pair<Node, ParserContext>> {
    private final Logger logger = LoggerFactory.getLogger(CompilerStage.class);
    private final ShaderType shaderType;
    private final GLSLProfile glslProfile;

    public CompilerStage(ShaderType shaderType, GLSLProfile glslProfile) {
        this.shaderType = shaderType;
        this.glslProfile = glslProfile;

        logger.debug("Using shader type {}", shaderType);
        logger.debug("Using GL Profile {}", glslProfile);
    }

    @Override
    public StageData<Pair<Node, ParserContext>> process(StageData<String> input) {
        final BuiltInFunctionRegistry builtInRegistry = new BuiltInFunctionRegistryImpl();

        FunctionSets.applyFunctions(builtInRegistry, shaderType, glslProfile);

        final ParserContext parserContext = new ParserContextImpl(
            new TypeRegistryImpl(),
            new VariableRegistryImpl(),
            new FunctionRegistryImpl(builtInRegistry)
        );

        parserContext.initializeVariables(shaderType, glslProfile);

        final SourcePositionMapper sourcePositionMapper = input.getMapper();

        try {
            final GLSLLexer lexer = new GLSLLexer(CharStreams.fromString(input.getData()));
            final CommonTokenStream tokenStream = new CommonTokenStream(lexer);
            final GLSLParser parser = new GLSLParser(tokenStream);
            parser.setErrorHandler(new ParserBailStrategy());

            logger.trace("Converting source to AST");
            final ASTConverter astConverter = new ASTConverter(sourcePositionMapper, parserContext);
            Node node = parser.translation_unit().accept(astConverter);

            logger.trace("Type checking");
            node.accept(parserContext.getTypeVisitor());

            return StageData.from(Pair.create(node, parserContext), sourcePositionMapper);
        } catch (SourcePositionException e) {
            final SourcePositionId sourcePositionId = sourcePositionMapper.map(e.getSourcePosition());
            final String message = sourcePositionId.format() + ": " + e.getMessage();
            throw new StageException(message, e);
        }
    }
}

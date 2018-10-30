package com.tazadum.glsl;

import com.tazadum.glsl.exception.SourcePositionException;
import com.tazadum.glsl.language.ast.ASTConverter;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.function.BuiltInFunctionRegistry;
import com.tazadum.glsl.language.function.BuiltInFunctionRegistryImpl;
import com.tazadum.glsl.language.function.FunctionRegistryImpl;
import com.tazadum.glsl.language.output.OutputConfig;
import com.tazadum.glsl.language.output.OutputConfigBuilder;
import com.tazadum.glsl.language.output.OutputVisitor;
import com.tazadum.glsl.language.type.TypeRegistryImpl;
import com.tazadum.glsl.language.variable.VariableRegistry;
import com.tazadum.glsl.language.variable.VariableRegistryImpl;
import com.tazadum.glsl.parser.*;
import com.tazadum.glsl.parser.functions.FunctionSets;
import com.tazadum.glsl.preprocessor.language.GLSLProfile;
import com.tazadum.glsl.util.SourcePosition;
import com.tazadum.glsl.util.SourcePositionId;
import com.tazadum.glsl.util.SourcePositionMapper;
import org.antlr.v4.runtime.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Created by erikb on 2018-09-16.
 */
public class TestUtil {
    public static CommonTokenStream tokenStream(String source) {
        GLSLLexer lexer = new GLSLLexer(CharStreams.fromString(source));
        return new CommonTokenStream(lexer);
    }

    public static List<Token> getTokens(CommonTokenStream stream) {
        stream.fill();
        return new ArrayList<>(stream.getTokens());
    }

    public static GLSLParser parser(CommonTokenStream tokenStream) {
        return new GLSLParser(tokenStream);
    }

    public static void printTokens(String source) {
        System.out.println("# Showing tokens for '" + source + "'");
        for (Token token : TestUtil.getTokens(TestUtil.tokenStream(source))) {
            System.out.println(token);
        }
    }

    public static ParserRuleContext parse(String source) {
        return parse(source, GLSLParser::translation_unit);
    }

    public static ParserRuleContext parse(String source, Function<GLSLParser, ParserRuleContext> extractor) {
        if (!source.endsWith("\n")) {
            source += "\n";
        }

        try {
            final CommonTokenStream stream = TestUtil.tokenStream(source);
            final GLSLParser parser = TestUtil.parser(stream);
            parser.setErrorHandler(new BailErrorStrategy());
            return extractor.apply(parser);
        } catch (Exception e) {
            printTokens(source);
            throw e;
        }
    }

    public static Node ast(ParserRuleContext context, ParserContext parserContext) {
        if (context == null) {
            return null;
        }
        try {
            SourcePositionMapper mapper = new SourcePositionMapper();
            mapper.remap(SourcePosition.TOP, SourcePositionId.DEFAULT);
            return context.accept(new ASTConverter(mapper, parserContext));
        } catch (SourcePositionException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void typeCheck(Node node, ParserContext parserContext) {
        assert node != null : "Node is null";
        node.accept(parserContext.getTypeVisitor());
    }

    public static String toString(Node node) {
        OutputConfig outputConfig = new OutputConfigBuilder().blacklistKeyword("const").build();
        return node.accept(new OutputVisitor(outputConfig)).get();
    }

    private static BuiltInFunctionRegistry builtInRegistry = null;

    public static BuiltInFunctionRegistry getBuiltInFunctionRegistry(ShaderType shaderType, GLSLProfile profile) {
        if (builtInRegistry == null) {
            System.err.println("WARNING: BuiltInFunctionRegistry is a singleton instance!");
            builtInRegistry = FunctionSets.applyFunctions(new BuiltInFunctionRegistryImpl(), shaderType, profile);
        }
        return builtInRegistry;
    }

    public static ParserContext parserContext() {
        return parserContext(ShaderType.FRAGMENT, GLSLProfile.COMPATIBILITY, true);
    }

    public static ParserContext parserContext(ShaderType shaderType, GLSLProfile profile) {
        return parserContext(shaderType, profile, true);
    }

    public static ParserContext parserContext(ShaderType shaderType, GLSLProfile profile, boolean builtIn) {
        BuiltInFunctionRegistry builtInRegistry;
        if (builtIn) {
            builtInRegistry = getBuiltInFunctionRegistry(shaderType, profile);
        } else {
            builtInRegistry = new BuiltInFunctionRegistryImpl();
        }

        VariableRegistry variableRegistry = new VariableRegistryImpl();
        FunctionRegistryImpl functionRegistry = new FunctionRegistryImpl(builtInRegistry);
        ParserContextImpl parserContext = new ParserContextImpl(new TypeRegistryImpl(), variableRegistry, functionRegistry);
        if (builtIn) {
            parserContext.initializeVariables(shaderType, profile);
        }
        return parserContext;
    }
}


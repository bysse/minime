package com.tazadum.glsl;

import com.tazadum.glsl.language.ast.ContextVisitor;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.function.FunctionRegistryImpl;
import com.tazadum.glsl.language.type.TypeRegistryImpl;
import com.tazadum.glsl.language.variable.VariableRegistryImpl;
import com.tazadum.glsl.parser.GLSLLexer;
import com.tazadum.glsl.parser.GLSLParser;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.ParserContextImpl;
import com.tazadum.glsl.util.SourcePosition;
import com.tazadum.glsl.util.SourcePositionId;
import com.tazadum.glsl.util.SourcePositionMapper;
import org.antlr.v4.runtime.*;

import java.util.ArrayList;
import java.util.List;

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
        if (!source.endsWith("\n")) {
            source += "\n";
        }

        try {
            final CommonTokenStream stream = TestUtil.tokenStream(source);
            final GLSLParser parser = TestUtil.parser(stream);
            parser.setErrorHandler(new BailErrorStrategy());
            return parser.translation_unit();
        } catch (Exception e) {
            printTokens(source);
            throw e;
        }
    }

    public static Node ast(ParserRuleContext context) {
        if (context == null) {
            return null;
        }
        try {
            ParserContext parserContext = parserContext();
            SourcePositionMapper mapper = new SourcePositionMapper();
            mapper.remap(SourcePosition.TOP, SourcePositionId.DEFAULT);

            ContextVisitor contextVisitor = new ContextVisitor(parserContext, mapper);
            return context.accept(contextVisitor);
        } catch (Exception e) {
            throw e;
        }
    }

    public static ParserContext parserContext() {
        return new ParserContextImpl(new TypeRegistryImpl(), new VariableRegistryImpl(), new FunctionRegistryImpl());
    }
}


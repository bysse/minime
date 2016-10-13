package com.tazadum.glsl.parser;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.language.GLSLLexer;
import com.tazadum.glsl.language.GLSLParser;
import com.tazadum.glsl.parser.function.FunctionRegistryImpl;
import com.tazadum.glsl.parser.type.TypeRegistryImpl;
import com.tazadum.glsl.parser.variable.VariableRegistryImpl;
import com.tazadum.glsl.parser.visitor.ContextVisitor;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

/**
 * @author erikb
 * @since 2016-07-31
 */
public class TestUtils {
    public static CommonTokenStream tokenStream(String source) {
        ANTLRInputStream inputStream = new ANTLRInputStream(source);
        GLSLLexer lexer = new GLSLLexer(inputStream);
        return new CommonTokenStream(lexer);
    }

    public static List<Token> getTokens(CommonTokenStream stream) {
        stream.fill();
        return stream.getTokens().stream().collect(Collectors.toCollection(ArrayList::new));
    }

    public static GLSLParser parser(CommonTokenStream tokenStream) {
        return new GLSLParser(tokenStream);
    }

    public static ParserContext parserContext() {
        return new ParserContextImpl(new TypeRegistryImpl(), new VariableRegistryImpl(), new FunctionRegistryImpl());
    }

    public static <T extends Node> T parse(ParserContext parserContext, Class<T> type, String source) {
        try {
            final CommonTokenStream stream = TestUtils.tokenStream(source);
            final GLSLParser parser = TestUtils.parser(stream);
            final ContextVisitor visitor = new ContextVisitor(parserContext);
            Node node = parser.translation_unit().accept(visitor);
            assertTrue("Expected node type " + type.getSimpleName(), type.isAssignableFrom(node.getClass()));
            return (T) node;
        } catch (Exception e) {
            for (Token token : TestUtils.getTokens(TestUtils.tokenStream(source))) {
                System.out.println(token);
            }
            throw e;
        }
    }
}

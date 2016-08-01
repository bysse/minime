package com.tazadum.glsl.parser;

import com.tazadum.glsl.language.GLSLLexer;
import com.tazadum.glsl.language.GLSLParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
}

package com.tazadum.glsl.preprocessor.parser;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by erikb on 2018-09-16.
 */
public class TestUtil {
    public static CommonTokenStream tokenStream(String source) {
        PPLexer lexer = new PPLexer(CharStreams.fromString(source));
        return new CommonTokenStream(lexer);
    }

    public static List<Token> getTokens(CommonTokenStream stream) {
        stream.fill();
        return new ArrayList<>(stream.getTokens());
    }

    public static PPParser parser(CommonTokenStream tokenStream) {
        return new PPParser(tokenStream);
    }

    public static void parse(String source) {
        if (!source.endsWith("\n")) {
            source += "\n";
        }

        try {
            final CommonTokenStream stream = TestUtil.tokenStream(source);
            final PPParser parser = TestUtil.parser(stream);
            parser.setErrorHandler(new BailErrorStrategy());
            parser.statement();
        } catch (Exception e) {
            System.out.println("# Showing tokens for '" + source + "'");
            for (Token token : TestUtil.getTokens(TestUtil.tokenStream(source))) {
                System.out.println(token);
            }
            throw e;
        }
    }
}


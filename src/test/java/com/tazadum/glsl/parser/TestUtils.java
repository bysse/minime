package com.tazadum.glsl.parser;

import com.tazadum.glsl.language.GLSLLexer;
import com.tazadum.glsl.language.GLSLListener;
import com.tazadum.glsl.language.GLSLParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.ArrayList;
import java.util.List;

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
        List<Token> tokens = new ArrayList<>();
        stream.fill();
        for (Token token : stream.getTokens()) {
            tokens.add(token);
        }
        return tokens;
    }

    public static GLSLParser parser(CommonTokenStream tokenStream) {
       return new GLSLParser(tokenStream);
    }

    public static <T extends GLSLListener> T walk(ParserRuleContext parserRuleContext, T listener) {
        ParseTreeWalker.DEFAULT.walk(listener, parserRuleContext);
        return listener;
    }
}

package com.tazadum.glsl.parser;

import com.tazadum.glsl.ast.StatementListNode;
import com.tazadum.glsl.language.GLSLLexer;
import com.tazadum.glsl.language.GLSLParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/**
 * @author erikb
 * @since 2016-07-31
 */
public class ParserImpl implements Parser {
    @Override
    public StatementListNode parse(String source) {
        ANTLRInputStream inputStream = new ANTLRInputStream(source);
        GLSLLexer lexer = new GLSLLexer(inputStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        GLSLParser parser = new GLSLParser(tokenStream);

        GLSLParser.Translation_unitContext translationUnit = parser.translation_unit();

        ParserListener listener = createListener();
        ParseTreeWalker.DEFAULT.walk(listener, translationUnit);
        return listener.getStatements();
    }

    private ParserListener createListener() {
        return null;
    }
}

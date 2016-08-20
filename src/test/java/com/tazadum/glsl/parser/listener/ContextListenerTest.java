package com.tazadum.glsl.parser.listener;

import com.tazadum.glsl.ast.Context;
import com.tazadum.glsl.language.GLSLParser;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.TestUtils;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Before;
import org.junit.Test;

/**
 * @author erikb
 * @since 2016-07-31
 */
public class ContextListenerTest {
    private ParserContext parserContext;

    @Before
    public void setUp() {
        this.parserContext = TestUtils.parserContext();
    }


    @Test
    public void showTokens() {
        CommonTokenStream stream = TestUtils.tokenStream("float variable;");
        System.out.println(TestUtils.getTokens(stream));
    }

    @Test
    public void showRuleInvocations() {
        CommonTokenStream stream = TestUtils.tokenStream("float variable;");
        GLSLParser parser = TestUtils.parser(stream);
        ParseTreeWalker.DEFAULT.walk(new PrintingListener(null, true, true), parser.translation_unit());
    }

    @Test
    public void testVariableDeclaration() {
        CommonTokenStream stream = TestUtils.tokenStream("float variable;");
        GLSLParser parser = TestUtils.parser(stream);

        ContextListener listener = new ContextListener(parserContext);
        listener.walk(parser.translation_unit());
        Context context = listener.getResult();
    }
}

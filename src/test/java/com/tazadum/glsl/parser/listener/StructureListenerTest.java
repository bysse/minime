package com.tazadum.glsl.parser.listener;

import com.tazadum.glsl.ast.Shader;
import com.tazadum.glsl.language.GLSLParser;
import com.tazadum.glsl.parser.TestUtils;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Test;

/**
 * @author erikb
 * @since 2016-07-31
 */
public class StructureListenerTest {
    @Test
    public void showTokens() {
        CommonTokenStream stream = TestUtils.tokenStream("float variable;");
        System.out.println(TestUtils.getTokens(stream));
    }

    @Test
    public void showRuleInvocations() {
        CommonTokenStream stream = TestUtils.tokenStream("float variable;");
        GLSLParser parser = TestUtils.parser(stream);
        TestUtils.walk(parser.translation_unit(), new PrintingListener(null, true, true));
    }

    @Test
    public void testVariableDeclaration() {
        CommonTokenStream stream = TestUtils.tokenStream("float variable;");
        GLSLParser parser = TestUtils.parser(stream);

        StructureListener listener = new StructureListener();
        TestUtils.walk(parser.translation_unit(), new PrintingListener(listener, true, true));
        Shader shader = listener.getShader();
    }
}
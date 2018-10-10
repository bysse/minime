package com.tazadum.glsl.language.ast.util;

import com.tazadum.glsl.language.ast.Node;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by erikb on 2018-10-10.
 */
class CloneUtilsTest {
    @Test
    void testEquals() {
        Node a = compile("int f() { float a=1.0; return 2*a; }");
        Node b = compile("int f() { float a=1.0; return 2*a; }");

        Node aClone = CloneUtils.clone(a, null);

        assertTrue(CloneUtils.equal(a, b));
        assertTrue(CloneUtils.equal(a, aClone));
        assertTrue(CloneUtils.equal(b, aClone));
    }

    @Test
    void testEqualsOtherIdentifier() {
        Node a = compile("int f() { float a=1.0; return 2*a; }");
        Node b = compile("int f() { float b=1.0; return 2*b; }");

        assertTrue(CloneUtils.equal(a, b, false));
        assertFalse(CloneUtils.equal(a, b, true));
    }

    protected Node compile(String source) {
        throw new UnsupportedOperationException("not implemented");
        /*
        GLSLOptimizerContext optimizerContext = TestUtil.optimizerContext("test.glsl");
        ParserContext parserContext = optimizerContext.parserContext();
        return TestUtil.parse(parserContext, Node.class, source);
        */
    }
}

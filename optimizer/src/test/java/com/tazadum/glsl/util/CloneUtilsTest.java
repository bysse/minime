package com.tazadum.glsl.util;

import com.tazadum.glsl.GLSLOptimizerContext;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.TestUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CloneUtilsTest {
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
        GLSLOptimizerContext optimizerContext = TestUtils.optimizerContext("test.glsl");
        ParserContext parserContext = optimizerContext.parserContext();
        return TestUtils.parse(parserContext, Node.class, source);
    }
}
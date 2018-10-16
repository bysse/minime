package com.tazadum.glsl.language.ast.util;

import com.tazadum.glsl.TestUtil;
import com.tazadum.glsl.language.ast.Node;
import org.antlr.v4.runtime.ParserRuleContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by erikb on 2018-10-10.
 */
class CloneUtilsTest {

    // Not working because we don't implement clone in UnresolvedNodes

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
        ParserRuleContext context = TestUtil.parse(source);
        return TestUtil.ast(context);
    }
}

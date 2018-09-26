package com.tazadum.glsl.util;

import org.junit.jupiter.api.Test;

import static com.tazadum.glsl.util.StringUtils.cut;
import static com.tazadum.glsl.util.StringUtils.isEmpty;
import static com.tazadum.glsl.util.StringUtils.rtrim;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StringUtilsTest {
    @Test
    public void testEmpty() {
        assertTrue(isEmpty(null));
        assertTrue(isEmpty(""));
        assertFalse(isEmpty("a"));
    }

    @Test
    public void testRtrim() {
        assertEquals(null, rtrim(null));
        assertEquals("", rtrim(""));
        assertEquals("text", rtrim("text"));
        assertEquals("text", rtrim("text "));
        assertEquals("text", rtrim("text \t "));
    }

    @Test
    public void testCut() {
        assertEquals("", cut(null, 1));
        assertEquals("", cut(null, 0));

        assertEquals("12345", cut("12345", 0));
        assertEquals("345", cut("12345", 2));
        assertEquals("123", cut("12345", -2));

        assertEquals("", cut("12345", -8));
        assertEquals("", cut("12345", 8));
    }
}
package com.tazadum.glsl.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class LineTokenizerTest {
    @Test
    @DisplayName("Empty string")
    public void test_1() {
        LineTokenizer tokenizer = new LineTokenizer("");

        assertEquals("", tokenizer.getLine());
        assertNull(tokenizer.getLine());
    }

    @Test
    @DisplayName("Peek")
    public void test_2() {
        LineTokenizer tokenizer = new LineTokenizer("1\r");

        assertEquals("1", tokenizer.getLine());
        assertNull(tokenizer.getLine());
    }

    @Test
    @DisplayName("Multi line")
    public void test_3() {
        LineTokenizer tokenizer = new LineTokenizer("1\r\n\n3\n");

        assertEquals("1", tokenizer.getLine());
        assertEquals("", tokenizer.getLine());
        assertEquals("3", tokenizer.getLine());
        assertNull(tokenizer.getLine());
    }

    @Test
    @DisplayName("Multi line with trailing")
    public void test_4() {
        LineTokenizer tokenizer = new LineTokenizer("1\n2");

        assertEquals("1", tokenizer.getLine());
        assertEquals("2", tokenizer.getLine());
        assertNull(tokenizer.getLine());
    }

    @Test
    @DisplayName("Iterable")
    public void test_5() {
        LineTokenizer tokenizer = new LineTokenizer("1\n2\n");
        Iterator<String> iterator = tokenizer.iterator();

        assertTrue(iterator.hasNext());
        assertEquals("1", iterator.next());
        assertEquals("2", iterator.next());
        assertFalse(iterator.hasNext());
    }

}
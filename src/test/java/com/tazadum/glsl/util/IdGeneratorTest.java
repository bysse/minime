package com.tazadum.glsl.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Erik on 2016-10-29.
 */
public class IdGeneratorTest {
    @Test
    public void testGeneration() throws Exception {
        IdGenerator generator = new IdGenerator(new char[]{'a', 'b', 'C'});
        assertEquals("a", generator.next());
        assertEquals("b", generator.next());
        assertEquals("C", generator.next());
        assertEquals("A", generator.next());
        assertEquals("B", generator.next());
        assertEquals("c", generator.next());

        assertEquals("aa", generator.next());
        assertEquals("ab", generator.next());
        assertEquals("aC", generator.next());
        assertEquals("aA", generator.next());

    }
}
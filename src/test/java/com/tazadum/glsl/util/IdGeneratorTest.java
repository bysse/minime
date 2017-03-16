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

        assertEquals("d", generator.next());
        assertEquals("D", generator.next());
        assertEquals("e", generator.next());
        assertEquals("E", generator.next());
    }

    @Test
    public void testRemoval() throws Exception {
        IdGenerator generator = new IdGenerator(new char[]{});
        generator.exclude('a');
        generator.exclude('Z');
        generator.exclude('b');

        assertEquals("A", generator.next());
        assertEquals("B", generator.next());

        IdGenerator cloned = generator.clone();

        assertEquals("A", cloned.next());
        assertEquals("B", cloned.next());

    }
}
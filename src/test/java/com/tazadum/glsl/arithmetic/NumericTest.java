package com.tazadum.glsl.arithmetic;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NumericTest {
    @Test
    public void testFraction() {
        assertFalse(Numeric.create("0.").hasFraction());
        assertFalse(Numeric.create("0").hasFraction());
        assertFalse(Numeric.create("10").hasFraction());
        assertFalse(Numeric.create("20.").hasFraction());

        assertTrue(Numeric.create("0.1").hasFraction());
        assertTrue(Numeric.create("0.10").hasFraction());
        assertTrue(Numeric.create("10.10").hasFraction());
    }

    @Test
    public void testDecimals() {
        assertEquals(0, Numeric.create("2").getDecimals());
        assertEquals(0, Numeric.create("2.").getDecimals());
        assertEquals(0, Numeric.create("2.0").getDecimals());
        assertEquals(0, Numeric.create("2.00").getDecimals());

        assertEquals(1, Numeric.create(".1").getDecimals());
        assertEquals(1, Numeric.create(".10").getDecimals());

        assertEquals(2, Numeric.create("0.12").getDecimals());
        assertEquals(3, Numeric.create("0.102").getDecimals());
        assertEquals(3, Numeric.create("0.1020").getDecimals());
    }
}

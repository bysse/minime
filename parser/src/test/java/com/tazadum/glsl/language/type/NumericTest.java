package com.tazadum.glsl.language.type;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NumericTest {
    @Test
    public void testFraction() {
        assertTrue(Numeric.create("0.").isFloat());
        assertFalse(Numeric.create("0").hasFraction());
        assertFalse(Numeric.create("10").hasFraction());
        assertTrue(Numeric.create("20.").isFloat());

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
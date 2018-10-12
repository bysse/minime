package com.tazadum.glsl.language.type;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NumericTest {
    @Test
    void testFraction() {
        assertTrue(Numeric.create("0.").isFloat());
        assertFalse(Numeric.create("0").hasFraction());
        assertFalse(Numeric.create("10").hasFraction());
        assertTrue(Numeric.create("20.").isFloat());

        assertTrue(Numeric.create("0.1").hasFraction());
        assertTrue(Numeric.create("0.10").hasFraction());
        assertTrue(Numeric.create("10.10").hasFraction());
    }

    @Test
    void testDecimals() {
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

    @Test
    @DisplayName("Test int")
    void testTypes_0() {
        Numeric numeric = Numeric.create("3");
        assertEquals(PredefinedType.INT, numeric.getType());
    }

    @Test
    @DisplayName("Test unsigned")
    void testTypes_1() {
        Numeric numeric = Numeric.create("3u");
        assertEquals(PredefinedType.UINT, numeric.getType());
    }

    @Test
    @DisplayName("Test float")
    void testTypes_2() {
        Numeric n1 = Numeric.create("3.0");
        assertEquals(PredefinedType.FLOAT, n1.getType());

        Numeric n2 = Numeric.create("3f");
        assertEquals(PredefinedType.FLOAT, n2.getType());
    }

    @Test
    @DisplayName("Test double")
    void testTypes_3() {
        Numeric numeric = Numeric.create("3LF");
        assertEquals(PredefinedType.DOUBLE, numeric.getType());
    }
}

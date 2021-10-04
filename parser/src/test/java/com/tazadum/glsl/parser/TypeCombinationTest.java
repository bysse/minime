package com.tazadum.glsl.parser;

import com.tazadum.glsl.exception.TypeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tazadum.glsl.language.type.PredefinedType.*;
import static com.tazadum.glsl.parser.TypeCombination.arithmeticResult;
import static com.tazadum.glsl.parser.TypeCombination.compatibleType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Created by erikb on 2018-10-18.
 */
class TypeCombinationTest {
    @Test
    @DisplayName("Test type compatibility")
    void testCompatibility() throws TypeException {
        assertEquals(INT, compatibleType(INT, INT));
        assertEquals(UINT, compatibleType(INT, UINT));
        assertEquals(DOUBLE, compatibleType(INT, DOUBLE));
        assertEquals(MAT2, compatibleType(MAT2, MAT2X2));

        assertThrows(TypeException.class, () -> compatibleType(MAT2, FLOAT));
    }

    @Test
    @DisplayName("Test type arithmetic")
    void testArithmetic() throws TypeException {
        // ok
        assertEquals(VEC2, arithmeticResult(VEC2, VEC2));
        assertEquals(VEC2, arithmeticResult(VEC2, MAT2));
        assertEquals(VEC2, arithmeticResult(MAT2, VEC2));
        assertEquals(MAT2, arithmeticResult(MAT2, MAT2));
        assertEquals(MAT2, arithmeticResult(MAT2, MAT2));
        assertEquals(VEC3, arithmeticResult(MAT3, VEC3));

        // matrix hell
        assertEquals(DMAT2, arithmeticResult(MAT3X2, DMAT2X3));

        // bad dimensions
        assertThrows(TypeException.class, () -> arithmeticResult(VEC3, MAT2));
        assertThrows(TypeException.class, () -> arithmeticResult(MAT3, VEC4));
        assertThrows(TypeException.class, () -> arithmeticResult(MAT3, MAT2));

        assertThrows(TypeException.class, () -> arithmeticResult(VEC2, VEC3));
    }
}

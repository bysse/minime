package com.tazadum.glsl.parser;

import com.tazadum.glsl.exception.TypeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tazadum.glsl.language.type.PredefinedType.*;
import static com.tazadum.glsl.parser.TypeCombination.compatibleType;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by erikb on 2018-10-18.
 */
class TypeCombinationTest {
    @Test
    @DisplayName("Test type compatibility")
    public void testCompatibility() throws TypeException {
        assertEquals(UINT, compatibleType(INT, UINT));
        assertEquals(DOUBLE, compatibleType(INT, DOUBLE));

        assertEquals(MAT2, compatibleType(MAT2, MAT2X2));
    }
}

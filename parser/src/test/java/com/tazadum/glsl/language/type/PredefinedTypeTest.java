package com.tazadum.glsl.language.type;

import com.tazadum.glsl.exception.NoSuchFieldException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tazadum.glsl.language.type.PredefinedType.*;
import static org.junit.jupiter.api.Assertions.*;

public class PredefinedTypeTest {
    @Test
    @DisplayName("Test swizzle types")
    public void testSwizzle() throws NoSuchFieldException {
        assertEquals(FLOAT, VEC3.fieldType("x"));
        assertEquals(VEC2, VEC2.fieldType("xy"));
        assertEquals(VEC3, VEC4.fieldType("xyz"));
        assertEquals(VEC4, VEC3.fieldType("xyzw"));

        assertEquals(INT, IVEC4.fieldType("r"));
        assertEquals(IVEC2, IVEC2.fieldType("gg"));
        assertEquals(IVEC3, IVEC3.fieldType("bbb"));
        assertEquals(IVEC4, IVEC2.fieldType("rgba"));

        assertEquals(BOOL, BVEC2.fieldType("s"));
        assertEquals(BVEC2, BVEC4.fieldType("st"));
        assertEquals(BVEC3, BVEC3.fieldType("stp"));
        assertEquals(BVEC4, BVEC2.fieldType("stpq"));

        assertEquals(DOUBLE, DVEC2.fieldType("s"));
        assertEquals(DVEC2, DVEC2.fieldType("st"));
        assertEquals(DVEC3, DVEC4.fieldType("stp"));
        assertEquals(DVEC4, DVEC2.fieldType("stpq"));
    }

    @Test
    @DisplayName("Test swizzle failure cases")
    public void testSwizzleFail() {
        assertThrows(NoSuchFieldException.class, () ->
            VEC4.fieldType("fail")
        );

        assertThrows(NoSuchFieldException.class, () ->
            IVEC4.fieldType("rbgargba")
        );

        assertThrows(NoSuchFieldException.class, () ->
            VOID.fieldType("x")
        );
    }

    @Test
    @DisplayName("Test assignment")
    public void testAssignment() {
        assertTrue(INT.isAssignableBy(INT));
        assertFalse(INT.isAssignableBy(FLOAT));
        assertTrue(UINT.isAssignableBy(INT));

        assertTrue(FLOAT.isAssignableBy(INT));
        assertTrue(FLOAT.isAssignableBy(UINT));
        assertTrue(FLOAT.isAssignableBy(DOUBLE));
    }
}

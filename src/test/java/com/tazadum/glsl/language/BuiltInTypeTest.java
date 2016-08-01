package com.tazadum.glsl.language;

import com.tazadum.glsl.exception.TypeException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BuiltInTypeTest {
    @Test
    public void testSwizzle() {
        assertEquals(BuiltInType.FLOAT, BuiltInType.VEC3.fieldType("x"));
        assertEquals(BuiltInType.VEC2, BuiltInType.VEC2.fieldType("xy"));
        assertEquals(BuiltInType.VEC3, BuiltInType.VEC4.fieldType("xyz"));
        assertEquals(BuiltInType.VEC4, BuiltInType.VEC3.fieldType("xyzw"));

        assertEquals(BuiltInType.INT, BuiltInType.IVEC4.fieldType("r"));
        assertEquals(BuiltInType.IVEC2, BuiltInType.IVEC2.fieldType("gg"));
        assertEquals(BuiltInType.IVEC3, BuiltInType.IVEC3.fieldType("bbb"));
        assertEquals(BuiltInType.IVEC4, BuiltInType.IVEC2.fieldType("rgba"));

        assertEquals(BuiltInType.BOOL, BuiltInType.BVEC2.fieldType("s"));
        assertEquals(BuiltInType.BVEC2, BuiltInType.BVEC4.fieldType("st"));
        assertEquals(BuiltInType.BVEC3, BuiltInType.BVEC3.fieldType("stp"));
        assertEquals(BuiltInType.BVEC4, BuiltInType.BVEC2.fieldType("stpq"));
    }

    @Test(expected = TypeException.class)
    public void testInvalidField() {
        BuiltInType.VEC4.fieldType("fail");
    }

    @Test(expected = TypeException.class)
    public void testTooLongSwizzle() {
        BuiltInType.IVEC4.fieldType("rbgargba");
    }

    @Test(expected = TypeException.class)
    public void testNoField() {
        BuiltInType.VOID.fieldType("x");
    }
}

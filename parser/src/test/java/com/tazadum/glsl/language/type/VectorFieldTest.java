package com.tazadum.glsl.language.type;

import com.tazadum.glsl.exception.NoSuchFieldException;
import com.tazadum.glsl.exception.TypeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tazadum.glsl.language.type.PredefinedType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Created by erikb on 2018-10-21.
 */
class VectorFieldTest {
    @Test
    @DisplayName("test simple")
    public void test_1() throws NoSuchFieldException, TypeException {
        assertEquals(FLOAT, new VectorField(FLOAT, "x").getType());
        assertEquals(VEC3, new VectorField(VEC3, "xyz").getType());
        assertEquals(VEC4, new VectorField(VEC3, "rgbr").getType());
        assertEquals(VEC2, new VectorField(VEC3, "tp").getType());
        assertEquals(IVEC2, new VectorField(IVEC3, "xy").getType());
        assertEquals(FLOAT, new VectorField(VEC3, "x").getType());
        assertEquals(INT, new VectorField(IVEC3, "x").getType());
    }

    @Test
    @DisplayName("test widening")
    public void test_2() throws NoSuchFieldException, TypeException {
        assertEquals(VEC3, new VectorField(VEC2, "xyx").getType());
        assertEquals(VEC4, new VectorField(VEC2, "xyxy").getType());

        assertThrows(NoSuchFieldException.class, () -> new VectorField(BOOL, "xyz"));
        assertThrows(NoSuchFieldException.class, () -> new VectorField(VEC2, "xyz"));
    }
}

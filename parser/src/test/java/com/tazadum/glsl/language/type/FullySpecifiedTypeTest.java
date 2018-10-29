package com.tazadum.glsl.language.type;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by erikb on 2018-10-29.
 */
class FullySpecifiedTypeTest {
    @Test
    void testEquality() {
        FullySpecifiedType a = new FullySpecifiedType(PredefinedType.VEC3);
        FullySpecifiedType b = new FullySpecifiedType(PredefinedType.VEC3);

        assertEquals(a.hashCode(), b.hashCode());
        assertTrue(a.equals(b));
    }
}

package com.tazadum.glsl.language.type;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static com.tazadum.glsl.language.type.PredefinedType.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by erikb on 2018-10-23.
 */
class GenTypeIteratorTest {
    @Test
    @DisplayName("Test fixed function")
    void testFixed() {
        List<GLSLType[]> list = collect(new GenTypeIterator(INT, INT));
        assertEquals(1, list.size());
        equals(list.get(0), INT, INT);
    }

    @Test
    @DisplayName("Test single GenType")
    void testGenType() {
        List<GLSLType[]> list = collect(new GenTypeIterator(INT, GenTypes.GenFType));
        assertEquals(4, list.size());
        equals(list.get(0), INT, FLOAT);
        equals(list.get(1), INT, VEC2);
        equals(list.get(2), INT, VEC3);
        equals(list.get(3), INT, VEC4);

        list = collect(new GenTypeIterator(GenTypes.GenFType, INT, GenTypes.GenFType));
        assertEquals(4, list.size());
        equals(list.get(0), FLOAT, INT, FLOAT);
        equals(list.get(1), VEC2, INT, VEC2);
        equals(list.get(2), VEC3, INT, VEC3);
        equals(list.get(3), VEC4, INT, VEC4);
    }

    @Test
    @DisplayName("Test multiple GenTypes")
    void testGenTypes() {
        List<GLSLType[]> list = collect(new GenTypeIterator(GenTypes.GenFType, INT, GenTypes.GenFVectorType));
        assertEquals(12, list.size());

        equals(list.get(0), FLOAT, INT, VEC2);
        equals(list.get(1), VEC2, INT, VEC2);
        equals(list.get(2), VEC3, INT, VEC2);
        equals(list.get(3), VEC4, INT, VEC2);
        equals(list.get(4), FLOAT, INT, VEC3);
        equals(list.get(5), VEC2, INT, VEC3);
        equals(list.get(6), VEC3, INT, VEC3);
        equals(list.get(7), VEC4, INT, VEC3);
        equals(list.get(8), FLOAT, INT, VEC4);
        equals(list.get(9), VEC2, INT, VEC4);
        equals(list.get(10), VEC3, INT, VEC4);
        equals(list.get(11), VEC4, INT, VEC4);
    }

    private List<GLSLType[]> collect(GenTypeIterator iterator) {
        final List<GLSLType[]> list = new ArrayList<>();
        int escape = 100;
        while (iterator.hasNext()) {
            list.add(iterator.next());
            if (escape-- <= 0) {
                fail("Too many iterations while collecting combinations");
            }
        }

        assertThrows(NoSuchElementException.class, () -> iterator.next());

        return list;
    }

    private void equals(GLSLType[] actual, GLSLType... expected) {
        assertEquals(expected.length, actual.length);
        assertArrayEquals(expected, actual);
    }
}

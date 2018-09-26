package com.tazadum.glsl.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tazadum.glsl.util.SourcePosition.create;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SourcePositionMapperTest {
    private SourcePositionMapper mapping;

    @BeforeEach
    void setup() {
        mapping = new SourcePositionMapper();
    }

    @Test
    @DisplayName("Simple line mapping")
    void testMapping_1() {
        mapping.remap(
                create(5, 0),
                create(10, 0)
        );

        assertEquals(create(1, 0), mapping.map(create(1, 0)));
        assertEquals(create(10, 0), mapping.map(create(5, 0)));
        assertEquals(create(20, 0), mapping.map(create(15, 0)));
    }

    @Test
    @DisplayName("Simple column mapping")
    void testMapping_2() {
        mapping.remap(
                create(5, 10),
                create(6, 0)
        );

        assertEquals(create(4, 15), mapping.map(create(4, 15)));
        assertEquals(create(5, 9), mapping.map(create(5, 9)));
        assertEquals(create(6, 0), mapping.map(create(5, 10)));
        assertEquals(create(6, 5), mapping.map(create(5, 15)));
        assertEquals(create(7, 5), mapping.map(create(6, 5)));
    }


    @Test
    @DisplayName("Multiple mappings")
    void testMapping_3() {
        mapping.remap(
                create(5, 10),
                create(6, 0)
        );

        mapping.remap(
                create(5, 15),
                create(7, 0)
        );

        mapping.remap(
                create(10, 5),
                create(11, 0)
        );

        assertEquals(create(5, 9), mapping.map(create(5, 9)));
        assertEquals(create(6, 0), mapping.map(create(5, 10)));
        assertEquals(create(6, 4), mapping.map(create(5, 14)));
        assertEquals(create(8, 5), mapping.map(create(5, 20)));

        assertEquals(create(11, 8), mapping.map(create(8, 8)));

        assertEquals(create(13, 1), mapping.map(create(10, 1)));
        assertEquals(create(13, 5), mapping.map(create(10, 10)));
        assertEquals(create(14, 10), mapping.map(create(11, 10)));
    }

}
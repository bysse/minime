package com.tazadum.glsl.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tazadum.glsl.util.SourcePosition.create;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SourcePositionMapperTest {
    private SourcePositionMapper mapper;

    @BeforeEach
    void setup() {
        mapper = new SourcePositionMapper();
    }

    @Test
    @DisplayName("Simple line mapping")
    void testMapping_1() {
        mapper.remap(
                create(5, 0),
                create(10, 0)
        );

        assertEquals(create(1, 0), mapper.map(create(1, 0)));
        assertEquals(create(10, 0), mapper.map(create(5, 0)));
        assertEquals(create(20, 0), mapper.map(create(15, 0)));
    }

    @Test
    @DisplayName("Simple column mapping")
    void testMapping_2() {
        mapper.remap(
            create(5, 5),
                create(6, 0)
        );

        assertEquals(create(4, 7), mapper.map(create(4, 7)));
        assertEquals(create(5, 4), mapper.map(create(5, 4)));
        assertEquals(create(6, 0), mapper.map(create(5, 5)));
        assertEquals(create(6, 4), mapper.map(create(5, 9)));
        assertEquals(create(7, 5), mapper.map(create(6, 5)));
    }

    @Test
    @DisplayName("Multiple mappings")
    void testMapping_3() {
        mapper.remap(
                create(5, 10),
                create(6, 0)
        );

        mapper.remap(
                create(5, 15),
                create(7, 0)
        );

        mapper.remap(
                create(10, 5),
                create(11, 0)
        );

        assertEquals(create(5, 9), mapper.map(create(5, 9)));
        assertEquals(create(6, 0), mapper.map(create(5, 10)));
        assertEquals(create(6, 4), mapper.map(create(5, 14)));
        assertEquals(create(7, 5), mapper.map(create(5, 20)));

        assertEquals(create(10, 1), mapper.map(create(8, 1)));

        assertEquals(create(12, 1), mapper.map(create(10, 1)));
        assertEquals(create(13, 5), mapper.map(create(10, 10)));
        assertEquals(create(14, 10), mapper.map(create(11, 10)));
    }

    @Test
    @DisplayName("Multiple mappings 2")
    void testMapping_4() {
        mapper.remap(
            create(2, 5),
            create(3, 0)
        );

        mapper.remap(
            create(4, 5),
            create(5, 0)
        );

        assertEquals(create(2, 0), mapper.map(create(2, 0)));
        assertEquals(create(4, 0), mapper.map(create(3, 0)));
        assertEquals(create(5, 0), mapper.map(create(4, 0)));
        assertEquals(create(7, 0), mapper.map(create(5, 0)));
    }

    @Test
    @DisplayName("Hierarchical mappings")
    void testHierarchical() {
        SourcePositionMapper base = new SourcePositionMapper();
        base.remap(create(5, 5), create(6, 0));

        assertEquals(create(5, 0), base.map(create(5, 0)));
        assertEquals(create(6, 0), base.map(create(5, 5)));
        assertEquals(create(7, 0), base.map(create(6, 0)));

        mapper = new SourcePositionMapper(base);
        mapper.remap(create(2, 5), create(3, 0));
        mapper.remap(create(7, 5), create(8, 0));

        assertEquals(create(1, 0), mapper.map(create(1, 0)));
        assertEquals(create(2, 0), mapper.map(create(2, 0)));
        assertEquals(create(3, 1), mapper.map(create(2, 6)));
        assertEquals(create(4, 0), mapper.map(create(3, 0)));
        assertEquals(create(5, 0), mapper.map(create(4, 0)));
        assertEquals(create(6, 1), mapper.map(create(4, 6)));
        assertEquals(create(7, 0), mapper.map(create(5, 0)));
        assertEquals(create(8, 0), mapper.map(create(6, 0)));
        assertEquals(create(9, 0), mapper.map(create(7, 0)));
    }
}

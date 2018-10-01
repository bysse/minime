package com.tazadum.glsl.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tazadum.glsl.util.SourcePosition.create;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SourcePositionMapperTest {
    private static final String TEST = "test";
    private SourcePositionMapper mapper;

    @BeforeEach
    void setup() {
        mapper = new SourcePositionMapper();
    }

    @Test
    @DisplayName("Empty mapping")
    void testMapping_0() {
        assertThrows(IllegalStateException.class, () -> mapper.map(create(1, 0)));
    }

    @Test
    @DisplayName("Simple line mapping")
    void testMapping_1() {
        mapper.remap(create(5, 0), id(10, 0));

        assertEquals(expect(1, 0), mapper.map(create(1, 0)));
        assertEquals(expect(10, 0), mapper.map(create(5, 0)));
        assertEquals(expect(20, 0), mapper.map(create(15, 0)));
    }

    @Test
    @DisplayName("Simple column mapping")
    void testMapping_2() {
        mapper.remap(create(5, 5), id(6, 0));

        assertEquals(expect(4, 7), mapper.map(create(4, 7)));
        assertEquals(expect(5, 4), mapper.map(create(5, 4)));
        assertEquals(expect(6, 0), mapper.map(create(5, 5)));
        assertEquals(expect(6, 4), mapper.map(create(5, 9)));
        assertEquals(expect(7, 5), mapper.map(create(6, 5)));
    }

    @Test
    @DisplayName("Multiple mappings")
    void testMapping_3() {
        mapper.remap(create(5, 10), id(6, 0));
        mapper.remap(create(5, 15), id(7, 0));
        mapper.remap(create(10, 5), id(11, 0));

        assertEquals(expect(5, 9), mapper.map(create(5, 9)));
        assertEquals(expect(6, 0), mapper.map(create(5, 10)));
        assertEquals(expect(6, 4), mapper.map(create(5, 14)));
        assertEquals(expect(7, 5), mapper.map(create(5, 20)));

        assertEquals(expect(10, 1), mapper.map(create(8, 1)));

        assertEquals(expect(12, 1), mapper.map(create(10, 1)));
        assertEquals(expect(11, 5), mapper.map(create(10, 10)));
        assertEquals(expect(12, 10), mapper.map(create(11, 10)));
    }

    @Test
    @DisplayName("Multiple mappings 2")
    void testMapping_4() {
        mapper.remap(create(2, 5), id(3, 0));
        mapper.remap(create(4, 5), id(5, 0));

        assertEquals(expect(2, 0), mapper.map(create(2, 0)));
        assertEquals(expect(4, 0), mapper.map(create(3, 0)));
        assertEquals(expect(5, 0), mapper.map(create(4, 0)));
        assertEquals(expect(6, 0), mapper.map(create(5, 0)));
    }

    @Test
    @DisplayName("Hierarchical mappings")
    void testHierarchical() {
        SourcePositionMapper base = new SourcePositionMapper();
        base.remap(create(5, 5), id(6, 0));

        assertEquals(expect(5, 0), base.map(create(5, 0)));
        assertEquals(expect(6, 0), base.map(create(5, 5)));
        assertEquals(expect(7, 0), base.map(create(6, 0)));

        mapper = new SourcePositionMapper(base);
        mapper.remap(create(2, 5), id(3, 0));
        mapper.remap(create(7, 5), id(8, 0));

        assertEquals(expect(1, 0), mapper.map(create(1, 0)));
        assertEquals(expect(2, 0), mapper.map(create(2, 0)));
        assertEquals(expect(3, 1), mapper.map(create(2, 6)));
        assertEquals(expect(4, 0), mapper.map(create(3, 0)));
        assertEquals(expect(5, 0), mapper.map(create(4, 0)));
        assertEquals(expect(6, 1), mapper.map(create(4, 6)));
        assertEquals(expect(7, 0), mapper.map(create(5, 0)));
        assertEquals(expect(8, 0), mapper.map(create(6, 0)));
        assertEquals(expect(9, 0), mapper.map(create(7, 0)));
    }

    @Test
    @DisplayName("Backwards mapping")
    void testBackwards() {
        mapper.remap(create(5, 5), id(1, 0));
        mapper.remap(create(10, 0), id(6, 0));

        assertEquals(expect(1, 0), mapper.map(create(1, 0)));
        assertEquals(expect(1, 5), mapper.map(create(5, 10)));
        assertEquals(expect(2, 0), mapper.map(create(6, 0)));
        assertEquals(expect(6, 0), mapper.map(create(10, 0)));
    }

    private SourcePositionId id(int line, int col) {
        return SourcePositionId.create(TEST, line, col);
    }

    private SourcePositionId expect(int line, int col) {
        return SourcePositionId.create(TEST, line, col);
    }
}

package com.tazadum.glsl.util;

import org.junit.jupiter.api.Test;

import static com.tazadum.glsl.util.SourcePosition.create;
import static org.junit.jupiter.api.Assertions.*;

class SourcePositionTest {
    @Test
    public void comparative() {
        assertTrue(create(10, 0).isAfterOrEqual(create(5, 0)));
        assertTrue(create(10, 5).isAfterOrEqual(create(10, 4)));
        assertTrue(create(10, 5).isAfterOrEqual(create(10, 5)));
        assertFalse(create(10, 5).isAfterOrEqual(create(10, 6)));
    }
}

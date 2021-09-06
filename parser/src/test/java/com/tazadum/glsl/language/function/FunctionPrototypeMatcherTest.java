package com.tazadum.glsl.language.function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.tazadum.glsl.language.function.FunctionPrototypeMatcher.ANY;
import static com.tazadum.glsl.language.type.PredefinedType.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FunctionPrototypeMatcherTest {
    private FunctionPrototype f_1;
    private FunctionPrototype f_2;
    private FunctionPrototype f_3;

    @BeforeEach
    public void setUp() {
        f_1 = new FunctionPrototype(true, VOID, VOID);
        f_2 = new FunctionPrototype(true, INT, MAT2, INT);
        f_3 = new FunctionPrototype(true, INT, MAT2, FLOAT);
    }

    @Test
    public void testExact() {
        assertTrue(match(f_1, new FunctionPrototypeMatcher(VOID, VOID)));
        assertFalse(match(f_2, new FunctionPrototypeMatcher(INT, VOID)));

        assertTrue(match(f_2, new FunctionPrototypeMatcher(INT, MAT2, INT)));
        assertTrue(match(f_3, new FunctionPrototypeMatcher(INT, MAT2, INT)));

        assertFalse(match(f_2, new FunctionPrototypeMatcher(INT, MAT2, BOOL)));
    }

    @Test
    public void testPartial() {
        assertTrue(match(f_1, new FunctionPrototypeMatcher(ANY, VOID)));

        assertTrue(match(f_2, new FunctionPrototypeMatcher(INT, ANY, INT)));
        assertTrue(match(f_3, new FunctionPrototypeMatcher(ANY, MAT2, FLOAT)));

        assertFalse(match(f_2, new FunctionPrototypeMatcher(INT, ANY, BOOL)));
    }

    private boolean match(FunctionPrototype functionPrototype, FunctionPrototypeMatcher matcher) {
        return matcher.matches(functionPrototype);
    }
}

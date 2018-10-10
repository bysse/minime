package com.tazadum.glsl.language.function;

import com.tazadum.glsl.language.type.PredefinedType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FunctionPrototypeMatcherTest {
    private FunctionPrototype f_1;
    private FunctionPrototype f_2;
    private FunctionPrototype f_3;

    @BeforeEach
    public void setUp() throws Exception {
        f_1 = new FunctionPrototype(true, PredefinedType.VOID, PredefinedType.VOID);
        f_2 = new FunctionPrototype(true, PredefinedType.INT, PredefinedType.MAT2, PredefinedType.INT);
        f_3 = new FunctionPrototype(true, PredefinedType.INT, PredefinedType.MAT2, PredefinedType.FLOAT);
    }

    @Test
    public void testExact() throws Exception {
        assertTrue(match(f_1, new FunctionPrototypeMatcher(PredefinedType.VOID, PredefinedType.VOID)));
        assertFalse(match(f_2, new FunctionPrototypeMatcher(PredefinedType.INT, PredefinedType.VOID)));

        assertTrue(match(f_2, new FunctionPrototypeMatcher(PredefinedType.INT, PredefinedType.MAT2, PredefinedType.FLOAT)));
        assertTrue(match(f_2, new FunctionPrototypeMatcher(PredefinedType.INT, PredefinedType.MAT2, PredefinedType.INT)));
        assertFalse(match(f_3, new FunctionPrototypeMatcher(PredefinedType.INT, PredefinedType.MAT2, PredefinedType.INT)));

        assertFalse(match(f_2, new FunctionPrototypeMatcher(PredefinedType.INT, PredefinedType.MAT2, PredefinedType.BOOL)));
    }

    @Test
    public void testPartial() throws Exception {
        assertTrue(match(f_1, new FunctionPrototypeMatcher(FunctionPrototypeMatcher.ANY, PredefinedType.VOID)));

        assertTrue(match(f_2, new FunctionPrototypeMatcher(FunctionPrototypeMatcher.ANY, PredefinedType.MAT2, PredefinedType.FLOAT)));
        assertTrue(match(f_2, new FunctionPrototypeMatcher(PredefinedType.INT, FunctionPrototypeMatcher.ANY, PredefinedType.INT)));

        assertFalse(match(f_2, new FunctionPrototypeMatcher(PredefinedType.INT, FunctionPrototypeMatcher.ANY, PredefinedType.BOOL)));
    }

    private boolean match(FunctionPrototype functionPrototype, FunctionPrototypeMatcher matcher) {
        return matcher.matches(functionPrototype);
    }
}

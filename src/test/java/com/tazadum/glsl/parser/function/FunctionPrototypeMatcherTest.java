package com.tazadum.glsl.parser.function;

import com.tazadum.glsl.language.BuiltInType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FunctionPrototypeMatcherTest {
    private FunctionPrototype f_1;
    private FunctionPrototype f_2;

    @Before
    public void setUp() throws Exception {
        f_1 = new FunctionPrototype(true, BuiltInType.VOID, BuiltInType.VOID);
        f_2 = new FunctionPrototype(true, BuiltInType.INT, BuiltInType.MAT2, BuiltInType.FLOAT);
    }

    @Test
    public void testExact() throws Exception {
        assertTrue(match(f_1, new FunctionPrototypeMatcher(BuiltInType.VOID, BuiltInType.VOID)));
        assertFalse(match(f_2, new FunctionPrototypeMatcher(BuiltInType.INT, BuiltInType.VOID)));

        assertTrue(match(f_2, new FunctionPrototypeMatcher(BuiltInType.INT, BuiltInType.MAT2, BuiltInType.FLOAT)));
        assertTrue(match(f_2, new FunctionPrototypeMatcher(BuiltInType.INT, BuiltInType.MAT2, BuiltInType.INT)));

        assertFalse(match(f_2, new FunctionPrototypeMatcher(BuiltInType.INT, BuiltInType.MAT2, BuiltInType.BOOL)));
    }

    @Test
    public void testPartial() throws Exception {
        assertTrue(match(f_1, new FunctionPrototypeMatcher(FunctionPrototypeMatcher.ANY, BuiltInType.VOID)));

        assertTrue(match(f_2, new FunctionPrototypeMatcher(FunctionPrototypeMatcher.ANY, BuiltInType.MAT2, BuiltInType.FLOAT)));
        assertTrue(match(f_2, new FunctionPrototypeMatcher(BuiltInType.INT, FunctionPrototypeMatcher.ANY, BuiltInType.INT)));

        assertFalse(match(f_2, new FunctionPrototypeMatcher(BuiltInType.INT, FunctionPrototypeMatcher.ANY, BuiltInType.BOOL)));
    }

    private boolean match(FunctionPrototype functionPrototype, FunctionPrototypeMatcher matcher) {
        return matcher.matches(functionPrototype);
    }
}

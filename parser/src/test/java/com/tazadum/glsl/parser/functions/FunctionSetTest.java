package com.tazadum.glsl.parser.functions;

import com.tazadum.glsl.language.function.FunctionRegistryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Created by erikb on 2018-10-22.
 */
class FunctionSetTest {
    private FunctionRegistryImpl registry;

    @BeforeEach
    void setUp() {
        registry = new FunctionRegistryImpl();
    }

    @Test
    void testConstructorsFunctionSet() {
        new ConstructorsFunctionSet(registry).generate();
    }

    @Test
    void testExponentialFunctionSet() {
        new ExponentialFunctionSet(registry).generate();
    }

    @Test
    void testCommonFunctionSet() {
        new CommonFunctionSet(registry).generate();
    }

    @Test
    void testTrigonometryFunctionSet() {
        new TrigonometryFunctionSet(registry).generate();
    }
}

package com.tazadum.glsl.parser.functions;

import com.tazadum.glsl.language.function.BuiltInFunctionRegistry;
import com.tazadum.glsl.language.function.BuiltInFunctionRegistryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Created by erikb on 2018-10-22.
 */
class FunctionSetTest {
    private BuiltInFunctionRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new BuiltInFunctionRegistryImpl();
    }

    @Test
    void testConstructorsFunctionSet() {
        new ConstructorsFunctionSet().generate(registry);
    }

    @Test
    void testExponentialFunctionSet() {
        new ExponentialFunctionSet().generate(registry);
    }

    @Test
    void testCommonFunctionSet() {
        new CommonFunctionSet().generate(registry);
    }

    @Test
    void testTrigonometryFunctionSet() {
        new TrigonometryFunctionSet().generate(registry);
    }

    @Test
    void testVectorAndMatrixFunctionSet() {
        new VectorAndMatrixFunctionSet().generate(registry);
    }

    @Test
    void testIntegerFunctionSet() {
        new IntegerFunctionSet().generate(registry);
    }

    @Test
    void testTextureFunctionSet() {
        new TextureFunctionSet().generate(registry);
    }
}

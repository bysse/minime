package com.tazadum.glsl.parser.functions;

import com.tazadum.glsl.language.function.BuiltInFunctionRegistry;
import com.tazadum.glsl.language.function.BuiltInFunctionRegistryImpl;
import com.tazadum.glsl.preprocessor.language.GLSLProfile;
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
        new ConstructorsFunctionSet().generate(registry, GLSLProfile.CORE);
    }

    @Test
    void testExponentialFunctionSet() {
        new ExponentialFunctionSet().generate(registry, GLSLProfile.CORE);
    }

    @Test
    void testCommonFunctionSet() {
        new CommonFunctionSet().generate(registry, GLSLProfile.CORE);
    }

    @Test
    void testTrigonometryFunctionSet() {
        new TrigonometryFunctionSet().generate(registry, GLSLProfile.CORE);
    }

    @Test
    void testVectorAndMatrixFunctionSet() {
        new VectorAndMatrixFunctionSet().generate(registry, GLSLProfile.CORE);
    }

    @Test
    void testIntegerFunctionSet() {
        new IntegerFunctionSet().generate(registry, GLSLProfile.CORE);
    }

    @Test
    void testTextureFunctionSet() {
        new TextureFunctionSet().generate(registry, GLSLProfile.CORE);
        new TextureFunctionSet().generate(registry, GLSLProfile.COMPATIBILITY);
    }

    @Test
    void testImageFunctionSet() {
        new ImageFunctionSet().generate(registry, GLSLProfile.COMPATIBILITY);
    }

    @Test
    void testGeometryShaderFunctionSet() {
        new GeometryShaderFunctionSet().generate(registry, GLSLProfile.COMPATIBILITY);
    }

    @Test
    void testFragmentShaderFunctionSet() {
        new FragmentShaderFunctionSet().generate(registry, GLSLProfile.COMPATIBILITY);
    }

    @Test
    void testControlFunctionSet() {
        new ControlFunctionSet().generate(registry, GLSLProfile.COMPATIBILITY);
    }
}

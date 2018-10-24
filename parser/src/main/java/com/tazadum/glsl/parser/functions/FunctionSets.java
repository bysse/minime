package com.tazadum.glsl.parser.functions;

import com.tazadum.glsl.language.function.BuiltInFunctionRegistry;
import com.tazadum.glsl.parser.ShaderType;
import com.tazadum.glsl.preprocessor.language.GLSLProfile;

/**
 * Created by erikb on 2018-10-23.
 */
public class FunctionSets {
    public static BuiltInFunctionRegistry applyFunctions(BuiltInFunctionRegistry registry, ShaderType shaderType, GLSLProfile profile) {
        new CommonFunctionSet().generate(registry, profile);
        new ConstructorsFunctionSet().generate(registry, profile);
        new ControlFunctionSet().generate(registry, profile);
        new ExponentialFunctionSet().generate(registry, profile);
        new ImageFunctionSet().generate(registry, profile);
        new IntegerFunctionSet().generate(registry, profile);
        new TextureFunctionSet().generate(registry, profile);
        new TrigonometryFunctionSet().generate(registry, profile);
        new VectorAndMatrixFunctionSet().generate(registry, profile);

        if (shaderType == ShaderType.GEOMETRY) {
            new GeometryShaderFunctionSet().generate(registry, profile);
        }

        if (shaderType == ShaderType.FRAGMENT) {
            new FragmentShaderFunctionSet().generate(registry, profile);
        }

        return registry;
    }
}

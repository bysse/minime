package com.tazadum.glsl.parser.functions;

import com.tazadum.glsl.language.function.BuiltInFunctionRegistry;
import com.tazadum.glsl.preprocessor.language.GLSLProfile;

import static com.tazadum.glsl.language.type.PredefinedType.INT;
import static com.tazadum.glsl.language.type.PredefinedType.VOID;

/**
 * Geometry Shader Functions
 * Created by erikb on 2018-10-24.
 */
public class GeometryShaderFunctionSet implements FunctionSet {

    @Override
    public void generate(BuiltInFunctionRegistry registry, GLSLProfile profile) {
        BuiltInFunctionRegistry.FunctionDeclarator def = registry.getFunctionDeclarator();

        def.function("EmitStreamVertex", VOID, INT);
        def.function("EndStreamPrimitive", VOID, INT);
        def.function("EmitVertex", VOID);
        def.function("EndPrimitive", VOID);
    }
}

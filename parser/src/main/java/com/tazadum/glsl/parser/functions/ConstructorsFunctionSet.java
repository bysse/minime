package com.tazadum.glsl.parser.functions;

import com.tazadum.glsl.language.function.BuiltInFunctionRegistry;
import com.tazadum.glsl.language.type.GenTypes;
import com.tazadum.glsl.language.type.PredefinedType;
import com.tazadum.glsl.preprocessor.language.GLSLProfile;

import static com.tazadum.glsl.language.type.GenTypes.*;
import static com.tazadum.glsl.language.type.PredefinedType.*;

/**
 * Created by erikb on 2018-10-22.
 */
public class ConstructorsFunctionSet implements FunctionSet {
    @Override
    public void generate(BuiltInFunctionRegistry registry, GLSLProfile profile) {
        BuiltInFunctionRegistry.FunctionDeclarator declarator = registry.getFunctionDeclarator();

        // scalar type construction
        declarator.function("int", INT, GenSingleType);
        declarator.function("uint", UINT, GenSingleType);
        declarator.function("bool", BOOL, GenSingleType);
        declarator.function("float", FLOAT, GenSingleType);
        declarator.function("double", DOUBLE, GenSingleType);

        // vector construction
        iterate(declarator, GenVec2Type, VEC2);
        iterate(declarator, GenVec2Type, GenScalarType);
        iterate(declarator, GenVec2Type, GenScalarType, GenScalarType);

        // vec3
        for (PredefinedType vec3 : GenVec3Type.types) {
            final String name = vec3.token();
            final PredefinedType scalar = vec3.baseType();
            final PredefinedType vec2 = GenVec2Type.fromBase(scalar);

            declarator.function(name, vec3, vec3);
            declarator.function(name, vec3, scalar);
            declarator.function(name, vec3, scalar, scalar, scalar);
            declarator.function(name, vec3, vec2, scalar);
            declarator.function(name, vec3, scalar, vec2);
        }

        // vec4
        for (PredefinedType vec4 : GenVec4Type.types) {
            final String name = vec4.token();
            final PredefinedType scalar = vec4.baseType();
            final PredefinedType vec3 = GenVec3Type.fromBase(scalar);
            final PredefinedType vec2 = GenVec2Type.fromBase(scalar);

            declarator.function(name, vec4, vec4);
            declarator.function(name, vec4, vec2, vec2);
            declarator.function(name, vec4, scalar);
            declarator.function(name, vec4, scalar, scalar, scalar, scalar);
            declarator.function(name, vec4, vec3, scalar);
            declarator.function(name, vec4, scalar, vec3);
            declarator.function(name, vec4, vec2, scalar, scalar);
            declarator.function(name, vec4, scalar, vec2, scalar);
            declarator.function(name, vec4, scalar, scalar, vec2);
        }

        // mat2 construction
        for (PredefinedType mat2 : GenMat2Type.types) {
            final String name = mat2.token();
            final PredefinedType scalar = mat2.baseType();
            final PredefinedType vec2 = GenVec2Type.fromBase(scalar);

            declarator.function(name, mat2, scalar);
            declarator.function(name, mat2, mat2);
            declarator.function(name, mat2, vec2, vec2);
            declarator.function(name, mat2, scalar, scalar, scalar, scalar);
            declarator.function(name, mat2, vec2, scalar, scalar);
            declarator.function(name, mat2, scalar, vec2, scalar);
            declarator.function(name, mat2, scalar, scalar, vec2);
        }

        // mat3 construction
        for (PredefinedType mat3 : GenMat3Type.types) {
            final String name = mat3.token();
            final PredefinedType scalar = mat3.baseType();
            final PredefinedType vec3 = GenVec3Type.fromBase(scalar);

            declarator.function(name, mat3, scalar);
            declarator.function(name, mat3, vec3, vec3, vec3);
            declarator.function(name, mat3,
                scalar, scalar, scalar,
                scalar, scalar, scalar,
                scalar, scalar, scalar);
        }

        // mat4 construction
        for (PredefinedType mat4 : GenMat4Type.types) {
            final String name = mat4.token();
            final PredefinedType scalar = mat4.baseType();
            final PredefinedType vec4 = GenVec4Type.fromBase(scalar);

            declarator.function(name, mat4, scalar);
            declarator.function(name, mat4, vec4, vec4, vec4, vec4);
            declarator.function(name, mat4, scalar, scalar, scalar, scalar,
                scalar, scalar, scalar, scalar,
                scalar, scalar, scalar, scalar,
                scalar, scalar, scalar, scalar);
        }
    }

    private void iterate(BuiltInFunctionRegistry.FunctionDeclarator declarator, GenTypes constructor, Object... parameterTypes) {
        final Object[] parameters = new Object[1 + parameterTypes.length];
        System.arraycopy(parameterTypes, 0, parameters, 1, parameterTypes.length);

        for (PredefinedType type : constructor.types) {
            String name = type.token();
            parameters[0] = type;
            declarator.function(name, parameters);
        }
    }
}

package com.tazadum.glsl.parser.functions;

import com.tazadum.glsl.language.function.BuiltInFunctionRegistry;
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

        declarator.function("int", UINT, GenVec2Type);
        declarator.function("uint", UINT, GenVec2Type);
        declarator.function("bool", BOOL, GenVec2Type);
        declarator.function("float", FLOAT, GenVec2Type);
        declarator.function("double", DOUBLE, GenVec2Type);

        declarator.function("int", UINT, GenVec3Type);
        declarator.function("uint", UINT, GenVec3Type);
        declarator.function("bool", BOOL, GenVec3Type);
        declarator.function("float", FLOAT, GenVec3Type);
        declarator.function("double", DOUBLE, GenVec3Type);

        declarator.function("int", UINT, GenVec4Type);
        declarator.function("uint", UINT, GenVec4Type);
        declarator.function("bool", BOOL, GenVec4Type);
        declarator.function("float", FLOAT, GenVec4Type);
        declarator.function("double", DOUBLE, GenVec4Type);

        // vec2 construction
        for (PredefinedType vec2 : GenVec2Type.types) {
            final String name = vec2.token();
            declarator.function(name, vec2, GenVec2Type);
            declarator.function(name, vec2, GenSingleType);
            declarator.function(name, vec2, GenSingleType, GenSingleType);

            declarator.function(name, vec2, GenVec3Type);
        }

        // vec3
        for (PredefinedType vec3 : GenVec3Type.types) {
            final String name = vec3.token();

            declarator.function(name, vec3, GenVec3Type);
            declarator.function(name, vec3, GenSingleType);
            declarator.function(name, vec3, GenSingleType, GenSingleType, GenSingleType);
            declarator.function(name, vec3, GenVec2Type, GenSingleType);
            declarator.function(name, vec3, GenSingleType, GenVec2Type);

            declarator.function(name, vec3, GenVec4Type);
        }

        // vec4
        for (PredefinedType vec4 : GenVec4Type.types) {
            final String name = vec4.token();

            declarator.function(name, vec4, GenVec4Type);
            declarator.function(name, vec4, GenVec2Type, GenVec2Type);
            declarator.function(name, vec4, GenSingleType);
            declarator.function(name, vec4, GenSingleType, GenSingleType, GenSingleType, GenSingleType);
            declarator.function(name, vec4, GenVec3Type, GenSingleType);
            declarator.function(name, vec4, GenSingleType, GenVec3Type);
            declarator.function(name, vec4, GenVec2Type, GenSingleType, GenSingleType);
            declarator.function(name, vec4, GenSingleType, GenVec2Type, GenSingleType);
            declarator.function(name, vec4, GenSingleType, GenSingleType, GenVec2Type);
        }

        declarator.function("vec4", VEC4, MAT2);
        declarator.function("dvec4", DVEC4, DMAT2);

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
}

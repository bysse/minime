package com.tazadum.glsl.parser.functions;

import com.tazadum.glsl.language.function.FunctionRegistry;
import com.tazadum.glsl.language.type.GenTypes;
import com.tazadum.glsl.language.type.PredefinedType;

import static com.tazadum.glsl.language.type.GenTypes.*;
import static com.tazadum.glsl.language.type.PredefinedType.*;

/**
 * Created by erikb on 2018-10-22.
 */
public class ConstructorsFunctionSet extends FunctionSet {
    public ConstructorsFunctionSet(FunctionRegistry functionRegistry) {
        super(functionRegistry);
    }

    @Override
    public void generate() {
        // scalar type construction
        function("int", INT, GenSingleType);
        function("uint", UINT, GenSingleType);
        function("bool", BOOL, GenSingleType);
        function("float", FLOAT, GenSingleType);
        function("double", DOUBLE, GenSingleType);

        // vector construction
        function(GenVec2Type, VEC2);
        function(GenVec2Type, GenScalarType);
        function(GenVec2Type, GenScalarType, GenScalarType);

        // vec3
        for (PredefinedType vec3 : GenVec3Type.types) {
            final String name = vec3.token();
            final PredefinedType scalar = vec3.baseType();
            final PredefinedType vec2 = GenVec2Type.fromBase(scalar);

            function(name, vec3, vec3);
            function(name, vec3, scalar);
            function(name, vec3, scalar, scalar, scalar);
            function(name, vec3, vec2, scalar);
            function(name, vec3, scalar, vec2);
        }

        // vec4
        for (PredefinedType vec4 : GenVec4Type.types) {
            final String name = vec4.token();
            final PredefinedType scalar = vec4.baseType();
            final PredefinedType vec3 = GenVec3Type.fromBase(scalar);
            final PredefinedType vec2 = GenVec2Type.fromBase(scalar);

            function(name, vec4, vec4);
            function(name, vec4, vec2, vec2);
            function(name, vec4, scalar);
            function(name, vec4, scalar, scalar, scalar, scalar);
            function(name, vec4, vec3, scalar);
            function(name, vec4, scalar, vec3);
            function(name, vec4, vec2, scalar, scalar);
            function(name, vec4, scalar, vec2, scalar);
            function(name, vec4, scalar, scalar, vec2);
        }

        // mat2 construction
        for (PredefinedType mat2 : GenMat2Type.types) {
            final String name = mat2.token();
            final PredefinedType scalar = mat2.baseType();
            final PredefinedType vec2 = GenVec2Type.fromBase(scalar);

            function(name, mat2, scalar);
            function(name, mat2, mat2);
            function(name, mat2, vec2, vec2);
            function(name, mat2, scalar, scalar, scalar, scalar);
            function(name, mat2, vec2, scalar, scalar);
            function(name, mat2, scalar, vec2, scalar);
            function(name, mat2, scalar, scalar, vec2);
        }

        // mat3 construction
        for (PredefinedType mat3 : GenMat3Type.types) {
            final String name = mat3.token();
            final PredefinedType scalar = mat3.baseType();
            final PredefinedType vec3 = GenVec3Type.fromBase(scalar);

            function(name, mat3, scalar);
            function(name, mat3, vec3, vec3, vec3);
            function(name, mat3,
                scalar, scalar, scalar,
                scalar, scalar, scalar,
                scalar, scalar, scalar);
        }

        // mat4 construction
        for (PredefinedType mat4 : GenMat4Type.types) {
            final String name = mat4.token();
            final PredefinedType scalar = mat4.baseType();
            final PredefinedType vec4 = GenVec4Type.fromBase(scalar);

            function(name, mat4, scalar);
            function(name, mat4, vec4, vec4, vec4, vec4);
            function(name, mat4, scalar, scalar, scalar, scalar,
                scalar, scalar, scalar, scalar,
                scalar, scalar, scalar, scalar,
                scalar, scalar, scalar, scalar);
        }
    }

    private void function(GenTypes constructor, Object... parameterTypes) {
        final Object[] parameters = new Object[1 + parameterTypes.length];
        System.arraycopy(parameterTypes, 0, parameters, 1, parameterTypes.length);

        for (PredefinedType type : constructor.types) {
            String name = type.token();
            parameters[0] = type;
            function(name, parameters);
        }
    }
}

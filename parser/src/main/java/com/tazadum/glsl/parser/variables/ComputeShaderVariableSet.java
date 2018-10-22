package com.tazadum.glsl.parser.variables;

import com.tazadum.glsl.parser.ShaderType;
import com.tazadum.glsl.preprocessor.language.GLSLProfile;

import static com.tazadum.glsl.language.model.StorageQualifier.CONST;
import static com.tazadum.glsl.language.model.StorageQualifier.IN;
import static com.tazadum.glsl.language.type.PredefinedType.INT;
import static com.tazadum.glsl.language.type.PredefinedType.UVEC3;

/**
 * Created by erikb on 2018-10-22.
 */
public class ComputeShaderVariableSet extends VariableSet {
    public ComputeShaderVariableSet(GLSLProfile profile) {
        super(profile, ShaderType.COMPUTE);
        add(variable(UVEC3, "gl_NumWorkGroups", IN));
        add(variable(UVEC3, "gl_WorkGroupSize", CONST));

        add(variable(UVEC3, "gl_WorkGroupID", IN));
        add(variable(UVEC3, "gl_LocalInvocationID", IN));
        add(variable(UVEC3, "gl_GlobalInvocationID", IN));
        add(variable(INT, "gl_LocalInvocationIndex", IN));

    }
}

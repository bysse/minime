package com.tazadum.glsl.parser.variables;

import com.tazadum.glsl.parser.ShaderType;
import com.tazadum.glsl.preprocessor.language.GLSLProfile;

import static com.tazadum.glsl.language.model.StorageQualifier.*;
import static com.tazadum.glsl.language.type.PredefinedType.*;

/**
 * Created by erikb on 2018-10-22.
 */
public class FragmentShaderVariableSet extends VariableSet {
    public FragmentShaderVariableSet(GLSLProfile profile) {
        super(profile, ShaderType.FRAGMENT);
        add(variable(VEC4, "gl_FragCoord", IN));
        add(variable(BOOL, "gl_FrontFacing", IN));
        add(variable(FLOAT, ARRAY_SIZE_OF_UNKNOWNS, "gl_ClipDistance", IN));
        add(variable(FLOAT, ARRAY_SIZE_OF_UNKNOWNS, "gl_CullDistance", IN));
        add(variable(VEC2, "gl_PointCoord", IN));
        add(variable(INT, "gl_PrimitiveID", IN));
        add(variable(INT, "gl_SampleID", IN));
        add(variable(VEC2, "gl_SamplePosition", IN));
        add(variable(INT, ARRAY_SIZE_OF_UNKNOWNS, "gl_SampleMaskIn", IN));
        add(variable(INT, "gl_Layout", IN));
        add(variable(INT, "gl_ViewportIndex", IN));
        add(variable(BOOL, "gl_HelperInvocation", IN));

        add(variable(FLOAT, "gl_FragDepth", OUT));
        add(variable(INT, ARRAY_SIZE_OF_UNKNOWNS, "gl_SampleMask", OUT));

        if (profile == GLSLProfile.COMPATIBILITY) {
            add(variable(VEC4, "gl_FragColor"));
            add(variable(VEC4, ARRAY_SIZE_OF_UNKNOWNS, "gl_FragData"));

            add(block("gl_PerFragment", null, IN, 0,
                variable(FLOAT, "gl_FogFragCoord", IN),
                variable(VEC4, ARRAY_SIZE_OF_UNKNOWNS, "gl_TexCoord", IN),
                variable(VEC4, "gl_Color", IN),
                variable(VEC4, "gl_SecondaryColor", IN)
            ));
        }

        add(block("gl_DepthRangeParameters", "gl_DepthRange", UNIFORM, 0,
            variable(FLOAT, "near"),
            variable(FLOAT, "far"),
            variable(FLOAT, "diff")
        ));
        add(variable(INT, "gl_NumSamples", UNIFORM));
    }
}

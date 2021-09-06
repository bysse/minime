package com.tazadum.glsl.parser.variables;

import com.tazadum.glsl.language.type.ArrayType;
import com.tazadum.glsl.parser.ShaderType;
import com.tazadum.glsl.preprocessor.language.GLSLProfile;

import static com.tazadum.glsl.language.model.StorageQualifier.*;
import static com.tazadum.glsl.language.type.PredefinedType.*;

/**
 * Created by erikb on 2018-10-22.
 */
public class TessellationShaderVariableSet extends VariableSet {
    public TessellationShaderVariableSet(GLSLProfile profile) {
        super(profile, ShaderType.TESSELLATION_EVALUATION);
        add(variable(INT, "gl_PatchVerticesIn", IN));
        add(variable(INT, "gl_PrimitiveID", IN));
        add(variable(INT, "gl_InvocationID", IN));
        add(variable(VEC3, "gl_TessCoord", IN));

        add(variable(FLOAT, 4, "gl_TessLevelOuter", PATCH, OUT));
        add(variable(FLOAT, 2, "gl_TessLevelInner", PATCH, OUT));

        if (profile != GLSLProfile.COMPATIBILITY) {
            add(block("gl_PerVertex", "gl_in", IN, ARRAY_SIZE_OF_UNKNOWNS,
                    variable(VEC4, "gl_Position"),
                    variable(FLOAT, "gl_PointSize"),
                    variable(FLOAT, ArrayType.UNKNOWN_LENGTH, "gl_ClipDistance"),
                    variable(FLOAT, ArrayType.UNKNOWN_LENGTH, "gl_CullDistance")
            ));

            add(block("gl_PerVertex", null, IN, ARRAY_SIZE_OF_UNKNOWNS,
                    variable(VEC4, "gl_Position"),
                    variable(FLOAT, "gl_PointSize"),
                    variable(FLOAT, ArrayType.UNKNOWN_LENGTH, "gl_ClipDistance"),
                    variable(FLOAT, ArrayType.UNKNOWN_LENGTH, "gl_CullDistance")
            ));
        } else {
            add(block("gl_PerVertex", "gl_in", IN, ARRAY_SIZE_OF_UNKNOWNS,
                    variable(VEC4, "gl_Position"),
                    variable(FLOAT, "gl_PointSize"),
                    variable(FLOAT, ARRAY_SIZE_OF_UNKNOWNS, "gl_ClipDistance"),
                    variable(FLOAT, ARRAY_SIZE_OF_UNKNOWNS, "gl_CullDistance"),

                    variable(VEC4, "gl_ClipVertex"),
                    variable(VEC4, "gl_FrontColor"),
                    variable(VEC4, "gl_BackColor"),
                    variable(VEC4, "gl_FrontSecondaryColor"),
                    variable(VEC4, "gl_BackSecondaryColor"),
                    variable(VEC4, ARRAY_SIZE_OF_UNKNOWNS, "gl_TexCoord"),
                    variable(FLOAT, "gl_FogFragCoord")
            ));

            add(block("gl_PerVertex", null, IN, ARRAY_SIZE_OF_UNKNOWNS,
                    variable(VEC4, "gl_Position"),
                    variable(FLOAT, "gl_PointSize"),
                    variable(FLOAT, ARRAY_SIZE_OF_UNKNOWNS, "gl_ClipDistance"),
                    variable(FLOAT, ARRAY_SIZE_OF_UNKNOWNS, "gl_CullDistance"),

                    variable(VEC4, "gl_ClipVertex"),
                    variable(VEC4, "gl_FrontColor"),
                    variable(VEC4, "gl_BackColor"),
                    variable(VEC4, "gl_FrontSecondaryColor"),
                    variable(VEC4, "gl_BackSecondaryColor"),
                    variable(VEC4, ARRAY_SIZE_OF_UNKNOWNS, "gl_TexCoord"),
                    variable(FLOAT, "gl_FogFragCoord")
            ));
        }
    }
}

package com.tazadum.glsl.parser.variables;

import com.tazadum.glsl.parser.ShaderType;
import com.tazadum.glsl.preprocessor.language.GLSLProfile;

import static com.tazadum.glsl.language.model.StorageQualifier.IN;
import static com.tazadum.glsl.language.model.StorageQualifier.OUT;
import static com.tazadum.glsl.language.type.PredefinedType.*;

/**
 * Created by erikb on 2018-10-22.
 */
public class GeometryShaderVariableSet extends VariableSet {
    public GeometryShaderVariableSet(GLSLProfile profile) {
        super(profile, ShaderType.GEOMETRY);
        add(variable(INT, "gl_PrimitiveIDIn", IN));
        add(variable(INT, "gl_InvocationID", IN));

        add(variable(INT, "gl_PrimitiveID", OUT));
        add(variable(INT, "gl_Layer", OUT));
        add(variable(INT, "gl_ViewportIndex", OUT));

        if (profile != GLSLProfile.COMPATIBILITY) {
            add(block("gl_PerVertex", "gl_in", IN, ARRAY_SIZE_OF_UNKNOWNS,
                    variable(VEC4, "gl_Position"),
                    variable(FLOAT, "gl_PointSize"),
                    variable(FLOAT, ARRAY_SIZE_OF_UNKNOWNS, "gl_ClipDistance"),
                    variable(FLOAT, ARRAY_SIZE_OF_UNKNOWNS, "gl_CullDistance")
            ));

            add(block("gl_PerVertex", null, IN, ARRAY_SIZE_OF_UNKNOWNS,
                    variable(VEC4, "gl_Position"),
                    variable(FLOAT, "gl_PointSize"),
                    variable(FLOAT, ARRAY_SIZE_OF_UNKNOWNS, "gl_ClipDistance"),
                    variable(FLOAT, ARRAY_SIZE_OF_UNKNOWNS, "gl_CullDistance")
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

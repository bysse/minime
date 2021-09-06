package com.tazadum.glsl.parser.variables;

import com.tazadum.glsl.parser.ShaderType;
import com.tazadum.glsl.preprocessor.language.GLSLProfile;

import static com.tazadum.glsl.language.model.StorageQualifier.IN;
import static com.tazadum.glsl.language.model.StorageQualifier.OUT;
import static com.tazadum.glsl.language.type.PredefinedType.*;

/**
 * Created by erikb on 2018-10-22.
 */
public class VertexShaderVariableSet extends VariableSet {
    public VertexShaderVariableSet(GLSLProfile profile) {
        super(profile, ShaderType.VERTEX);
        add(variable(INT, "gl_VertexID", IN));
        add(variable(INT, "gl_InstanceID", IN));
        add(variable(INT, "gl_DrawID", IN));
        add(variable(INT, "gl_BaseVertex", IN));
        add(variable(INT, "gl_BaseInstance", IN));

        if (profile != GLSLProfile.COMPATIBILITY) {
            add(block("gl_PerVertex", null, OUT, 0,
                    variable(VEC4, "gl_Position"),
                    variable(FLOAT, "gl_PointSize"),
                    variable(FLOAT, ARRAY_SIZE_OF_UNKNOWNS, "gl_ClipDistance"),
                    variable(FLOAT, ARRAY_SIZE_OF_UNKNOWNS, "gl_CullDistance")
            ));
        } else {
            add(block("gl_PerVertex", null, OUT, 0,
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

            add(variable(VEC4, "gl_Color", IN));
            add(variable(VEC4, "gl_SecondaryColor", IN));
            add(variable(VEC3, "gl_Normal", IN));
            add(variable(VEC4, "gl_Vertex", IN));

            add(variable(VEC4, "gl_MultiTexCoord0", IN));
            add(variable(VEC4, "gl_MultiTexCoord1", IN));
            add(variable(VEC4, "gl_MultiTexCoord2", IN));
            add(variable(VEC4, "gl_MultiTexCoord3", IN));
            add(variable(VEC4, "gl_MultiTexCoord4", IN));
            add(variable(VEC4, "gl_MultiTexCoord5", IN));
            add(variable(VEC4, "gl_MultiTexCoord6", IN));
            add(variable(VEC4, "gl_MultiTexCoord7", IN));

            add(variable(FLOAT, "gl_FogCoord", IN));
        }
    }
}

package com.tazadum.glsl.parser.variables;

import com.tazadum.glsl.preprocessor.language.GLSLProfile;

import static com.tazadum.glsl.language.model.StorageQualifier.UNIFORM;
import static com.tazadum.glsl.language.type.PredefinedType.*;

/**
 * Created by erikb on 2018-10-22.
 */
public class ShaderToyVariableSet extends FragmentShaderVariableSet {
    public ShaderToyVariableSet(GLSLProfile profile) {
        super(profile);

        add(variable(VEC3, "iResolution", UNIFORM));
        add(variable(FLOAT, "iTime", UNIFORM));
        add(variable(FLOAT, "iTimeDelta", UNIFORM));
        add(variable(INT, "iFrame", UNIFORM));
        add(variable(FLOAT, 4, "iChannelTime", UNIFORM));
        add(variable(VEC3, 4, "iChannelResolution", UNIFORM));
        add(variable(VEC4, "iMouse", UNIFORM));
        add(variable(VEC4, "iDate", UNIFORM));
        add(variable(FLOAT, "iSampleRate", UNIFORM));

        add(variable(SAMPLER2D, "iChannel0", UNIFORM));
        add(variable(SAMPLER2D, "iChannel1", UNIFORM));
        add(variable(SAMPLER2D, "iChannel2", UNIFORM));
        add(variable(SAMPLER2D, "iChannel3", UNIFORM));
    }
}

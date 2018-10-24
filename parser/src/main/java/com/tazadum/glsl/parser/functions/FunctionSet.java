package com.tazadum.glsl.parser.functions;

import com.tazadum.glsl.language.function.BuiltInFunctionRegistry;
import com.tazadum.glsl.preprocessor.language.GLSLProfile;

/**
 * Created by erikb on 2018-10-22.
 */
public interface FunctionSet {
    void generate(BuiltInFunctionRegistry registry, GLSLProfile profile);
}

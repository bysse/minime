package com.tazadum.glsl.preprocessor;

import com.tazadum.glsl.util.io.Source;

import java.io.IOException;

/**
 * Class for handling the preprocessing of a GLSL source file.
 */
public interface Preprocessor {
    void define(String macro, String value);

    void define(String macro, String[] parameters, String template);

    String process(Source source) throws IOException;
}

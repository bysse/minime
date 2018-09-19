package com.tazadum.glsl.preprocessor;

import java.io.IOException;
import java.io.InputStream;

/**
 * Class for handling the preprocessing of a GLSL source file.
 */
public interface Preprocessor {
    void define(String macro, String value);

    void process(InputStream inputStream) throws IOException;
}

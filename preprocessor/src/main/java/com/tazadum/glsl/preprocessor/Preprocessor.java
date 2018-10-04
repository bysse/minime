package com.tazadum.glsl.preprocessor;

import com.tazadum.glsl.util.io.Source;

import java.io.IOException;
import java.util.List;

/**
 * Class for handling the preprocessing of a GLSL source file.
 */
public interface Preprocessor {
    /**
     * Define an object-like macro.
     *
     * @param macro The name of the macro.
     * @param value The value of the macro.
     */
    void define(String macro, String value);

    /**
     * Define a function-like macro.
     *
     * @param macro      The name of the macro.
     * @param parameters The parameter names.
     * @param template   The macro template.
     */
    void define(String macro, String[] parameters, String template);

    /**
     * Process a source file.
     *
     * @param source A source to process.
     * @return A non-null Result object.
     */
    Result process(Source source) throws IOException, PreprocessorException;

    interface Result {
        /**
         * Returns the result source string after preprocessing.
         *
         * @return A non-null string.
         */
        String getSource();

        /**
         * Returns all warnings as a list of strings.
         *
         * @return A non-null string.
         */
        List<String> getWarnings();
    }
}

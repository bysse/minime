package com.tazadum.glsl.preprocessor;

import com.tazadum.glsl.preprocessor.language.GLSLVersion;
import com.tazadum.glsl.util.io.StringSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by erikb on 2018-09-19.
 */
class DefaultPreprocessorTest {
    private DefaultPreprocessor preprocessor;

    @BeforeEach
    void setUp() {
        preprocessor = new DefaultPreprocessor(GLSLVersion.OpenGL46);

        preprocessor.define("MACRO", "expanded");
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("getSourceLines")
    public void test(String source, String result) throws IOException {
        assertEquals(result, preprocessor.process(new StringSource("test", source)));
    }

    private static Arguments[] getSourceLines() {
        return new Arguments[]{
            Arguments.of("A MACRO B", "A expanded B"),
            Arguments.of("A MACRO.B", "A MACRO.B"),
        };
    }

    ;
}

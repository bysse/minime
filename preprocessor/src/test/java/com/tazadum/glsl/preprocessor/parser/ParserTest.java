package com.tazadum.glsl.preprocessor.parser;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Created by erikb on 2018-09-16.
 */
class ParserTest {
    @ParameterizedTest(name = "test {index}: {0}")
    @MethodSource("getSourceLines")
    void testParsing(String title, String source) {
        TestUtil.parse(source);
    }

    private static Arguments[] getSourceLines() {
        return new Arguments[]{
            Arguments.of("ext-1", "extension all : warn"),
            Arguments.of("ext-2", "extension extension_name : require"),
            Arguments.of("ext-3", "extension extension_name : enable"),
            Arguments.of("ext-4", "extension name : disable"),

            Arguments.of("version-1", "version 440"),
            Arguments.of("version-2", "version 440 core"),
            Arguments.of("version-3", "version 440 compatibility"),
            Arguments.of("version-4", "version 440 es"),

            Arguments.of("line-1", "line 100 200"),

            Arguments.of("if-1", "if 1"),
            Arguments.of("if-2", "if SOMETHING"), // this is not supported by the spec
            Arguments.of("if-3", "if (1)"),
            Arguments.of("if-4", "if defined MACRO"),
            Arguments.of("if-5", "if defined(MACRO)"),
            Arguments.of("if-6", "if +1"),
            Arguments.of("if-7", "if -1"),
            Arguments.of("if-8", "if ~1"),
            Arguments.of("if-9", "if !1"),
            Arguments.of("elif-1", "elif 10*20%30"),
            Arguments.of("elif-2", "elif 10+20-0x10"),
            Arguments.of("elif-2", "elif 10 >> 2 << 1"),
            Arguments.of("elif-2", "elif 10 >= 2 <= 1"),
        };
    }
}

package com.tazadum.glsl.preprocessor.parser;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ParserTest {
    @ParameterizedTest(name = "test {index}: {0}")
    @MethodSource("getSourceLines")
    void testParsing(String title, String source) {
        TestUtil.parse(source);
    }

    private static Arguments[] getSourceLines() {
        return new Arguments[]{
            Arguments.of("empty-1", "#"),

            Arguments.of("ext-1", "#extension all : warn"),
            Arguments.of("ext-2", "#extension extension_name : require"),
            Arguments.of("ext-3", "#extension extension_name : enable"),
            Arguments.of("ext-4", "#extension name : disable"),

            Arguments.of("version-1", "#version 440"),
            Arguments.of("version-2", "#version 440 core"),
            Arguments.of("version-3", "#version 440 compatibility"),
            Arguments.of("version-4", "#version 440 es"),

            Arguments.of("line-1", "#line 100"),
            Arguments.of("line-2", "#line 100 200"),

            Arguments.of("pragma-1", "#pragma"),
            Arguments.of("pragma-2", "#pragma include(shared.glsl)"),
            Arguments.of("pragma-3", "#pragma include(../glsl/shared.glsl)"),

            Arguments.of("if-1", "#if 1"),
            Arguments.of("ifdef-1", "#ifdef MACRO"),
            Arguments.of("ifndef-1", "#ifndef MACRO"),
            Arguments.of("else-1", "#else"),
            Arguments.of("elif-1", "#elif 1"),
            Arguments.of("endif-1", "#endif"),
            Arguments.of("undef-1", "#undef MACRO"),

            // const expressions
            Arguments.of("expr-1", "#if SOMETHING"), // this is not supported by the spec
            Arguments.of("expr-3", "#if (1)"),
            Arguments.of("expr-4", "#if defined MACRO"),
            Arguments.of("expr-5", "#if defined(MACRO)"),
            Arguments.of("expr-6", "#if +1"),
            Arguments.of("expr-7", "#if -1"),
            Arguments.of("expr-8", "#if ~1"),
            Arguments.of("expr-9", "#if !1"),
            Arguments.of("expr-10", "#if 10*20%30"),
            Arguments.of("expr-11", "#if 10+20-0x10"),
            Arguments.of("expr-12", "#if 10 >> 2 << 1"),
            Arguments.of("expr-13", "#if 1 < 2&3"),
            Arguments.of("expr-14", "#if 1 <= 2^3"),
            Arguments.of("expr-15", "#if 2|3 > 1"),
            Arguments.of("expr-16", "#if 2 >= 1"),
            Arguments.of("expr-17", "#if 2 == 1"),
            Arguments.of("expr-18", "#if SOMETHING != 1"),
            Arguments.of("expr-19", "#if 0x10"),
            Arguments.of("expr-20", "#if 0123"),

            // logic
            Arguments.of("logic-1", "#if 1 && 2 || 3"),
            Arguments.of("logic-2", "#if 1 != MACRO && (2<<4 || 3) || 2*SOMETHING >= MACRO"),

            // macro declaration
            Arguments.of("macro-1", "#define PLAIN"),
            Arguments.of("macro-2", "#define PLAIN 1"),
            Arguments.of("macro-3", "#define SUM(x,y,z) x+y+z"),

            // comments
            Arguments.of("comment", "#line/*// hello\n */5"),
        };
    }
}

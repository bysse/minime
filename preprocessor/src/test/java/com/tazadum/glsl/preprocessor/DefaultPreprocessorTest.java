package com.tazadum.glsl.preprocessor;

import com.tazadum.glsl.preprocessor.language.GLSLVersion;
import com.tazadum.glsl.util.io.StringSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by erikb on 2018-09-19.
 */
class DefaultPreprocessorTest {
    private DefaultPreprocessor preprocessor;

    @BeforeEach
    void setUp() {
        preprocessor = new DefaultPreprocessor(GLSLVersion.OpenGL46);

        preprocessor.define("MACRO", "expanded");
        preprocessor.define("OPT", new String[0], "opt");
        preprocessor.define("FUNC", new String[]{"x", "y"}, "x+y");
        preprocessor.define("F", new String[]{"x"}, "FUNC(x,x)");
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("getSourceLines")
    void test(String source, String result) throws IOException {
        assertEquals(result, preprocessor.process(new StringSource("test", source)));
    }

    private static Arguments[] getSourceLines() {
        return new Arguments[]{
                Arguments.of("A MACRO B", "A expanded B"),
                Arguments.of("A MACRO MACRO B", "A expanded expanded B"),
                Arguments.of("A MACRO.B", "A MACRO.B"),
                Arguments.of("A OPT() B", "A opt B"),
                Arguments.of("A FUNC(1,2) B", "A 1+2 B"),
                Arguments.of("A F(1+1) B", "A 1+1+1+1 B"),
        };
    }

    @Test
    @DisplayName("Test happy case argument list extraction")
    void testArgumentExtraction() {
        List<String> args = new ArrayList<>();

        int index = preprocessor.getMacroParameters(args, "()", 0);
        assertEquals(2, index);
        assertTrue(args.isEmpty());

        index = preprocessor.getMacroParameters(args, "(1,2,3)", 0);
        assertEquals(7, index);
        assertEquals(3, args.size());
        assertEquals("1", args.get(0));
        assertEquals("2", args.get(1));
        assertEquals("3", args.get(2));

        preprocessor.getMacroParameters(args, "(\",)1\",2*(1+x),f(1,3))", 0);
        assertEquals(3, args.size());
        assertEquals("\",)1\"", args.get(0));
        assertEquals("2*(1+x)", args.get(1));
        assertEquals("f(1,3)", args.get(2));
    }

    @Test
    @DisplayName("Test argument list extraction that should fail")
    void testArgumentExtraction_2() {
        List<String> args = new ArrayList<>();

        assertEquals(1, preprocessor.getMacroParameters(args, "(", 0));
        assertTrue(args.isEmpty());

        assertEquals(0, preprocessor.getMacroParameters(args, "3)", 0));
        assertTrue(args.isEmpty());

        assertEquals(4, preprocessor.getMacroParameters(args, "(1,2", 0));
        assertTrue(args.isEmpty());
    }

    @Test
    @DisplayName("Test token replacement")
    void testTokenReplacement() {
        assertEquals("xxx+y", preprocessor.replaceToken("x+y", "x", "xxx"));
        assertEquals("xxx+xxx", preprocessor.replaceToken("x+x", "x", "xxx"));
        assertEquals("xy z", preprocessor.replaceToken("xy x", "x", "z"));
    }
}

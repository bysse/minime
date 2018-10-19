package com.tazadum.glsl.parser;

import com.tazadum.glsl.TestUtil;
import com.tazadum.glsl.language.ast.Node;
import org.antlr.v4.runtime.ParserRuleContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ASTConvertionTest {
    private static Arguments[] getSnippets() {
        return new Arguments[]{
            differentOutput(
                "const struct S{const struct T{float u;} t;} s={{1}};int a[s.t.u];",
                "struct S{struct T{float u;} t;} s={{1}};int a[1];"),
            differentOutput(
                "const struct S{const struct T{const struct U{float v;} u;} t;} s={{{1}}};int a[s.t.u.v];",
                "struct S{struct T{struct U{float v;} u;} t;} s={{{1}}};int a[1];"),
            sameOutput("struct S{float f;};"),
            sameOutput("struct S{float a,b;};S create(){S s={1,2};return s;}"),
            differentOutput(
                "const int[] a={1,1,2};\nint b[a[1+1]+1]={1,2,3};",
                "int[] a={1,1,2};int b[3]={1,2,3};"
            ),
            differentOutput(
                "const int[] a={1,1};\nint b[a.length()];",
                "int[] a={1,1};int b[2];"
            ),
            differentOutput(
                "const int[2] a={1,1};\nint b[a.length()];",
                "int[2] a={1,1};int b[2];"
            ),
            differentOutput(
                "const struct{float a,b;} s={1,2};\nint b[s.b]={1,2};",
                "struct{float a,b;} s={1,2};int b[2]={1,2};"
            ),
            differentOutput("uniform float iGlobalTime;uniform vec2 iResolution;void main(void) {vec2 uv = gl_FragCoord.xy / iResolution.xy;gl_FragColor = vec4(uv,0.5+0.5*sin(iGlobalTime),1.0);}",
                "uniform float iGlobalTime;uniform vec2 iResolution;void main(){vec2 uv=gl_FragCoord.xy/iResolution.xy;gl_FragColor=vec4(uv,.5+.5*sin(iGlobalTime),1);}"
            ),
        };
    }

    private static Arguments sameOutput(String source) {
        return Arguments.of(source, source);
    }

    private static Arguments differentOutput(String source, String expected) {
        return Arguments.of(source, expected);
    }

    @DisplayName("Test AST Conversion")
    @ParameterizedTest(name = "test {index}: {0}")
    @MethodSource("getSnippets")
    void testAST(String source, String expected) {
        ParserContext parserContext = TestUtil.parserContext();
        ParserRuleContext context = TestUtil.parse(source);
        Node node = TestUtil.ast(context, parserContext);
        assertNotNull(node);

        String output = TestUtil.toString(node);
        assertEquals(expected, output);
    }
}

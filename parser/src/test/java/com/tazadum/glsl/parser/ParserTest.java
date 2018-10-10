package com.tazadum.glsl.parser;

import com.tazadum.glsl.TestUtil;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class ParserTest {
    @ParameterizedTest(name = "test {index}: {0}")
    @MethodSource("getSourceLines")
    void testParsing(String source) {
        TestUtil.parse(source);
    }

    private static String[] getSourceLines() {
        return new String[]{
            "void main() {}",
            "uniform vec3 u_position[2];",
            "uint k = 3u;",
            "float b[]=float[](1,2,3,4,5);",
            "vec2 x=sin(3.14*u_time)>0?vec2(1):vec2(0);",
            "vec4[3][2] a;",

            "struct S { float f; };",
            "struct s { float a; float b; } S;",

            "mat2x2 a = mat2( vec2( 1.0, 0.0 ), vec2( 0.0, 1.0 ) );",
            "mat2x2 b = { vec2( 1.0, 0.0 ), vec2( 0.0, 1.0 ) };",
            "mat2x2 c = { { 1.0, 0.0 }, { 0.0, 1.0 } };",

            "const vec3 zAxis = vec3 (0.0, 0.0, 1.0);",
            "in vec2 texCoord[4];",
            "centroid in vec2 TexCoord;",
            "invariant centroid in vec4 Color;",
            "flat in vec3 myColor;",
            "noperspective centroid in vec2 myTexCoord;",

            "uniform vec4 lightPosition;",
            "uniform vec3 color = vec3(0.7, 0.7, 0.2);",

            "out vec3 normal;",
            "centroid out vec2 TexCoord;",
            "invariant centroid out vec4 Color;",
            "flat out vec3 myColor;",
            "sample out vec4 perSampleColor;",

            "buffer BufferName { int count; } Name;",
            "uniform Transform { mat4 mv; uniform mat3 normal; };",
            "in Material {smooth in vec4 Color1; vec2 TexCoord; };",

            "layout(location = 3) in vec4 normal;",
            "layout(location = start + 2) in vec4 v;",
            "layout(location = 0, component = 3) in float f[6];",
            "layout(xfb_buffer = 1, xfb_stride = 32) out;",
            "lowp ivec2 foo(lowp mat3);",

            "float myfunc (float f, out float g);",
        };
    }
}

package com.tazadum.glsl.parser;

import com.tazadum.glsl.TestUtil;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.arithmetic.IntLeafNode;
import com.tazadum.glsl.language.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.language.model.StorageQualifier;
import com.tazadum.glsl.language.type.FullySpecifiedType;
import com.tazadum.glsl.language.type.Numeric;
import com.tazadum.glsl.language.type.PredefinedType;
import com.tazadum.glsl.language.type.TypeQualifierList;
import com.tazadum.glsl.util.SourcePosition;
import org.antlr.v4.runtime.ParserRuleContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ParserTest {
    private static String[] getSourceLines() {
        return new String[]{
            "void main(){}",
            "uniform vec3 u_position[2];",
            "uint k=3u;",
            "float b[]=float[](1,2,3,4,5);",
            "vec2 x=sin(3.14*u_time)>0?vec2(1):vec2(0);",
            "vec4[3][2] a;",
            "vec4 a;vec2 b=a.xy;",

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
            "int[] a(int[3] a[2]) {}"
        };
    }

    private static Arguments[] getSourceLinesWithResult() {
        return new Arguments[]{
            Arguments.of("void main(){}", "void main(){}"),
            Arguments.of("void main(void){}", "void main(){}"),
            Arguments.of("uniform vec3 u_position[2];", null),
            Arguments.of("uint k=3u;", null),
            Arguments.of("float b[]=float[](1,2,3,4,5);", null),
            Arguments.of("vec2 x=sin(3.14*u_time)>0?vec2(1):vec2(0);", null),
            Arguments.of("vec4[3][2] a;", null),
            Arguments.of("vec4 a;vec2 b=a.xy;", null),

            Arguments.of("struct S { float f; };", "struct S{float f;};"),
            Arguments.of("struct s { float a; float b; } S;", "struct s{float a;float b;} S;"),
            Arguments.of("struct {float f;} s;", "struct{float f;} s;"),

            Arguments.of("mat2x2 a=mat2( vec2( 1.0, 0.0), vec2( 0.0, 1.0 ) );", "mat2x2 a=mat2(vec2(1,0),vec2(0,1));"),
            Arguments.of("mat2x2 b={ vec2( 1.0, 0.0), vec2( 0.0, 1.0 ) };", "mat2x2 b={vec2(1,0),vec2(0,1)};"),
            Arguments.of("mat2x2 c={ { 1.0, 0.0 }, { 0.0, 1.0 } };", "mat2x2 c={{1,0},{0,1}};"),

            Arguments.of("const vec3 zAxis = vec3 (0.0, 0.0, 1.0);", "vec3 zAxis=vec3(0,0,1);"),
            Arguments.of("in vec2 texCoord[4];", null),
            Arguments.of("centroid in vec2 TexCoord;", null),
            Arguments.of("invariant centroid in vec4 Color;", null),
            Arguments.of("flat in vec3 myColor;", null),
            Arguments.of("noperspective centroid in vec2 myTexCoord;", null),

            Arguments.of("uniform vec4 lightPosition;", null),
            Arguments.of("uniform vec3 color = vec3(0.7, 0.7, 0.2);", "uniform vec3 color=vec3(.7,.7,.2);"),

            Arguments.of("out vec3 normal;", null),
            Arguments.of("centroid out vec2 TexCoord;", null),
            Arguments.of("invariant centroid out vec4 Color;", null),
            Arguments.of("flat out vec3 myColor;", null),
            Arguments.of("sample out vec4 perSampleColor;", null),

            Arguments.of("buffer BufferName { int count; } Name;", "buffer BufferName{int count;}Name;"),
            Arguments.of("uniform Transform { mat4 mv; uniform mat3 normal; };", "uniform Transform{mat4 mv;uniform mat3 normal;};"),
            Arguments.of("in Material {smooth in vec4 Color1; vec2 TexCoord; };", "in Material{smooth in vec4 Color1;vec2 TexCoord;};"),

            Arguments.of("layout(location=3) in vec4 normal;", null),
            Arguments.of("layout(location=start+2) in vec4 v;", "layout(location=3) in vec4 v;"), // const expression is resolved to an integer value
            Arguments.of("layout(location=0,component=3) in float f[6];", null),
            Arguments.of("layout(xfb_buffer=1,xfb_stride=32) out;", null),
            Arguments.of("lowp ivec2 foo(lowp mat3);", null),

            Arguments.of("float myfunc(float f,out float g);", null),
            Arguments.of("int[] a(int[3] a[2]){}", null)
        };
    }

    @DisplayName("Test Generated Parser")
    @ParameterizedTest(name = "test {index}: {0}")
    @MethodSource("getSourceLines")
    void testParsing(String source) {
        TestUtil.parse(source);
    }

    @DisplayName("Test AST Conversion")
    @ParameterizedTest(name = "test {index}: {0}")
    @MethodSource("getSourceLines")
    void testAST(String source) {
        ParserContext parserContext = getParserContext();
        ParserRuleContext context = TestUtil.parse(source);
        Node node = TestUtil.ast(context, parserContext);
        assertNotNull(node);
    }

    private ParserContext getParserContext() {
        final ParserContext parserContext = TestUtil.parserContext();

        parserContext.getVariableRegistry().declareVariable(
            parserContext.globalContext(),
            new VariableDeclarationNode(SourcePosition.TOP, true, new FullySpecifiedType(PredefinedType.FLOAT), "u_time", null, null, null)
        );

        TypeQualifierList qualifiers = new TypeQualifierList();
        qualifiers.add(StorageQualifier.CONST);

        parserContext.getVariableRegistry().declareVariable(
            parserContext.globalContext(),
            new VariableDeclarationNode(
                SourcePosition.TOP,
                true,
                new FullySpecifiedType(qualifiers, PredefinedType.INT),
                "start",
                null,
                new IntLeafNode(SourcePosition.TOP, new Numeric(1, 0, PredefinedType.INT)),
                null)
        );

        return parserContext;
    }

    @DisplayName("Test String Conversion")
    @ParameterizedTest(name = "test {index}: {0}")
    @MethodSource("getSourceLinesWithResult")
    void testString(String source, String expected) {
        if (expected == null) {
            expected = source;
        }

        ParserContext parserContext = getParserContext();
        ParserRuleContext context = TestUtil.parse(source);
        Node node = TestUtil.ast(context, parserContext);
        String rendered = TestUtil.toString(node);
        assertEquals(expected, rendered);
    }
}

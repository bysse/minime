package com.tazadum.glsl.parser;

import com.tazadum.glsl.TestUtil;
import com.tazadum.glsl.exception.SourcePositionException;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.util.SourcePosition;
import org.antlr.v4.runtime.ParserRuleContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Created by erikb on 2018-10-18.
 */
class TypeVisitorTest {
    @DisplayName("Test AST Conversion")
    @ParameterizedTest(name = "test {index}: {0}")
    @MethodSource("getSnippets")
    void testAST(String source, SourcePosition sourcePosition) {
        ParserContext parserContext = TestUtil.parserContext();
        ParserRuleContext context = TestUtil.parse(source);

        Node node = TestUtil.ast(context, parserContext);
        assertNotNull(node);

        try {
            TestUtil.typeCheck(node, parserContext);

            if (sourcePosition != null) {
                fail("No exception thrown! Expected type problem at " + sourcePosition.format());
            }
        } catch (SourcePositionException e) {
            if (sourcePosition == null) {
                e.printStackTrace();
                fail(e.getSourcePosition().format() + ": Did not expect an exception");
            } else {
                if (!e.getSourcePosition().equals(sourcePosition)) {
                    e.printStackTrace();
                    fail("Position of exception are not matching expectations: expected " + sourcePosition.format() + ", found: " + e.getSourcePosition().format());
                }
            }
        }
    }

    private static Arguments[] getSnippets() {
        return new Arguments[]{
            ok("int[][] a[]={{{1,2},{3,4}}};float b=a[0][1][2];"),
            ok("int[] a={1,2,3};float b=a[2];"),
            ok("int[] a={1,2,3};int[3] b=a;"),
            ok("float[] a=float[3](1,2,3);vec2 b=vec2(a[0]);"),
            ok("float[] a=float[](1,2,3);vec2 b=vec2(a[0]);"),
            ok("void f(mat2 a){a[0][1]=1.0;}"),
            ok("void f(mat2 a){a[0]=vec2(1);}"),
            ok("int a=1;float b=a;"),
            ok("uniform vec2 a[2];void main(){float b=a[1].x;}"),
            ok("uniform vec2 a[2];void main(){vec3 b=a[1].xxx;}"),
            ok("int p(vec2 a){return int(a.x);}void main(){float b=p(vec2(1,2));}"),
            ok("vec2 a=vec2(1.0),b=vec2(1.0,2),c=vec2(ivec2(1)),d=vec2(vec3(1));"),

            notOk("float a=1;int b=a;", 1, 16),
            notOk("uniform vec2 a[2];void main(){vec3 b=a[1].xyz;}", 1, 42),
            notOk("uniform vec2 a[2];void main(){vec3 b=a[1].yx;}", 1, 37),
        };
    }

    private static Arguments ok(String source) {
        return Arguments.of(source, null);
    }

    private static Arguments notOk(String source, int line, int column) {
        return Arguments.of(source, SourcePosition.create(line, column));
    }
}

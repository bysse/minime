package com.tazadum.glsl.optimizer;

import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.type.PredefinedType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static com.tazadum.glsl.optimizer.RefCheck.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Erik on 2016-10-20.
 */
public class DeclarationSqueezeTest extends BaseOptimizerTest {
    @Override
    protected OptimizerType[] getOptimizerTypes() {
        return new OptimizerType[]{OptimizerType.DeclarationSqueezeType};
    }

    @BeforeEach
    void setup() {
        testInit(3);
    }

    @ParameterizedTest(name = "optimize: {1}")
    @DisplayName("Optimizations that should work")
    @MethodSource("getPositiveCases")
    void testOptimizerPositive(String expected, String source, List<RefCheck> checks) {
        Node node = optimize(source);
        assertEquals(expected, toString(node));

        RefCheck.runChecks(checks, parserContext);
    }

    @ParameterizedTest(name = "optimize: {0}")
    @DisplayName("Optimizations that should not work")
    @MethodSource("getNegativeCases")
    void testOptimizerNegative(String source) {
        Node node = optimize(source);
        assertEquals(source, toString(node));
    }

    private static Arguments[] getPositiveCases() {
        return new Arguments[]{
                Arguments.of(
                        "vec3 a,b,c;", "vec3 a;vec3 b;vec3 c;",
                        list(type(PredefinedType.VEC3, 3))
                ),
                Arguments.of(
                        "float a,b=2,c;", "float a;float b=2;float c;",
                        list(type(PredefinedType.FLOAT, 3))
                ),
                Arguments.of(
                        "int a,b,c=a;float x=a;", "int a;int b;float x=a;int c=a;",
                        list(
                                variable("a", 2),
                                type(PredefinedType.INT, 3),
                                type(PredefinedType.FLOAT, 1)
                        )
                ),
                Arguments.of(
                        "float a,b=2,c=b;", "float a;float b=2;float c=b;",
                        list(
                                variable("b", 1),
                                type(PredefinedType.FLOAT, 3)
                        )
                ),
                Arguments.of(
                        "int a,b;float x=a++;int c=a;", "int a;int b;float x=a++;int c=a;",
                        list(
                                variable("a", 2),
                                type(PredefinedType.INT, 3),
                                type(PredefinedType.FLOAT, 1)
                        )
                ),
                Arguments.of(
                        "uniform int a;void main(){int b,c=a;}", "uniform int a;void main(){int b;int c=a;}",
                        list(
                                variable("a", 1),
                                type(PredefinedType.INT, 3)
                        )
                ),
                Arguments.of(
                        "int a;void main(){int b,c=a;}", "int a;void main(){int b;int c=a;}",
                        list(
                                variable("a", 1),
                                type(PredefinedType.INT, 3)
                        )
                ),
                Arguments.of(
                        "void main(){vec2 a,c=2*a,d=vec2(c);}", "void main(){vec2 a;vec2 c=2*a;vec2 d=vec2(c);}",
                        list(
                                variable("a", 1),
                                variable("c", 1),
                                type(PredefinedType.VEC2, 3)
                        )
                ),
                Arguments.of(
                        "struct S{float a,b;};", "struct S{float a;float b;};",
                        noChecks()
                ),
                Arguments.of(
                        "float c;struct S{float a,b;};", "float c;struct S{float a;float b;};",
                        list(
                                type(PredefinedType.FLOAT, 1)
                        )
                ),
                Arguments.of(
                        "struct S{float a,b;};struct T{float c;};", "struct S{float a;float b;};struct T{float c;};",
                        noChecks()
                ),
                Arguments.of(
                        "uniform float x;void main(){vec2 a,c=vec2(x*a),d=vec2(c);}",
                        "uniform float x;void main(){vec2 a;vec2 c=vec2(x*a);vec2 d=vec2(c);}",
                        list(
                                variable("a", 1),
                                variable("c", 1),
                                variable("x", 1),
                                type(PredefinedType.VEC2, 3)
                        )
                ),
                Arguments.of( // x is modified in function call so c shouldn't be squeezed
                        "float x=0;float f(){return x++;}void main(){float a=0;vec2 b=vec2(f());float c=x;}",
                        "float x=0;float f(){return x++;}void main(){float a=0;vec2 b=vec2(f());float c=x;}",
                        list(
                                variable("a", 0),
                                variable("b", 0),
                                variable("x", 2),
                                type(PredefinedType.FLOAT, 3),
                                type(PredefinedType.VEC2, 1)
                        )
                ),
                Arguments.of( // f() is mutating x so c can't be rearranged without consequences
                        "float x=0;float f(){return x++;}void main(){float a=0;vec2 b=vec2(f());float c=f();}",
                        "float x=0;float f(){return x++;}void main(){float a=0;vec2 b=vec2(f());float c=f();}",
                        list(
                                variable("a", 0),
                                variable("b", 0),
                                variable("c", 0),
                                variable("x", 1),
                                type(PredefinedType.FLOAT, 3)
                        )
                ),
                Arguments.of( // g() is mutating x so c, which uses f(), can't be rearranged without consequences
                        "float x=0;float g(out float A){return A++;}float f(){return g(x);}void main(){float a=0;vec2 b=vec2(f());float c=f();}",
                        "float x=0;float g(out float A){return A++;}float f(){return g(x);}void main(){float a=0;vec2 b=vec2(f());float c=f();}",
                        list(
                                variable("a", 0),
                                variable("b", 0),
                                variable("c", 0),
                                variable("x", 1),
                                type(PredefinedType.FLOAT, 3)
                        )
                ),
                Arguments.of( // g() is mutating x so c, which uses x, can't be rearranged without consequences
                        "float x=0;float g(out float A){return A++;}float f(){return g(x);}void main(){float a=0;vec2 b=vec2(f());float c=x;}",
                        "float x=0;float g(out float A){return A++;}float f(){return g(x);}void main(){float a=0;vec2 b=vec2(f());float c=x;}",
                        list(
                                variable("a", 0),
                                variable("b", 0),
                                variable("c", 0),
                                variable("x", 2),
                                type(PredefinedType.FLOAT, 3)
                        )
                ),
                Arguments.of(
                        "uniform float x;float a=2;void main(){float b=x+a;}",
                        "uniform float x;float a=2;void main(){float b=x+a;}",
                        list(
                                variable("a", 1),
                                variable("x", 1),
                                type(PredefinedType.FLOAT, 3)
                        )
                ),
                Arguments.of(
                        "void main(){float a=0;vec3 b=vec3(a);a++;vec3 c=vec3(a),d=c+vec3(a);}",
                        "void main(){float a=0;vec3 b=vec3(a);a++;vec3 c=vec3(a);vec3 d=c+vec3(a);}",
                        list(
                                variable("a", 4),
                                variable("b", 0),
                                variable("c", 1),
                                variable("d", 0),
                                type(PredefinedType.FLOAT, 1)
                        )
                ),
                Arguments.of(
                        "void main(vec2 p){vec3 ro=4*normalize(vec3(cos(2.75-3*p.x),.7+(p.y+1),sin(2.75-3*p.x))),ta=vec3(0,1,0),ww=normalize(ta-ro),uu=normalize(cross(vec3(0,1,0),ww)),vv=normalize(cross(ww,uu)),rd=normalize(p.x*uu+p.y*vv+1.5*ww);}",
                        "void main(vec2 p){vec3 ro=4.0*normalize(vec3(cos(2.75-3*p.x),.7+(p.y+1),sin(2.75-3.0*p.x)));" +
                                "vec3 ta=vec3(0,1,0);" +
                                "vec3 ww = normalize( ta - ro);" +
                                "vec3 uu = normalize(cross( vec3(0.0,1.0,0.0), ww ));" +
                                "vec3 vv = normalize(cross(ww,uu));" +
                                "vec3 rd = normalize( p.x*uu + p.y*vv + 1.5*ww );" +
                                "}",
                        noChecks()

                )
        };
    }

    private static Arguments[] getNegativeCases() {
        return new Arguments[]{
                Arguments.of("uniform vec4 A;layout(binding=1) buffer P{vec4 B;};"),
                Arguments.of("layout(binding=1) buffer A{vec4 a;};layout(binding=2) buffer B{vec4 b;};"),

        };
    }
}

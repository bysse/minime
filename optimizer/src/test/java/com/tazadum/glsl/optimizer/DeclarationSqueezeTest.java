package com.tazadum.glsl.optimizer;

import com.tazadum.glsl.language.ast.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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
    void testOptimizerPositive(String expected, String source) {
        Node node = optimize(source);
        assertEquals(expected, toString(node));
    }

    private static Arguments[] getPositiveCases() {
        return new Arguments[]{
            Arguments.of("vec3 a,b,c;", "vec3 a;vec3 b;vec3 c;"),
            Arguments.of("float a,b=2,c;", "float a;float b=2;float c;"),
            Arguments.of("int a,b,c=a;float x=a;", "int a;int b;float x=a;int c=a;"),
            Arguments.of("float a,b=2,c=b;", "float a;float b=2;float c=b;"),
            Arguments.of("int a,b;float x=a++;int c=a;", "int a;int b;float x=a++;int c=a;"),
            Arguments.of("uniform int a;void main(){int b,c=a;}", "uniform int a;void main(){int b;int c=a;}"),
            Arguments.of("int a;void main(){int b,c=a;}", "int a;void main(){int b;int c=a;}"),
            Arguments.of("void main(){vec2 a,c=2*a,d=vec2(c);}", "void main(){vec2 a;vec2 c=2*a;vec2 d=vec2(c);}"),
            Arguments.of(
                "uniform float x;void main(){vec2 a,c=vec2(x*a),d=vec2(c);}",
                "uniform float x;void main(){vec2 a;vec2 c=vec2(x*a);vec2 d=vec2(c);}"),
            Arguments.of( // x is modified in function call so c shouldn't be squeezed
                "float x=0;float f(){return x++;}void main(){float a=0;vec2 b=vec2(f());float c=x;}",
                "float x=0;float f(){return x++;}void main(){float a=0;vec2 b=vec2(f());float c=x;}"),
            Arguments.of( // f() is mutating x so c can't be rearranged without consequences
                "float x=0;float f(){return x++;}void main(){float a=0;vec2 b=vec2(f());float c=f();}",
                "float x=0;float f(){return x++;}void main(){float a=0;vec2 b=vec2(f());float c=f();}"),
            Arguments.of( // g() is mutating x so c, which uses f(), can't be rearranged without consequences
                "float x=0;float g(out float a){return a++;}float f(){return g(x);}void main(){float a=0;vec2 b=vec2(f());float c=f();}",
                "float x=0;float g(out float a){return a++;}float f(){return g(x);}void main(){float a=0;vec2 b=vec2(f());float c=f();}"),
            Arguments.of( // g() is mutating x so c, which uses x, can't be rearranged without consequences
                "float x=0;float g(out float a){return a++;}float f(){return g(x);}void main(){float a=0;vec2 b=vec2(f());float c=x;}",
                "float x=0;float g(out float a){return a++;}float f(){return g(x);}void main(){float a=0;vec2 b=vec2(f());float c=x;}"),
            Arguments.of(
                "uniform float x;float a=2;void main(){float b=x+a;}",
                "uniform float x;float a=2;void main(){float b=x+a;}"),
            Arguments.of(
                "void main(){float a=0;vec3 b=vec3(a);a++;vec3 c=vec3(a),d=c+vec3(a);}",
                "void main(){float a=0;vec3 b=vec3(a);a++;vec3 c=vec3(a);vec3 d=c+vec3(a);}"),
            Arguments.of(
                "void main(vec2 p){vec3 ro=4*normalize(vec3(cos(2.75-3*p.x),.7+(p.y+1),sin(2.75-3*p.x))),ta=vec3(0,1,0),ww=normalize(ta-ro),uu=normalize(cross(vec3(0,1,0),ww)),vv=normalize(cross(ww,uu)),rd=normalize(p.x*uu+p.y*vv+1.5*ww);}",
                "void main(vec2 p){vec3 ro=4.0*normalize(vec3(cos(2.75-3*p.x),.7+(p.y+1),sin(2.75-3.0*p.x)));" +
                    "vec3 ta=vec3(0,1,0);" +
                    "vec3 ww = normalize( ta - ro);" +
                    "vec3 uu = normalize(cross( vec3(0.0,1.0,0.0), ww ));" +
                    "vec3 vv = normalize(cross(ww,uu));" +
                    "vec3 rd = normalize( p.x*uu + p.y*vv + 1.5*ww );}"

            )
        };
    }
}

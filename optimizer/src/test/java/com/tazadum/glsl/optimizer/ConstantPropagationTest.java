package com.tazadum.glsl.optimizer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Erik on 2016-10-20.
 */
public class ConstantPropagationTest extends BaseOptimizerTest {
    @Override
    protected OptimizerType[] getOptimizerTypes() {
        return new OptimizerType[] { OptimizerType.ConstantPropagationType };
    }

    @BeforeEach
    void setup() {
        testInit();
    }

    @ParameterizedTest(name = "optimize: {1}")
    @DisplayName("Optimizations that should work")
    @MethodSource("getPositiveCases")
    void testOptimizerPositive(String expected, String source) {
        assertEquals(expected, toString(optimize(source)));
    }

    private static Arguments[] getPositiveCases() {
        return new Arguments[]{
            Arguments.of("int main(){vec2 a=2*(vec2(1,1)-1).xy;return a.x+a.y;}", "vec2 m=vec2(1,1)-1;int main(){vec2 a=2*m.xy;return a.x+a.y;}"),
            Arguments.of("int f(){return 1;}", "const int a=1;int f(){return a;}"),
            Arguments.of("int f(){return 1;}", "int a=1;int f(){return a;}"),
            Arguments.of("int f(){return (1+2);}", "int a=(1+2);int f(){return a;}"),
            Arguments.of("vec3 f(){return vec3(0);}", "vec3 a=vec3(0);vec3 f(){return a;}"),
            Arguments.of("float f(){return 1+1;}", "float a=1;float f(){return a+a;}"),
            Arguments.of("int main(){int x=0;for(int i=0;i<5;i++)x+=i;return x;}", "int main(){int x=0;for(int i=0;i<5;i++)x+=i;return x;}"),
            Arguments.of("int a(){return 2;}int main(){int b=a(),x=0;for(int i=0;i<5;i++)x+=i*b;return x;}", "int a(){return 2;}int main(){int b=a(),x=0;for(int i=0;i<5;i++){x+=i*b;}return x;}"),
            Arguments.of("int a=1;int f(){a++;return a;}", "int a=1;int f(){a++;return a;}"),
            Arguments.of("int b=0;int g(){return b++;}int a=g();int f(){return a;}", "int b=0;int g(){return b++;}int a=g();int f(){return a;}"),
        };
    }

    ;
}

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
        return new OptimizerType[]{OptimizerType.ConstantPropagationType};
    }

    @BeforeEach
    void setup() {
        testInit();
    }

    @ParameterizedTest(name = "optimize: {1}")
    @DisplayName("Optimizations that should work")
    @MethodSource("getPositiveCases")
    void testOptimizerPositive(String expected, String source, int aCount) {
        assertEquals(expected, toString(optimize(source)));

        RefCheck.variable("a", aCount).runCheck(parserContext);
    }

    private static Arguments[] getPositiveCases() {
        return new Arguments[]{
                Arguments.of(
                        "int a=1;int f(){a++;return a;}",
                        "int a=1;int f(){a++;return a;}",
                        2
                ),
                Arguments.of(
                        "int b=0;int g(){return b++;}int a=g();int f(){return a;}",
                        "int b=0;int g(){return b++;}int a=g();int f(){return a;}",
                        1
                ),
                Arguments.of(
                        "int main(){vec2 a=2*(vec2(1,1)-1).xy;return a.x+a.y;}",
                        "vec2 m=vec2(1,1)-1;int main(){vec2 a=2*m.xy;return a.x+a.y;}",
                        2
                ),
                Arguments.of(
                        "int f(){return 1;}",
                        "const int a=1;int f(){return a;}",
                        0
                ),
                Arguments.of(
                        "int f(){return 1;}",
                        "int a=1;int f(){return a;}",
                        0
                ),
                Arguments.of(
                        "int f(){return (1+2);}",
                        "int a=(1+2);int f(){return a;}",
                        0
                ),
                Arguments.of(
                        "vec3 f(){return vec3(0);}",
                        "vec3 a=vec3(0);vec3 f(){return a;}",
                        0
                ),
                Arguments.of(
                        "float f(){return 1+1;}",
                        "float a=1;float f(){return a+a;}",
                        0
                ),
                Arguments.of(
                        "int main(){int a=0;for(int i=0;i<5;i++)a+=i;return a;}",
                        "int main(){int a=0;for(int i=0;i<5;i++)a+=i;return a;}",
                        2
                ),
                Arguments.of(
                        "int a(){return 2;}int main(){int b=a(),a=0;for(int i=0;i<5;i++)a+=i*b;return a;}",
                        "int a(){return 2;}int main(){int b=a(),a=0;for(int i=0;i<5;i++){a+=i*b;}return a;}",
                        2
                ),
        };
    }
}

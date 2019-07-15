package com.tazadum.glsl.cli;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Created by erikb on 2018-10-29.
 */
class FragmentShaderTest {
    @ParameterizedTest(name = "case: {0}")
    @DisplayName("Shader Optimization")
    @MethodSource("getShaders")
    void testShaders(String shader) {
        OptimizerMain.noExit = true;
        OptimizerMain.main(new String[]{"-type", "st", "-o", "output.min.glsl", "src/test/resources/shaders/" + shader});
    }

    private static Arguments[] getShaders() {
        return new Arguments[]{
                Arguments.of("simple.glsl"),
                Arguments.of("simple_macro.glsl"),
                Arguments.of("basic_dof.glsl"),
                Arguments.of("basic_raymarch.glsl"),
                Arguments.of("drop.glsl"),
                Arguments.of("falling.glsl"),
                Arguments.of("iq-clouds.glsl"),
                Arguments.of("starstruck.glsl")
        };
    }
}

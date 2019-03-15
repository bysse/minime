package com.tazadum.glsl.cli;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Created by erikb on 2018-10-29.
 */
class ComputeShaderTest {
    @ParameterizedTest(name = "case: {0}")
    @DisplayName("Shader Optimization")
    @MethodSource("getShaders")
    void testShaders(String shader) {
        OptimizerMain.noExit = true;
        OptimizerMain.main(new String[]{"-type", "c", "-o", "output.min.glsl", "src/test/resources/shaders/" + shader});
    }

    private static Arguments[] getShaders() {
        return new Arguments[]{
            Arguments.of("compute.glsl"),
        };
    }
}

package com.tazadum.glsl.cli;

import org.junit.jupiter.api.Test;

/**
 * Created by erikb on 2018-10-29.
 */
class OptimizerMainTest {
    @Test
    void test() {
        OptimizerMain.main(new String[]{"-format", "shadertoy", "-type", "st", "-vv", "-o", "output.min.glsl", "src/test/resources/shaders/iq-clouds.glsl"});
    }
}
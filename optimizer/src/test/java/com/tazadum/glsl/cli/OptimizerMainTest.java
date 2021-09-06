package com.tazadum.glsl.cli;

import org.junit.jupiter.api.Test;

/**
 * Created by erikb on 2018-10-29.
 */
class OptimizerMainTest {
    @Test
    void test() {
        OptimizerMain.main(new String[]{
                "-vv", "-format", "c",
                "-type", "f",
                "-o", "output.h",
                "src/test/resources/shaders/first.glsl",
                "src/test/resources/shaders/second.glsl"
        });
    }
}

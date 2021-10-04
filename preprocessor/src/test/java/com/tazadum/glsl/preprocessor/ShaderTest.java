package com.tazadum.glsl.preprocessor;

import com.tazadum.glsl.preprocessor.language.GLSLVersion;
import com.tazadum.glsl.test.TestData;
import com.tazadum.glsl.util.io.FileSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.stream.Stream;

/**
 * Created by erikb on 2018-09-19.
 */
class ShaderTest {
    private DefaultPreprocessor preprocessor;

    @BeforeEach
    void setUp() {
        preprocessor = new DefaultPreprocessor(GLSLVersion.OpenGL46, Collections.emptyList()    );
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("getSourceFiles")
    void test(Path shader) throws IOException {
        preprocessor.process(new FileSource(shader)).getSource();
    }

    private static Path[] getSourceFiles() {
        return new Path[]{
               Path.of("src/test/resources/shaders/camtest.glsl")
        };
    }

    private static Stream<TestData.Data> getTestData() throws IOException {
        return TestData.read(Paths.get("src/test/resources/cases"), "directory");
    }
}

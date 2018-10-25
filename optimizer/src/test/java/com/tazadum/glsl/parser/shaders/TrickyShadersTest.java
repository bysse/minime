package com.tazadum.glsl.parser.shaders;

import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

public class TrickyShadersTest {
    public static final String BASE_DIR = "src/test/resources";
    private ParserContext parserContext;

    @BeforeEach
    public void setUp() throws Exception {
        parserContext = TestUtils.parserContext();
    }

    @Test
    public void test_basic_dof() throws Exception {
        String shader = TestUtils.loadFile(Paths.get(BASE_DIR, "shaders/basic_dof.glsl").toFile());
        final Node node = TestUtils.parse(parserContext, Node.class, shader);
    }
}

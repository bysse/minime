package com.tazadum.glsl.preprocessor.stage;

import com.tazadum.glsl.preprocessor.LogKeeper;
import com.tazadum.glsl.util.SourcePositionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Created by erikb on 2018-09-27.
 */
class ContinuationAndCommentStageTest {
    private ContinuationAndCommentStage stage;
    private LogKeeper logKeeper;
    private SourcePositionMapper mapper;

    @BeforeEach
    public void setup() {
        stage = new ContinuationAndCommentStage();
        logKeeper = new LogKeeper();
        mapper = new SourcePositionMapper();
    }

    @Test
    @DisplayName("Test line continuation")
    public void test() {
        StageResult result = stage.process(mapper, logKeeper, "line 1\\\nmore line 1\nline 2");

        assertNotNull(result);
        assertEquals("line 1more line 1\nline 2\n", result.getSource());

        final Stream<String> warnings = logKeeper.getWarnings();
        assertEquals(0, warnings.count());
    }

    @Test
    @DisplayName("Test that warnings are emitted")
    public void test_warning() {
        StageResult result = stage.process(mapper, logKeeper, "line 1\\ \nmore line 1");

        assertNotNull(result);
        assertEquals("line 1more line 1\n", result.getSource());

        final Stream<String> warnings = logKeeper.getWarnings();
        assertEquals(1, warnings.count());
    }


    @Test
    @DisplayName("Test single line comments")
    public void test_single_comment_1() {
        StageResult result = stage.process(mapper, logKeeper, "//123\ncd");
        assertEquals("     \ncd\n", result.getSource());

        result = stage.process(mapper, logKeeper, "ab//123\ncd");
        assertEquals("ab     \ncd\n", result.getSource());

        result = stage.process(mapper, logKeeper, "ab\ncd // 123");
        assertEquals("ab\ncd       \n", result.getSource());

        result = stage.process(mapper, logKeeper, "ab\n\"//\"123");
        assertEquals("ab\n\"//\"123\n", result.getSource());

        result = stage.process(mapper, logKeeper, "a/ /");
        assertEquals("a/ /\n", result.getSource());
    }

    @Test
    @DisplayName("Test multi line comments")
    public void test_multi_comment_1() {
        StageResult result = stage.process(mapper, logKeeper, "a/*b*/c");
        assertEquals("a     c\n", result.getSource());

        result = stage.process(mapper, logKeeper, "a/*b*/");
        assertEquals("a     \n", result.getSource());

        result = stage.process(mapper, logKeeper, "a/*b\nc*/d");
        assertEquals("a   \n   d\n", result.getSource());

        result = stage.process(mapper, logKeeper, "a/*b\nc\nd\ne*/f");
        assertEquals("a   \n \n \n   f\n", result.getSource());

        result = stage.process(mapper, logKeeper, "a\"/*\"b*/");
        assertEquals("a\"/*\"b*/\n", result.getSource());
    }

    @Test
    @DisplayName("Nested comments and strings")
    public void test_multi_comment_2() {
        StageResult result = stage.process(mapper, logKeeper, "a/*b\"*/\"c");
        assertEquals("a      \"c\n", result.getSource());
    }
}

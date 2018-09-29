package com.tazadum.glsl.preprocessor.stage;

import com.tazadum.glsl.preprocessor.LogKeeper;
import com.tazadum.glsl.util.io.StringSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Created by erikb on 2018-09-27.
 */
class CommentStageTest {
    private CommentStage stage;
    private LogKeeper logKeeper;

    @BeforeEach
    public void setup() {
        logKeeper = new LogKeeper();
    }

    @Test
    @DisplayName("Test end of line comments")
    public void test_eol_comment() throws IOException {
        Stage stage = process("//123");
        assertEquals("     ", stage.readLine());
        assertNull(stage.readLine());

        stage = process("ab//123");
        assertEquals("ab     ", stage.readLine());
        assertNull(stage.readLine());

        stage = process("ab // 123");
        assertEquals("ab       ", stage.readLine());
        assertNull(stage.readLine());

        stage = process("ab\n\"//\"123");
        assertEquals("ab", stage.readLine());
        assertEquals("\"//\"123", stage.readLine());
        assertNull(stage.readLine());

        stage = process("a/ /");
        assertEquals("a/ /", stage.readLine());
        assertNull(stage.readLine());
    }

    @Test
    @DisplayName("Test inline comments")
    public void test_inline_comment() throws IOException {
        Stage stage = process("a/*b*/c");
        assertEquals("a     c", stage.readLine());
        assertNull(stage.readLine());

        stage = process("a/*b\nc*/d");
        assertEquals("a   ", stage.readLine());
        assertEquals("   d", stage.readLine());
        assertNull(stage.readLine());

        stage = process("a/*b\nc\nd\ne*/f");
        assertEquals("a   ", stage.readLine());
        assertEquals(" ", stage.readLine());
        assertEquals(" ", stage.readLine());
        assertEquals("   f", stage.readLine());
        assertNull(stage.readLine());

        stage = process("a\"/*\"b*/");
        assertEquals("a\"/*\"b*/", stage.readLine());
        assertNull(stage.readLine());
    }

    private Stage process(String source) {
        logKeeper.reset();
        LineContinuationStage stage1 = new LineContinuationStage(new StringSource("test", source), logKeeper);
        return new CommentStage(stage1, logKeeper);
    }
}

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
class ContinuationAndCommentStageTest {
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
        assertEquals("     \n", stage.readLine());
        assertNull(stage.readLine());

        stage = process("ab//123");
        assertEquals("ab     \n", stage.readLine());
        assertNull(stage.readLine());

        stage = process("ab // 123");
        assertEquals("ab       \n", stage.readLine());
        assertNull(stage.readLine());

        stage = process("ab\n\"//\"123");
        assertEquals("ab\n\"//\"123\n", stage.readLine());
        assertNull(stage.readLine());

        stage = process("a/ /");
        assertEquals("a/ /\\n", stage.readLine());
        assertNull(stage.readLine());
    }

    @Test
    @DisplayName("Test inline comments")
    public void test_inline_comment() throws IOException {
        Stage stage = process("a/*b*/c");
        assertEquals("a     c\n", stage.readLine());
        assertNull(stage.readLine());

        stage = process("a/*b\nc*/d");
        assertEquals("a   \n", stage.readLine());
        assertEquals("   d\n", stage.readLine());
        assertNull(stage.readLine());

        stage = process("a/*b\nc\nd\ne*/f");
        assertEquals("a   \n", stage.readLine());
        assertEquals(" \n", stage.readLine());
        assertEquals(" \n", stage.readLine());
        assertEquals("   f\n", stage.readLine());
        assertNull(stage.readLine());

        stage = process("a\"/*\"b*/");
        assertEquals("a\"/*\"b*/\n", stage.readLine());
        assertNull(stage.readLine());
    }

    private Stage process(String source) {
        logKeeper.reset();
        LineContinuationStage stage1 = new LineContinuationStage(new StringSource("test", source), logKeeper);
        return new CommentStage(stage1, logKeeper);
    }
}

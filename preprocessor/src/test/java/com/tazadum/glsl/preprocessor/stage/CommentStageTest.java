package com.tazadum.glsl.preprocessor.stage;

import com.tazadum.glsl.preprocessor.LogKeeper;
import com.tazadum.glsl.util.io.StringSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Created by erikb on 2018-09-27.
 */
class CommentStageTest {
    @Test
    @DisplayName("Test end of line comments")
    void test_eol_comment() throws IOException {
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
    void test_inline_comment() throws IOException {
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

    @Test
    @DisplayName("Test line numberings")
    void testLineNumbers() throws IOException {
        Stage stage = process("A\n/*\n\n*/\nB");
        for (int i=0;i<5;i++) {
            assertEquals(i, stage.getLineNumber(), "Verifying line number " + i);
            System.out.println(stage.readLine());
        }
    }

    private Stage process(String source) {
        LineContinuationStage stage1 = new LineContinuationStage(new StringSource("test", source), new LogKeeper());
        return new CommentStage(stage1);
    }
}

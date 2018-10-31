package com.tazadum.glsl.preprocessor.stage;

import com.tazadum.glsl.preprocessor.LogKeeper;
import com.tazadum.glsl.util.io.StringSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Created by erikb on 2018-09-27.
 */
class LineContinuationStageTest {
    private LineContinuationStage stage;
    private LogKeeper logKeeper;

    @BeforeEach
    public void setup() {
        logKeeper = new LogKeeper();
    }

    @Test
    @DisplayName("Test null source")
    public void test_null() throws IOException {
        Stage stage = process(null);
        assertNull(stage.readLine());
    }

    @Test
    @DisplayName("Test empty source")
    public void test_empty() throws IOException {
        Stage stage = process("");
        assertEquals("", stage.readLine());
        assertNull(stage.readLine());
    }

    @Test
    @DisplayName("Test empty line")
    public void test_empty_line() throws IOException {
        Stage stage = process("A\n\nB");
        assertEquals("A", stage.readLine());
        assertEquals("", stage.readLine());
        assertEquals("B", stage.readLine());
        assertNull(stage.readLine());
    }

    @Test
    @DisplayName("Test line continuation")
    public void test_continuation() throws IOException {
        Stage stage = process("line 1\\\nmore line 1\nline 2");

        assertEquals("line 1more line 1", stage.readLine());
        assertEquals("line 2", stage.readLine());
        assertNull(stage.readLine());

        final List<String> warnings = logKeeper.getWarnings();
        assertEquals(0, warnings.size());
    }

    @Test
    @DisplayName("Test that warnings are emitted")
    public void test_continuation_warning() throws IOException {
        Stage stage = process("line 1\\ \nmore line 1");

        assertEquals("line 1more line 1", stage.readLine());
        assertNull(stage.readLine());

        final List<String> warnings = logKeeper.getWarnings();
        assertEquals(1, warnings.size());
    }

    private Stage process(String source) {
        logKeeper.reset();
        return new LineContinuationStage(new StringSource("test", source), logKeeper);
    }
}

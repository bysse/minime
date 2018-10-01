package com.tazadum.glsl.preprocessor.stage;

import com.tazadum.glsl.preprocessor.LogKeeper;
import com.tazadum.glsl.preprocessor.Message;
import com.tazadum.glsl.util.SourcePosition;
import com.tazadum.glsl.util.SourcePositionMapper;
import com.tazadum.glsl.util.StringUtils;
import com.tazadum.glsl.util.io.Source;

import java.io.IOException;

import static com.tazadum.glsl.util.StringUtils.cut;
import static com.tazadum.glsl.util.StringUtils.rtrim;

public class LineContinuationStage implements Stage {
    private final Source source;
    private final LogKeeper logKeeper;
    private final SourcePositionMapper mapper;
    private int lineNumber;

    public LineContinuationStage(Source source, LogKeeper logKeeper) {
        this.source = source;
        this.logKeeper = logKeeper;
        this.lineNumber = 0;

        this.mapper = new SourcePositionMapper();
        this.mapper.remap(source.getSourceId(), SourcePosition.TOP, SourcePosition.TOP);

    }

    @Override
    public String getSourceId() {
        return source.getSourceId();
    }

    @Override
    public int getLineNumber() {
        return lineNumber;
    }

    @Override
    public String readLine() throws IOException {
        String line = "";

        int startingRow = source.getLineNumber();

        // read a line honoring continuations and remap the line numbers
        for (; ; ) {
            String part = source.readLine();
            if (part == null) {
                return null;
            }

            int currentRow = source.getLineNumber();

            if (hasLineContinuation(part, logKeeper, startingRow)) {
                // remap the line number
                mapper.remap(
                    source.getSourceId(),
                    SourcePosition.create(startingRow, line.length()),
                    SourcePosition.create(currentRow, 0)
                );

                // clean the line and append to the rest of the continuation
                line += cut(rtrim(part), -1);
                continue;
            }

            return line + part;
        }
    }

    @Override
    public SourcePositionMapper getMapper() {
        return mapper;
    }

    private boolean hasLineContinuation(String line, LogKeeper logKeeper, int lineNumber) {
        if (line.isEmpty()) {
            return false;
        }
        int index = line.length() - 1;
        while (index >= 0 && StringUtils.isWhitespace(line.charAt(index))) {
            index--;
        }

        if (line.charAt(index) != '\\') {
            return false;
        }

        if (index != line.length() - 1) {
            // trailing whitespaces after the line continuation can be a problem
            logKeeper.addWarning(lineNumber, 0, Message.Warning.LINE_CONTINUATION);
        }

        return true;
    }
}

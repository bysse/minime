package com.tazadum.glsl.preprocessor.stage;

import com.tazadum.glsl.preprocessor.LogKeeper;
import com.tazadum.glsl.preprocessor.Message;
import com.tazadum.glsl.util.LineTokenizer;
import com.tazadum.glsl.util.SourcePosition;
import com.tazadum.glsl.util.SourcePositionMapper;
import com.tazadum.glsl.util.StringUtils;

import static com.tazadum.glsl.util.StringUtils.cut;
import static com.tazadum.glsl.util.StringUtils.rtrim;

public class ContinuationAndCommentStage implements Stage {
    @Override
    public StageResult process(SourcePositionMapper mapper, LogKeeper logKeeper, String source) {
        boolean inComment = false;

        final StringBuilder builder = new StringBuilder();
        final LineTokenizer tokenizer = new LineTokenizer(source);

        while (tokenizer.hasNext()) {
            int startingRow = tokenizer.getLineNumber();
            String line = "";
            boolean hasContinuation = false;

            do {
                int currentRow = tokenizer.getLineNumber();
                String part = tokenizer.getLine();

                if (hasLineContinuation(part, logKeeper, startingRow)) {
                    // remap the line number
                    SourcePosition from = SourcePosition.create(startingRow, line.length());
                    SourcePosition to = SourcePosition.create(currentRow, 0);
                    mapper.remap(from, to);

                    // clean the line and append to the rest of the continuation
                    line += cut(rtrim(part), -1);
                    continue;
                }

                line += part;
                break;

            } while (true);


            if (inComment) {
                // TODO

                continue;
            }

            System.out.println(line);
        }

        return null;
    }

    private boolean hasLineContinuation(String line, LogKeeper logKeeper, int lineNumber) {
        int index = line.length() - 1;
        while (index >= 0 && StringUtils.isWhitespace(line.charAt(index))) {
            index--;
        }

        if (line.charAt(index) != '\\') {
            return false;
        }

        if (index == line.length() - 1) {
            // trailing whitespaces after the line continuation can be a problem
            logKeeper.addWarning(lineNumber, 0, Message.Warning.LINE_CONTINUATION);
        }

        return true;
    }
}

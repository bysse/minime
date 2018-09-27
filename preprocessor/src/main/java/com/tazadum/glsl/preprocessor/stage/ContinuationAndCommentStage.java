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
    public StageResult process(SourcePositionMapper sourceMapper, LogKeeper logKeeper, String source) {
        boolean inComment = false;

        final StringBuilder builder = new StringBuilder();
        final LineTokenizer tokenizer = new LineTokenizer(source);
        final SourcePositionMapper mapper = new SourcePositionMapper(sourceMapper);

        while (tokenizer.hasNext()) {
            int startingRow = tokenizer.getLineNumber();
            String line = "";

            // read a line honoring continuations and remap the line numbers
            for (; ; ) {
                String part = tokenizer.getLine();
                int currentRow = tokenizer.getLineNumber();

                if (hasLineContinuation(part, logKeeper, startingRow)) {
                    // remap the line number
                    mapper.remap(
                        SourcePosition.create(startingRow, line.length()),
                        SourcePosition.create(currentRow, 0)
                    );

                    // clean the line and append to the rest of the continuation
                    line += cut(rtrim(part), -1);
                    continue;
                }

                line += part;
                break;
            }


            if (inComment) {
                // replace all characters with whitespace
                for (int i = 0; i < line.length(); i++) {
                    builder.append(' ');
                }
                builder.append('\n');
                continue;
            } else {
                int index = indexOfLineComment(line);
                if (index >= 0) {
                    builder.append(line.substring(0, index));
                    for (int i = index; i < line.length(); i++) {
                        builder.append(' ');
                    }
                    builder.append('\n');
                    continue;
                }
            }

            builder.append(line).append('\n');
        }

        return new StageResult(builder.toString(), mapper);
    }

    private boolean hasLineContinuation(String line, LogKeeper logKeeper, int lineNumber) {
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

    private int indexOfLineComment(String line) {
        boolean string = false;
        int i = 0, comment = 0;
        for (char ch : line.toCharArray()) {
            switch (ch) {
                default:
                case '\\':
                    comment = 0;
                    break;
                case '\'':
                case '"':
                    string = !string;
                    comment = 0;
                    break;
                case '/':
                    if (!string) {
                        comment++;
                    }
                    break;
            }

            if (comment == 2) {
                return i - 1;
            }
            i++;
        }
        return -1;
    }
}

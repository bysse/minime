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
    private static final char SPACE = ' ';
    private static final char SLASH = '/';
    private static final char STAR = '*';

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

            boolean inString = false;
            char previous = '\n';
            for (int i = 0; i < line.length(); i++) {
                char ch = line.charAt(i);

                if (!inComment && previous != '\\' && (ch == '"' || ch == '\'')) {
                    inString = !inString;
                    builder.append(ch);
                    previous = ch;
                    continue;
                }

                if (inString) {
                    builder.append(ch);
                    previous = ch;
                    continue;
                }

                if (inComment) {
                    builder.append(SPACE);
                    if (previous == STAR && ch == SLASH) {
                        inComment = false;
                        previous = 0; // don't let other clauses act on this slash
                    } else {
                        previous = ch;
                    }
                    continue;
                } else {
                    if (previous == SLASH) {
                        if (ch == STAR) {
                            builder.append(SPACE).append(SPACE);
                            inComment = true;
                            previous = ch;
                            continue;
                        } else if (ch == SLASH) {
                            previous = 0;
                            appendWhitespace(builder, line.length() - i + 1);
                            break;
                        } else {
                            builder.append(SLASH);
                        }
                    }

                    if (ch == SLASH) {
                        // don't append slashes, we need to look ahead first
                        previous = ch;
                        continue;
                    }
                }

                builder.append(ch);
                previous = ch;
            }

            if (previous == SLASH) {
                builder.append(SLASH);
            }

            builder.append('\n');
        }

        return new StageResult(builder.toString(), mapper);
    }

    private void appendWhitespace(StringBuilder builder, int whitespaces) {
        for (int i = 0; i < whitespaces; i++) {
            builder.append(' ');
        }
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
}

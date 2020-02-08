package com.tazadum.glsl.preprocessor.stage;

import com.tazadum.glsl.util.SourcePosition;
import com.tazadum.glsl.util.SourcePositionId;
import com.tazadum.glsl.util.SourcePositionMapper;
import com.tazadum.glsl.util.io.Source;

import java.io.IOException;

public class CommentStage implements Stage {
    private static final char SPACE = ' ';
    private static final char SLASH = '/';
    private static final char STAR = '*';

    private final Source source;
    private final SourcePositionMapper mapper;

    private int lineNumber;
    private boolean inComment = false;

    public CommentStage(Stage stage) {
        this.source = stage;
        this.lineNumber = 0;
        this.mapper = new SourcePositionMapper(stage.getMapper());
        this.mapper.remap(SourcePosition.TOP, SourcePositionId.create(source.getSourceId(), SourcePosition.TOP));
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
        final String line = source.readLine();
        if (line == null) {
            return null;
        }

        final StringBuilder builder = new StringBuilder();
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

        // since comments are just replaced with whitespace we only need to increase the line number
        // and not care about the mapping at all
        lineNumber++;
        return builder.toString();
    }

    @Override
    public Source resolve(String filePath) throws IOException {
        return source.resolve(filePath);
    }

    private void appendWhitespace(StringBuilder builder, int whitespaces) {
        for (int i = 0; i < whitespaces; i++) {
            builder.append(' ');
        }
    }

    @Override
    public SourcePositionMapper getMapper() {
        return mapper;
    }
}

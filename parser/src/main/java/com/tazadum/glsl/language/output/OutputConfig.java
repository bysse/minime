package com.tazadum.glsl.language.output;

import com.tazadum.glsl.language.ast.Identifier;

import java.nio.CharBuffer;
import java.util.Set;

/**
 * Created by erikb on 2018-10-15.
 */
public class OutputConfig {
    private boolean renderNewLine;
    private String indentation;
    private Set<String> keywordBlacklist;
    private IdentifierOutputMode identifierMode;

    OutputConfig(boolean renderNewLine,
                 int indentation,
                 Set<String> keywordBlacklist,
                 IdentifierOutputMode identifierMode
    ) {
        this.renderNewLine = renderNewLine;
        this.indentation = CharBuffer.allocate(indentation).toString().replace('\0', ' ');
        this.keywordBlacklist = keywordBlacklist;
        this.identifierMode = identifierMode;
    }

    public void appendNewLine(StringBuilder builder) {
        if (renderNewLine) {
            builder.append('\n');
        }
    }

    public void appendIndentation(StringBuilder builder) {
        builder.append(indentation);
    }

    public void appendIdentifier(StringBuilder builder, Identifier identifier) {
        builder.append(identifierMode.apply(identifier));
    }

    public void appendKeyword(StringBuilder builder, String keyword) {
        if (!keywordBlacklist.contains(keyword)) {
            builder.append(keyword);
        }
    }

    public void appendSpace(StringBuilder builder) {
        if (!lastCharIs(builder, ' ')) {
            builder.append(' ');
        }
    }

    public void appendSemicolon(StringBuilder builder) {
        if (!lastCharIs(builder, ';')) {
            builder.append(';');
        }
    }

    private boolean lastCharIs(StringBuilder builder, char ch) {
        return builder.length() > 0 && builder.charAt(builder.length() - 1) == ch;
    }
}

package com.tazadum.glsl.language.output;

import com.tazadum.glsl.language.ast.Identifier;
import com.tazadum.glsl.language.type.Numeric;

import java.nio.CharBuffer;
import java.util.Set;

/**
 * Created by erikb on 2018-10-15.
 */
public class OutputConfig {
    private static final String EMPTY_STRING = "";

    private boolean renderNewLine;
    private String indentation;
    private Set<String> keywordBlacklist;
    private IdentifierOutputMode identifierMode;
    private NumericFormatter formatter;

    OutputConfig(boolean renderNewLine,
                 int indentation,
                 Set<String> keywordBlacklist,
                 IdentifierOutputMode identifierMode,
                 NumericFormatter formatter
    ) {
        this.renderNewLine = renderNewLine;
        this.indentation = CharBuffer.allocate(indentation).toString().replace('\0', ' ');
        this.keywordBlacklist = keywordBlacklist;
        this.identifierMode = identifierMode;
        this.formatter = formatter;
    }

    public String newLine() {
        if (renderNewLine) {
            return "\n";
        }
        return EMPTY_STRING;
    }

    public void newLine(StringBuilder builder) {
        if (renderNewLine && !lastCharIs(builder, '\n')) {
            builder.append('\n');
        }
    }

    public String indentation() {
        return indentation;
    }

    public String identifier(Identifier identifier) {
        return identifierMode.apply(identifier);
    }

    public String identifierSpacing() {
        return identifierMode == IdentifierOutputMode.None ? "" : " ";
    }

    public void appendSemicolon(StringBuilder builder) {
        if (!lastCharIs(builder, ';')) {
            builder.append(';');
        }
    }

    public String renderNumeric(Numeric numeric) {
        return formatter.format(numeric);
    }

    public String keyword(String keyword) {
        if (keywordBlacklist.contains(keyword)) {
            return EMPTY_STRING;
        }
        return keyword;
    }

    private boolean lastCharIs(StringBuilder builder, char ch) {
        return builder.length() > 0 && builder.charAt(builder.length() - 1) == ch;
    }
}

package com.tazadum.glsl.language.output;

import com.tazadum.glsl.language.ast.Identifier;
import com.tazadum.glsl.language.type.Numeric;

import java.nio.CharBuffer;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by erikb on 2018-10-15.
 */
public class OutputConfig {
    private static final String EMPTY_STRING = "";

    private final boolean renderNewLine;
    private final boolean originalIdentifiers;
    private final String indentation;
    private final int indentationCount;
    private final IdentifierOutputMode identifierMode;
    private final NumericFormatter formatter;
    private final Set<String> keywordBlacklist;

    OutputConfig(boolean renderNewLine,
                 int indentation,
                 Set<String> keywordBlacklist,
                 IdentifierOutputMode identifierMode,
                 NumericFormatter formatter,
                 boolean originalIdentifiers
    ) {
        this.renderNewLine = renderNewLine;
        this.indentationCount = indentation;
        this.indentation = CharBuffer.allocate(indentation).toString().replace('\0', ' ');
        this.keywordBlacklist = keywordBlacklist;
        this.identifierMode = identifierMode;
        this.formatter = formatter;
        this.originalIdentifiers = originalIdentifiers;
    }

    public OutputConfigBuilder edit() {
        OutputConfigBuilder builder = new OutputConfigBuilder();
        builder.renderNewLines(renderNewLine);
        builder.indentation(indentationCount);
        builder.significantDecimals(formatter.getSignificantDigits());
        builder.shaderToy(formatter.isShaderToy());
        builder.identifierMode(identifierMode);
        builder.blacklistKeyword(new HashSet<>(keywordBlacklist));
        builder.showOriginalIdentifiers(originalIdentifiers);

        return builder;
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

    /**
     * Returns true if original identifiers should be added as comments in the output.
     */
    public boolean isOriginalIdentifiers() {
        return originalIdentifiers;
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

    public NumericFormatter getFormatter() {
        return formatter;
    }

    private boolean lastCharIs(StringBuilder builder, char ch) {
        return builder.length() > 0 && builder.charAt(builder.length() - 1) == ch;
    }
}

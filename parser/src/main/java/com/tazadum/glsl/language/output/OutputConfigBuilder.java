package com.tazadum.glsl.language.output;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by erikb on 2018-10-15.
 */
public class OutputConfigBuilder {
    private Set<String> keywordBlacklist = new HashSet<>();
    private boolean renderNewLines = false;
    private int indentation = 0;
    private int maxDecimals = 5;
    private IdentifierOutputMode identifierMode = IdentifierOutputMode.Original;
    private boolean originalIdentifiers = false;

    /**
     * Indicate if new lines should be rendered to the output.
     */
    public OutputConfigBuilder renderNewLines(boolean enabled) {
        this.renderNewLines = enabled;
        return this;
    }

    /**
     * Sets the mode for how identifiers are rendered.
     */
    public OutputConfigBuilder identifierMode(IdentifierOutputMode mode) {
        identifierMode = mode;
        return this;
    }

    /**
     * The number of spaces to use as indentation.
     */
    public OutputConfigBuilder indentation(int spaces) {
        this.indentation = spaces;
        return this;
    }

    /**
     * Blacklist a keyword. The keyword will not be rendered if added
     * to this list. Use with care.
     */
    public OutputConfigBuilder blacklistKeyword(String keyword) {
        this.keywordBlacklist.add(keyword);
        return this;
    }

    /**
     * Blacklist a set of keywords. The keyword will not be rendered if added
     * to this list. Use with care.
     */
    public OutputConfigBuilder blacklistKeyword(Collection<String> keyword) {
        this.keywordBlacklist.addAll(keyword);
        return this;
    }

    /**
     * The maximum number of significant decimals rendered.
     */
    public OutputConfigBuilder significantDecimals(int decimals) {
        this.maxDecimals = decimals;
        return this;
    }

    /**
     * Set to true if original identifiers should be added as comments in the output.
     */
    public OutputConfigBuilder originalIdentifiers(boolean originalIdentifiers) {
        this.originalIdentifiers = originalIdentifiers;
        return this;
    }

    public OutputConfig build() {
        return new OutputConfig(
            renderNewLines,
            indentation,
            keywordBlacklist,
            identifierMode,
            new NumericFormatter(maxDecimals),
            originalIdentifiers
        );
    }
}

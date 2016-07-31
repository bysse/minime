package com.tazadum.glsl.language;

import org.antlr.v4.runtime.tree.ParseTree;

/**
 * @author erikb
 * @since 2016-07-31
 */
public interface HasToken {
    String getToken();

    static <T extends HasToken> T match(ParseTree context, T[] values) {
        final String contextName = context.getText();
        for (T value : values) {
            if (contextName.equals(value.getToken())) {
                return value;
            }
        }
        return null;
    }
}

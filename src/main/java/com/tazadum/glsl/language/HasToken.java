package com.tazadum.glsl.language;

import org.antlr.v4.runtime.tree.ParseTree;

/**
 * @author erikb
 * @since 2016-07-31
 */
public interface HasToken {
    String token();

    static <T extends HasToken> T match(ParseTree context, T[] values) {
        if (context == null) {
            return null;
        }
        final String contextName = context.getText();
        for (T value : values) {
            if (contextName.equals(value.token())) {
                return value;
            }
        }
        return null;
    }
}

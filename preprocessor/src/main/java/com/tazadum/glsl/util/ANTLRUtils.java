package com.tazadum.glsl.util;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by erikb on 2018-09-18.
 */
public class ANTLRUtils {
    /**
     * Concatenates all child nodes into a string separated by the provided separator.
     * This function is not recursive.
     *
     * @param ctx       Context to stringify.
     * @param separator The separator between tokens.
     */
    public static String stringify(ParserRuleContext ctx, String separator) {
        int childCount = ctx.getChildCount();
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < childCount; i++) {
            if (i > 0) {
                builder.append(separator);
            }
            builder.append(ctx.getChild(i).getText());
        }
        return builder.toString();
    }
}

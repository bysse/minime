package com.tazadum.glsl.util;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ParseTree;

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

        Interval previous = ctx.getChild(0).getSourceInterval();
        for (int i = 0; i < childCount; i++) {
            ParseTree child = ctx.getChild(i);
            if (i > 0) {
                // get nicer formatting for the string
                Interval interval = child.getSourceInterval();
                if (!interval.adjacent(previous)) {
                    builder.append(separator);
                }
                previous = interval;
            }
            builder.append(child.getText());
        }
        return builder.toString();
    }
}

package com.tazadum.glsl.util;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by erikb on 2018-09-17.
 */
public class FormatUtil {
    public static String error(SourcePosition position, String format, Object... args) {
        return "ERROR:" + position.getLine() + "(" + position.getColumn() + "): " + String.format(format, args);
    }

    public static String error(int lineNumber, String format, Object... args) {
        return "ERROR:" + lineNumber + ": " + String.format(format, args);
    }

    public static String error(ParserRuleContext ctx, String format, Object... args) {
        int line = ctx.getStart().getLine();
        return error(line, format, args);
    }
}

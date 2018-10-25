package com.tazadum.glsl.cli.options;

import joptsimple.BuiltinHelpFormatter;
import joptsimple.internal.Strings;


/**
 * Created by erikb on 2018-10-24.
 */
public class Formatter extends BuiltinHelpFormatter {
    /**
     * Makes a formatter with a given overall row width and column separator width.
     *
     * @param desiredOverallWidth         how many characters wide to make the overall help display
     * @param desiredColumnSeparatorWidth how many characters wide to make the separation between option column and
     */
    public Formatter(int desiredOverallWidth, int desiredColumnSeparatorWidth) {
        super(desiredOverallWidth, desiredColumnSeparatorWidth);
    }

    protected String optionLeader(String option) {
        return "-";
    }

    protected void appendTypeIndicator(StringBuilder buffer, String typeIndicator, String description, char start, char end) {
        buffer.append(' ').append(start);
        if (!Strings.isNullOrEmpty(description)) {
            buffer.append(description);
        }
        buffer.append(end);
    }
}

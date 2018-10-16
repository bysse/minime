package com.tazadum.glsl.language.output;

import com.tazadum.glsl.language.type.Numeric;
import com.tazadum.glsl.language.type.PredefinedType;

import java.util.Locale;

/**
 * Created by erikb on 2018-10-15.
 */
public class NumericFormatter {
    private static final String ZERO = "0";

    private int maxDecimals;

    public NumericFormatter(int maxDecimals) {
        this.maxDecimals = maxDecimals;
    }

    public String format(Numeric numeric) {
        switch (numeric.getType()) {
            case FLOAT:
            case DOUBLE:
                return renderFloat(numeric);
            case INT:
            case UINT:
            default:
                return renderInteger(numeric);
        }
    }

    private String renderFloat(Numeric numeric) {
        if (!numeric.hasFraction()) {
            return renderInteger(numeric);
        }

        final int decimals = Math.min(numeric.getDecimals(), maxDecimals);
        final String format = String.format(Locale.US, "%%.%df", decimals);
        final double value = numeric.getValue();

        String number = String.format(Locale.US, format, value);
        if (number.startsWith("0")) {
            number = number.substring(1);
        } else if (number.startsWith("-0")) {
            number = "-" + number.substring(2);
        }

        while (number.endsWith("0")) {
            number = number.substring(0, number.length() - 1);
        }

        if (".".equals(number)) {
            return ZERO;
        }

        if (number.endsWith(".")) {
            number = number.substring(0, number.length() - 1);
            if (value >= 1000) {
                return exponentPositive1000(number);
            }
            if (value <= -1000) {
                return exponentNegative1000(number);
            }
        } else if (number.startsWith(".")) {
            if (0 < value && value < 0.001) {
                return exponentPositiveTiny(number);
            }
        } else if (number.startsWith("-.")) {
            if (-0.001 < value && value < 0) {
                return exponentNegativeTiny(number);
            }
        }

        return number;
    }

    private String renderInteger(Numeric numeric) {
        if (numeric.getValue() == 0) {
            return ZERO;
        }

        String typeSuffix = "";
        if (numeric.getType() == PredefinedType.UINT) {
            typeSuffix = "u";
        }

        final String number = String.format(Locale.US, "%d", (int) numeric.getValue());
        if (numeric.getValue() >= 1000) {
            return exponentPositive1000(number) + typeSuffix;
        }
        if (numeric.getValue() <= -1000) {
            return exponentNegative1000(number) + typeSuffix;
        }
        return number + typeSuffix;
    }

    /**
     * Check if the number is a clean exponent and if so format it as an exponent
     */
    private String exponentPositive1000(String number) {
        final int length = number.length();
        for (int i = 1; i < length; i++) {
            if (number.charAt(i) != '0') {
                return number;
            }
        }
        return number.charAt(0) + "e" + (length - 1);
    }

    /**
     * Check if the number is a clean exponent and if so format it as an exponent
     */
    private String exponentNegative1000(String number) {
        final int length = number.length();
        for (int i = 2; i < length; i++) {
            if (number.charAt(i) != '0') {
                return number;
            }
        }
        return "-" + number.charAt(1) + "e" + (length - 2);
    }

    /**
     * Check if the number is a clean exponent and if so format it as an exponent
     */
    private String exponentPositiveTiny(String number) {
        final int length = number.length();
        for (int i = 1; i < length - 1; i++) {
            if (number.charAt(i) != '0') {
                return number;
            }
        }
        return number.charAt(length - 1) + "e-" + (length - 1);
    }

    /**
     * Check if the number is a clean exponent and if so format it as an exponent
     */
    private String exponentNegativeTiny(String number) {
        final int length = number.length();
        for (int i = 2; i < length - 1; i++) {
            if (number.charAt(i) != '0') {
                return number;
            }
        }
        return "-" + number.charAt(length - 1) + "e-" + (length - 2);
    }
}

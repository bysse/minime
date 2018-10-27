package com.tazadum.glsl.language.output;

import com.tazadum.glsl.language.type.Numeric;
import com.tazadum.glsl.language.type.PredefinedType;

import java.math.BigDecimal;

/**
 * Created by erikb on 2018-10-15.
 */
public class NumericFormatter {
    private static final String ZERO = "0";

    private int significantDigits;

    public NumericFormatter(int significantDigits) {
        this.significantDigits = significantDigits;
    }

    public int getSignificantDigits() {
        return significantDigits;
    }

    public String format(Numeric numeric) {
        BigDecimal decimal = numeric.getValue();

        switch (numeric.getType()) {
            case FLOAT:
            case DOUBLE:
                return renderFloat(decimal, numeric.getType());
            case INT:
            case UINT:
            default:
                return renderInteger(decimal, numeric.getType());
        }
    }

    private String renderFloat(BigDecimal decimal, PredefinedType type) {
        if (decimal.scale() <= 0) {
            return renderInteger(decimal, PredefinedType.INT);
        }

        decimal = scale(decimal);
        String number = decimal.toPlainString();

        if (number.indexOf('.') < 0) {
            return renderInteger(decimal, PredefinedType.INT);
        }

        if (number.startsWith("0")) {
            number = number.substring(1);
        } else if (number.startsWith("-0")) {
            number = "-" + number.substring(2);
        }

        while (number.endsWith("0")) {
            number = number.substring(0, number.length() - 1);
        }

        if (number.length() == 0) {
            return ZERO;
        }

        if (".".equals(number)) {
            return ZERO;
        }

        final double value = decimal.doubleValue();
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

    private BigDecimal scale(BigDecimal value) {
        int digits = value.precision();
        if (digits <= significantDigits) {
            return value;
        }

        if (value.scale() == 0) {
            return value;
        }

        BigDecimal original = value;
        int escape = 20;
        while (escape-- > 0) {
            BigDecimal rescaled = value.setScale(value.scale() - 1, BigDecimal.ROUND_HALF_UP);
            if (rescaled.precision() != digits && rescaled.precision() >= significantDigits) {
                digits = rescaled.precision();
                value = rescaled;
                continue;
            }
            return value;
        }

        // value rendering escape
        return original;
    }

    private String renderInteger(BigDecimal decimal, PredefinedType type) {
        if (decimal.doubleValue() == 0.0) {
            return ZERO;
        }

        String typeSuffix = "";
        if (type == PredefinedType.UINT) {
            typeSuffix = "u";
        }

        int intValue = decimal.intValue();
        String number = decimal.setScale(0, BigDecimal.ROUND_HALF_UP).toPlainString();
        if (intValue >= 1000) {
            return exponentPositive1000(number) + typeSuffix;
        }
        if (intValue <= -1000) {
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

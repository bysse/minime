package com.tazadum.glsl.language.type;

import java.math.BigDecimal;
import java.util.Locale;

import static com.tazadum.glsl.language.type.PredefinedType.INT;
import static com.tazadum.glsl.language.type.PredefinedType.UINT;
import static com.tazadum.glsl.parser.TypeCombination.anyOf;

public class Numeric implements Comparable<Numeric> {
    private static final int MAX_PRECISION = 8;
    private BigDecimal value;
    private PredefinedType type;

    Numeric(BigDecimal value, PredefinedType type) {
        this.value = value.setScale(MAX_PRECISION, BigDecimal.ROUND_HALF_UP);
        this.type = type;

        assert type.category() == TypeCategory.Scalar : "Non scalar type passed to constructor";
    }

    public BigDecimal getValue() {
        return value;
    }

    /**
     * Returns the signum function of this Numeric.
     *
     * @return -1, 0, or 1 as the value of this Numeric is negative, zero, or positive.
     */
    public int signum() {
        return value.signum();
    }

    public int intValue() {
        return value.intValue();
    }

    public double doubleValue() {
        return value.doubleValue();
    }

    /**
     * Returns true if the value is positive otherwise false.
     */
    public boolean boolValue() {
        return signum() > 0;
    }

    public boolean isFloat() {
        return type == PredefinedType.FLOAT || type == PredefinedType.DOUBLE;
    }

    public PredefinedType getType() {
        return type;
    }

    /**
     * Takes the type into consideration when comparing numbers.
     */
    @Override
    public int compareTo(Numeric o) {
        if (isFloat()) {
            if (o.isFloat()) {
                // float VS float
                return value.compareTo(o.value);
            }
            // float VS int
            return Double.compare(o.doubleValue(), o.intValue());
        }
        int intValue = intValue();
        if (o.isFloat()) {
            // int VS float
            return Double.compare(intValue, o.doubleValue());
        }
        // int VS int
        return Integer.compare(intValue, o.intValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Numeric numeric = (Numeric) o;

        if (!value.equals(numeric.value)) return false;
        return type == numeric.type;
    }

    @Override
    public int hashCode() {
        int result = value.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }

    public String toString() {
        return String.format(Locale.US, "{%s, %s}", value.toPlainString(), type.token());
    }

    public static Numeric createFloat(double floatValue, PredefinedType type) {
        return new Numeric(new BigDecimal(floatValue), type);
    }

    public static Numeric createInt(int intValue, PredefinedType type) {
        if (anyOf(type, INT, UINT)) {
            return new Numeric(new BigDecimal(intValue), type);
        }
        throw new IllegalArgumentException("Type must be either INT or UINT : " + type.name());
    }

    public static Numeric create(String number, PredefinedType type) {
        return new Numeric(new BigDecimal(number), type);
    }

    public static Numeric create(String number) {
        int length = number.length();
        char last = number.charAt(--length);

        // check for type suffixes
        if (last == 'u' || last == 'U') {
            // no need for special float handling since this is an unsigned int
            String value = number.substring(0, length);
            return new Numeric(new BigDecimal(value), UINT);
        }

        PredefinedType type = INT;
        if (last == 'f' || last == 'F') {
            type = PredefinedType.FLOAT;
            char prev = number.charAt(length - 1);
            if (prev == 'l' || prev == 'L') {
                type = PredefinedType.DOUBLE;
                length--;
            }
            number = number.substring(0, length);
        }

        int index = number.indexOf('.');
        if (index < 0) {
            // no dot found so this value has no decimals
            return new Numeric(new BigDecimal(number), type);
        }

        if (type == INT) {
            type = PredefinedType.FLOAT;
        }

        return new Numeric(new BigDecimal(number), type);
    }
}

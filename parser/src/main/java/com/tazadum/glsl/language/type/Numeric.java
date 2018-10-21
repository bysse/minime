package com.tazadum.glsl.language.type;

import java.util.Locale;

public class Numeric implements Comparable<Numeric> {
    private double value;
    private int decimals;
    private PredefinedType type;

    public Numeric(double value, int decimals, PredefinedType type) {
        this.value = value;
        this.decimals = decimals;
        this.type = type;

        assert type.category() == TypeCategory.Scalar : "Non scalar type passed to constructor";
    }

    public boolean hasFraction() {
        return decimals > 0;
    }

    public int getDecimals() {
        return decimals;
    }

    public double getValue() {
        return value;
    }

    public boolean isFloat() {
        return type == PredefinedType.FLOAT || type == PredefinedType.DOUBLE;
    }

    public PredefinedType getType() {
        return type;
    }

    @Override
    public int compareTo(Numeric o) {
        return (int) Math.signum(value - o.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Numeric numeric = (Numeric) o;

        return Double.compare(numeric.value, value) == 0;
    }

    @Override
    public int hashCode() {
        long temp = Double.doubleToLongBits(value);
        return (int) (temp ^ (temp >>> 32));
    }

    public static Numeric create(String number) {
        int length = number.length();
        char last = number.charAt(--length);

        // check for type suffixes
        if (last == 'u' || last == 'U') {
            // no need for special float handling since this is an unsigned int
            double value = Double.parseDouble(number.substring(0, length));
            return new Numeric(value, 0, PredefinedType.UINT);
        }

        PredefinedType type = PredefinedType.INT;
        if (last == 'f' || last == 'F') {
            type = PredefinedType.FLOAT;
            char prev = number.charAt(length - 1);
            if (prev == 'l' || prev == 'L') {
                type = PredefinedType.DOUBLE;
                length--;
            }
            number = number.substring(0, length);
        }

        final double value = Double.parseDouble(number);

        int index = number.indexOf('.');
        if (index < 0) {
            // no dot found so this value has no decimals
            return new Numeric(value, 0, type);
        }

        if (type == PredefinedType.INT) {
            type = PredefinedType.FLOAT;
        }

        int decimals = number.length() - index - 1;

        // remove trailing zeroes
        for (int i = number.length() - 1; i > index; i--) {
            if (number.charAt(i) != '0') {
                break;
            }
            decimals--;
        }

        return new Numeric(value, decimals, type);
    }

    public String toString() {
        return String.format(Locale.US, "{%f, %d, %s}", value, decimals, type.token());
    }

    public static Numeric abs(Numeric n) {
        return new Numeric(Math.abs(n.getValue()), n.getDecimals(), n.getType());
    }
}

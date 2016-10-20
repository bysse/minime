package com.tazadum.glsl.language;

public class Numeric implements Comparable<Numeric> {
    private double value;
    private int decimals;
    private boolean isFloat;

    public Numeric(double value, int decimals, boolean isFloat) {
        this.value = value;
        this.decimals = decimals;
        this.isFloat = isFloat;
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
        return isFloat;
    }

    @Override
    public int compareTo(Numeric o) {
        return (int) Math.signum(value - o.value);
    }

    public static Numeric create(String number) {
        final double value = Double.parseDouble(number);

        int decimals = 0;
        int index = number.indexOf('.');

        if (index < 0) {
            return new Numeric(value, 0, false);
        }
        decimals = number.length() - index - 1;

        // remove trailing zeroes
        for (int i = number.length() - 1; i > index; i--) {
            if (number.charAt(i) != '0') {
                break;
            }
            decimals--;
        }

        return new Numeric(value, decimals, true);
    }
}

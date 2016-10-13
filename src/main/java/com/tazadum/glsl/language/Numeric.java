package com.tazadum.glsl.language;

public class Numeric implements Comparable<Numeric> {
    private double value;
    private int decimals;

    Numeric(double value, int decimals) {
        this.value = value;
        this.decimals = decimals;
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

    @Override
    public int compareTo(Numeric o) {
        return (int) Math.signum(value - o.value);
    }

    public static Numeric create(String number) {
        final double value = Double.parseDouble(number);

        int decimals = 0;
        int index = number.indexOf('.');

        if (index < 0) {
            return new Numeric(value, 0);
        }
        decimals = number.length() - index - 1;

        // remove trailing zeroes
        for (int i = number.length() - 1; i > index; i--) {
            if (number.charAt(i) != '0') {
                break;
            }
            decimals--;
        }

        return new Numeric(value, decimals);
    }
}

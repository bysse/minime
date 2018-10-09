package com.tazadum.glsl.language.type;

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

    public static Numeric abs(Numeric n) {
        return new Numeric(Math.abs(n.getValue()), n.getDecimals(), n.isFloat());
    }
}

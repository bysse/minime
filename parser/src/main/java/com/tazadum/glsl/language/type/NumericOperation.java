package com.tazadum.glsl.language.type;

import com.tazadum.glsl.exception.TypeException;
import com.tazadum.glsl.parser.TypeCombination;

import java.math.BigDecimal;

/**
 * Implementation of the basic numeric operations using Numerics.
 */
public class NumericOperation {
    public static Numeric add(Numeric a, Numeric b) throws TypeException {
        return new Numeric(a.getValue().add(b.getValue()), negotiateType(a, b));
    }

    public static Numeric sub(Numeric a, Numeric b) throws TypeException {
        return new Numeric(a.getValue().subtract(b.getValue()), negotiateType(a, b));
    }

    public static Numeric mul(Numeric a, Numeric b) throws TypeException {
        return new Numeric(a.getValue().multiply(b.getValue()), negotiateType(a, b));
    }

    public static Numeric div(Numeric a, Numeric b) throws TypeException {
        return new Numeric(a.getValue().divide(b.getValue(), BigDecimal.ROUND_HALF_UP), negotiateType(a, b));
    }

    public static Numeric mod(Numeric a, Numeric b) throws TypeException {
        return new Numeric(a.getValue().remainder(b.getValue()), negotiateType(a, b));
    }

    public static Numeric inc(Numeric a) {
        return new Numeric(a.getValue().add(BigDecimal.ONE), a.getType());
    }

    public static Numeric dec(Numeric a) {
        return new Numeric(a.getValue().subtract(BigDecimal.ONE), a.getType());
    }

    public static Numeric negate(Numeric a) {
        return new Numeric(a.getValue().negate(), a.getType());
    }

    private static PredefinedType negotiateType(Numeric a, Numeric b) throws TypeException {
        return (PredefinedType) TypeCombination.compatibleType(a.getType(), b.getType());
    }

}

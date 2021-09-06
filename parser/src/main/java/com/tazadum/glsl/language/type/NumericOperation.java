package com.tazadum.glsl.language.type;

import com.tazadum.glsl.exception.TypeException;
import com.tazadum.glsl.parser.TypeCombination;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Implementation of the basic operations using Numeric operands.
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
        int scaleA = a.getValue().scale();
        int scaleB = b.getValue().scale();

        BigDecimal abd = a.getValue();
        if (scaleA < scaleB) {
            abd = abd.setScale(scaleB, RoundingMode.HALF_UP);
        }

        PredefinedType type = negotiateType(a, b);
        if (type == PredefinedType.INT) {
            type = PredefinedType.FLOAT;
        }

        return new Numeric(abd.divide(b.getValue(), RoundingMode.HALF_UP), type);
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
        // float types wil force the expression to be float


        return (PredefinedType) TypeCombination.compatibleType(a.getType(), b.getType());
    }

}

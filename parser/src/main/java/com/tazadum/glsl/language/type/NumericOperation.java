package com.tazadum.glsl.language.type;

import com.tazadum.glsl.exception.TypeException;
import com.tazadum.glsl.parser.TypeCombination;

import static com.tazadum.glsl.language.type.PredefinedType.INT;
import static com.tazadum.glsl.language.type.PredefinedType.UINT;
import static com.tazadum.glsl.parser.TypeCombination.anyOf;

public class NumericOperation {
    public static Numeric add(Numeric a, Numeric b) throws TypeException {
        int decimals = Math.max(a.getDecimals(), b.getDecimals());
        return new Numeric(a.getValue() + b.getValue(), decimals, negotiateType(a, b));
    }

    public static Numeric sub(Numeric a, Numeric b) throws TypeException {
        int decimals = Math.max(a.getDecimals(), b.getDecimals());
        return new Numeric(a.getValue() - b.getValue(), decimals, negotiateType(a, b));
    }

    public static Numeric mul(Numeric a, Numeric b) throws TypeException {
        int decimals = Math.max(a.getDecimals(), b.getDecimals());
        return new Numeric(a.getValue() * b.getValue(), decimals, negotiateType(a, b));
    }

    public static Numeric div(Numeric a, Numeric b) throws TypeException {
        int decimals = Math.max(a.getDecimals(), b.getDecimals());
        PredefinedType type = negotiateType(a, b);

        if (anyOf(type, INT, UINT)) {
            return new Numeric((int) a.getValue() / (int) b.getValue(), decimals, type);
        }

        return new Numeric(a.getValue() / b.getValue(), decimals, type);
    }

    public static Numeric mod(Numeric a, Numeric b) throws TypeException {
        int decimals = Math.max(a.getDecimals(), b.getDecimals());
        PredefinedType type = negotiateType(a, b);

        if (anyOf(type, INT, UINT)) {
            return new Numeric((int) a.getValue() % (int) b.getValue(), decimals, type);
        }

        return new Numeric(a.getValue() % b.getValue(), decimals, type);
    }

    private static PredefinedType negotiateType(Numeric a, Numeric b) throws TypeException {
        return (PredefinedType) TypeCombination.compatibleType(a.getType(), b.getType());
    }
}

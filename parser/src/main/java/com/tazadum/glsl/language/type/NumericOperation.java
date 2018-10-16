package com.tazadum.glsl.language.type;

import com.tazadum.glsl.exception.Errors;
import com.tazadum.glsl.exception.TypeException;

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

        if (type == PredefinedType.INT || type == PredefinedType.UINT) {
            return new Numeric((int) a.getValue() / (int) b.getValue(), decimals, type);
        }

        return new Numeric(a.getValue() / b.getValue(), decimals, type);
    }

    public static Numeric mod(Numeric a, Numeric b) throws TypeException {
        int decimals = Math.max(a.getDecimals(), b.getDecimals());
        PredefinedType type = negotiateType(a, b);

        if (type == PredefinedType.INT || type == PredefinedType.UINT) {
            return new Numeric((int) a.getValue() % (int) b.getValue(), decimals, type);
        }

        return new Numeric(a.getValue() % b.getValue(), decimals, type);
    }

    public static PredefinedType negotiateType(Numeric a, Numeric b) throws TypeException {
        PredefinedType typeA = a.getType();
        PredefinedType typeB = b.getType();
        if (typeA == typeB || typeA.isAssignableBy(typeB)) {
            return typeA;
        }
        if (typeB.isAssignableBy(typeA)) {
            return typeB;
        }
        throw new TypeException(Errors.Type.INCOMPATIBLE_OP_TYPES(typeA, typeB));
    }
}

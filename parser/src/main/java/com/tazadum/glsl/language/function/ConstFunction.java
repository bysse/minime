package com.tazadum.glsl.language.function;

import com.tazadum.glsl.language.type.Numeric;
import com.tazadum.glsl.preprocessor.model.HasToken;

/**
 * An index over functions that produces constant expressions.
 * Created by erikb on 2018-10-17.
 */
public enum ConstFunction implements HasToken {
    RADIANS("radians"),
    DEGREES("degrees"),
    SIN("sin"),
    COS("cos"),
    ASIN("asin"),
    ACOS("acos"),
    POW("pow"),
    EXP("exp"),
    LOG("log"),
    EXP2("exp2"),
    LOG2("log2"),
    SQRT("sqrt"),
    INVERSESQRT("inversesqrt"),
    ABS("abs"),
    SIGN("sign"),
    FLOOR("floor"),
    TRUNC("trunc"),
    ROUND("round"),
    CEIL("ceil"),
    MOD("mod"),
    MIN("min"),
    MAX("max"),
    CLAMP("clamp"),
    LENGTH("length"),
    DOT("dot"),
    NORMALIZE("normalize"),;

    private final String token;

    ConstFunction(String token) {
        this.token = token;
    }

    public Numeric apply(Numeric n) {
        return new Numeric(apply(n.getValue()), n.getDecimals(), n.getType());
    }

    public double apply(double n) {
        switch (this) {
            case ABS:
                return Math.abs(n);
            case ACOS:
                return Math.acos(n);
            case ASIN:
                return Math.asin(n);
            case CEIL:
                return Math.ceil(n);
            case COS:
                return Math.cos(n);
            case DEGREES:
                return n * 180. / Math.PI;
            case EXP:
                return Math.exp(n);
            case EXP2:
                return Math.pow(2, n);
            case FLOOR:
                return Math.floor(n);
            case INVERSESQRT:
                return 1.0 / Math.sqrt(n);
            case LOG:
                return Math.log(n);
            case LOG2:
                return Math.log(n) / Math.log(2);
            case RADIANS:
                return n * Math.PI / 180.0;
            case ROUND:
                return Math.round(n);
            case SIGN:
                return Math.signum(n);
            case SIN:
                return Math.sin(n);
            case SQRT:
                return Math.sqrt(n);
            case TRUNC:
                return n > 0 ? Math.floor(n) : Math.ceil(n);
            default:
                throw new UnsupportedOperationException("Not supported");
        }
    }

    @Override
    public String token() {
        return token;
    }
}

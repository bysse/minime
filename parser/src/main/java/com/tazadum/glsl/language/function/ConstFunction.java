package com.tazadum.glsl.language.function;

import com.tazadum.glsl.language.HasToken;

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

    @Override
    public String token() {
        return token;
    }
}

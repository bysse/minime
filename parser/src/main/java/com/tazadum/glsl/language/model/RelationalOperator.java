package com.tazadum.glsl.language.model;

import com.tazadum.glsl.language.HasToken;
import com.tazadum.glsl.language.type.Numeric;
import com.tazadum.glsl.parser.GLSLParser;

import java.util.function.BiFunction;

public enum RelationalOperator implements HasToken {
    LessThan("<", GLSLParser.LEFT_ANGLE, (a, b) -> a < b),
    GreaterThan(">", GLSLParser.RIGHT_ANGLE, (a, b) -> a > b),
    LessThanOrEqual("<=", GLSLParser.LE_OP, (a, b) -> a <= b),
    GreaterThanOrEqual(">=", GLSLParser.GE_OP, (a, b) -> a >= b),
    Equal("==", GLSLParser.EQUAL, (a, b) -> Double.compare(a, b) == 0),
    NotEqual("!=", GLSLParser.NE_OP, (a, b) -> Double.compare(a, b) != 0),;

    private final String token;
    private final int tokenId;
    private final BiFunction<Double, Double, Boolean> function;

    RelationalOperator(String token, int tokenId, BiFunction<Double, Double, Boolean> function) {
        this.token = token;
        this.tokenId = tokenId;
        this.function = function;
    }

    @Override
    public String token() {
        return token;
    }

    @Override
    public int tokenId() {
        return tokenId;
    }

    public Boolean apply(Numeric a, Numeric b) {
        return function.apply(a.getValue(), b.getValue());
    }
}

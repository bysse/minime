package com.tazadum.glsl.language.output;

import com.tazadum.glsl.language.type.Numeric;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static com.tazadum.glsl.language.type.PredefinedType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by erikb on 2018-10-15.
 */
class NumericFormatterTest {
    private NumericFormatter formatter = new NumericFormatter(5);

    @ParameterizedTest(name = "test: {0}")
    @MethodSource("getCases")
    void testString(String expected, Numeric numeric) {
        assertEquals(expected, formatter.format(numeric));
    }

    private static Arguments[] getCases() {
        return new Arguments[]{
            Arguments.of("0", new Numeric(0, 2, FLOAT)),
            Arguments.of("0", new Numeric(0, 2, DOUBLE)),

            Arguments.of("3.14159", new Numeric(Math.PI, 10, FLOAT)),
            Arguments.of("10.2", new Numeric(10.15, 1, DOUBLE)),
            Arguments.of(".1", new Numeric(0.100, 3, DOUBLE)),
            Arguments.of("1e3", new Numeric(1000, 2, FLOAT)),
            Arguments.of(".00011", new Numeric(0.00011, 5, FLOAT)),
            Arguments.of("-4e3", new Numeric(-4000, 2, DOUBLE)),
            Arguments.of("1e-4", new Numeric(0.0001, 4, FLOAT)),
            Arguments.of("-.1", new Numeric(-0.1, 4, FLOAT)),
            Arguments.of("-1e-4", new Numeric(-0.0001, 4, FLOAT)),
            Arguments.of("-.00011", new Numeric(-0.00011, 5, FLOAT)),

            Arguments.of("0", new Numeric(0, 0, UINT)),
            Arguments.of("0", new Numeric(0, 0, INT)),
            Arguments.of("10", new Numeric(10, 0, UINT)),
            Arguments.of("100", new Numeric(100, 0, INT)),
            Arguments.of("1e3", new Numeric(1000, 0, UINT)),
            Arguments.of("4e3", new Numeric(4000, 0, INT)),
            Arguments.of("-2", new Numeric(-2, 0, INT)),
            Arguments.of("-4e3", new Numeric(-4000, 0, INT)),
        };
    }
}

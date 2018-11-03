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
    private NumericFormatter formatter = new NumericFormatter(3, false);
    private NumericFormatter shaderToy = new NumericFormatter(3, true);

    @ParameterizedTest(name = "test: {0}")
    @MethodSource("getCases")
    void testString(String expectedNormal, String expectedShaderToy, Numeric numeric) {
        assertEquals(expectedNormal, formatter.format(numeric), "ordinary formatting");
        assertEquals(expectedShaderToy, shaderToy.format(numeric), "shadertoy formatting");
    }

    private static Arguments[] getCases() {
        return new Arguments[]{
            Arguments.of("0", "0.", Numeric.create("0", FLOAT)),
            Arguments.of("0", "0.", Numeric.create("0", DOUBLE)),
            Arguments.of("3", "3.", Numeric.create("3.0", FLOAT)),

            Arguments.of("3", "3", Numeric.create("3.1", INT)),
            Arguments.of("3.1", "3.1", Numeric.create("3.1", FLOAT)),
            Arguments.of("3.14", "3.14", Numeric.create("3.1415926535", FLOAT)),
            Arguments.of("10.2", "10.2", Numeric.create("10.15", DOUBLE)),
            Arguments.of(".1", ".1", Numeric.create("0.100", DOUBLE)),
            Arguments.of("1e3", "1e3", Numeric.create("1000", FLOAT)),
            Arguments.of(".00011", ".00011", Numeric.create("0.00011", FLOAT)),
            Arguments.of(".000123", ".000123", Numeric.create("0.0001234", FLOAT)),
            Arguments.of("-4e3", "-4e3", Numeric.create("-4000", DOUBLE)),
            Arguments.of("1e-4", "1e-4", Numeric.create("0.0001", FLOAT)),
            Arguments.of("-.1", "-.1", Numeric.create("-0.1", FLOAT)),
            Arguments.of("-1e-4", "-1e-4", Numeric.create("-0.0001", FLOAT)),
            Arguments.of("-.00011", "-.00011", Numeric.create("-0.00011", FLOAT)),

            Arguments.of("0", "0", Numeric.create("0", UINT)),
            Arguments.of("0", "0", Numeric.create("0", INT)),
            Arguments.of("10", "10", Numeric.create("10", INT)),
            Arguments.of("10u", "10u", Numeric.create("10", UINT)),
            Arguments.of("100", "100", Numeric.create("100", INT)),
            Arguments.of("1e3u", "1e3u", Numeric.create("1000", UINT)),
            Arguments.of("-4e3", "-4e3", Numeric.create("-4000", INT)),
            Arguments.of("4e3", "4e3", Numeric.create("4000", INT)),
            Arguments.of("4100", "4100", Numeric.create("4100", INT)),
            Arguments.of("-2", "-2", Numeric.create("-2", INT)),
            Arguments.of("-4100", "-4100", Numeric.create("-4100", INT)),
        };
    }
}

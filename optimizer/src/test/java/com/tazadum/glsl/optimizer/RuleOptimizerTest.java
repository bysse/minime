package com.tazadum.glsl.optimizer;

import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.arithmetic.NumericLeafNode;
import com.tazadum.glsl.language.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.language.context.GLSLContext;
import com.tazadum.glsl.language.type.FullySpecifiedType;
import com.tazadum.glsl.language.type.Numeric;
import com.tazadum.glsl.language.variable.VariableRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static com.tazadum.glsl.language.type.PredefinedType.FLOAT;
import static com.tazadum.glsl.optimizer.RefCheck.*;
import static com.tazadum.glsl.util.SourcePosition.TOP;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Erik on 2018-03-30.
 */
public class RuleOptimizerTest extends BaseOptimizerTest {
    @Override
    protected OptimizerType[] getOptimizerTypes() {
        return new OptimizerType[]{OptimizerType.ArithmeticOptimizerType};
    }

    @BeforeEach
    public void setUp() {
        testInit(2);

        GLSLContext global = parserContext.globalContext();
        VariableRegistry registry = parserContext.getVariableRegistry();

        Numeric ONE = Numeric.create("1");
        FullySpecifiedType floatType = new FullySpecifiedType(FLOAT);

        registry.declareVariable(global, new VariableDeclarationNode(TOP, true, floatType, "x", null, new NumericLeafNode(TOP, ONE), null));
        registry.declareVariable(global, new VariableDeclarationNode(TOP, true, floatType, "y", null, new NumericLeafNode(TOP, ONE), null));
    }

    @ParameterizedTest(name = "optimize: {0}")
    @DisplayName("Optimizations that should work")
    @MethodSource("getPositiveCases")
    void testOptimizerPositive(String source, String expected, List<RefCheck> checks) {
        Node node = optimize(source);
        String result = toString(node);
        assertEquals(expected, result.trim());

        RefCheck.runChecks(checks, parserContext);
    }

    private static Arguments[] getPositiveCases() {
        return new Arguments[]{
                Arguments.of(
                        "float b=x*2;", "float b=2*x;",
                        list(
                                variable("x", 1)
                        )
                ),
                Arguments.of(
                        "float b=3*x*2;", "float b=2*3*x;",
                        list(
                                variable("x", 1)
                        )
                ),
                Arguments.of(
                        "float b=x*y*2;", "float b=2*x*y;",
                        list(
                                variable("x", 1),
                                variable("y", 1)
                        )
                ),
                Arguments.of(
                        "float b=x*x*2*y*y;", "float b=2*x*x*y*y;",
                        list(
                                variable("x", 2),
                                variable("y", 2)
                        )
                ),
                Arguments.of("float b=2+x;", "float b=x+2;",
                        list(
                                variable("x", 1)
                        )
                ),
                Arguments.of("float b=(1+x);", "float b=(x+1);",
                        list(
                                variable("x", 1),
                                variable("b", 0)
                        )
                ),
                Arguments.of("float a=0*5;", "float a=0;", noChecks()),
                Arguments.of("float a=5*0;", "float a=0;", noChecks()),
                Arguments.of(
                        "float b=2,a=0*b;", "float b=2,a=0;",
                        list(
                                variable("a", 0),
                                variable("b", 0)
                        )
                ),
                Arguments.of("float a=1*5;", "float a=5;", noChecks()),
                Arguments.of("float a=5*1;", "float a=5;", noChecks()),
                Arguments.of("float b=2,a=1*b;", "float b=2,a=b;",
                        list(
                                variable("a", 0),
                                variable("b", 1)
                        )
                ),
                Arguments.of("float a=0+1;", "float a=1;", noChecks()),
                Arguments.of("float a=1-0;", "float a=1;", noChecks()),
                Arguments.of("float a=1-1;", "float a=0;", noChecks()),
                Arguments.of("float b=x/x;", "float b=1;", noChecks()),
                Arguments.of("float b=0/x;", "float b=0;", noChecks()),
                Arguments.of("float b=(1+x)/(1+x);", "float b=1;",
                        list(
                                variable("x", 0),
                                variable("b", 0)
                        )
                ),
                Arguments.of("float b=(1+x)/(x+1);", "float b=1;",
                        list(
                                variable("x", 0),
                                variable("b", 0)
                        )
                ),
                Arguments.of("float a=pow(2,0);", "float a=1;", noChecks()),
                Arguments.of("float a=pow(x,0);", "float a=1;",
                        list(
                                variable("x", 0)
                        )
                ),
                Arguments.of("float a=pow(2,1);", "float a=2;", noChecks()),
                Arguments.of("float a=pow(x,1);", "float a=x;",
                        list(
                                variable("x", 1)
                        )
                ),
                Arguments.of("float a=pow(3,2);", "float a=3*3;", noChecks()),
                Arguments.of("float a=abs(2);", "float a=2;", noChecks()),
                Arguments.of("float a=abs(-2);", "float a=2;", noChecks()),
                Arguments.of("float a=sin(0);", "float a=0;", noChecks()),
                Arguments.of("float a=cos(0);", "float a=1;", noChecks()),
                Arguments.of("vec2 u,v;float a=distance(u,v);", "vec2 u,v;float a=length((u)-(v));", noChecks()),
        };
    }


}

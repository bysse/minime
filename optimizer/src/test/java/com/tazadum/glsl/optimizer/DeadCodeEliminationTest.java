package com.tazadum.glsl.optimizer;

import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.language.context.GLSLContext;
import com.tazadum.glsl.language.type.FullySpecifiedType;
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
 * Created by Erik on 2016-10-20.
 */
public class DeadCodeEliminationTest extends BaseOptimizerTest {
    @Override
    protected OptimizerType[] getOptimizerTypes() {
        return new OptimizerType[]{OptimizerType.DeadCodeEliminationType};
    }

    @BeforeEach
    void setup() {
        testInit(3);
    }

    @ParameterizedTest(name = "optimize: {1}")
    @DisplayName("Optimizations that should work")
    @MethodSource("getPositiveCases")
    void testOptimizerPositive(String expected, String source, List<RefCheck> checks) {
        GLSLContext context = parserContext.currentContext();
        VariableRegistry registry = parserContext.getVariableRegistry();
        registry.declareVariable(context, new VariableDeclarationNode(TOP, true, new FullySpecifiedType(FLOAT), "blackhole", null, null, null));

        Node node = optimize(source);
        assertEquals(expected, toString(node));

        RefCheck.runChecks(checks, parserContext);
    }

    private static Arguments[] getPositiveCases() {
        return new Arguments[]{
                Arguments.of(
                        "void main(){}",
                        "void main(){}",
                        noChecks()
                ),
                Arguments.of(
                        "void mainImage(out vec4 fragColor,in vec2 fragCoord){}",
                        "void mainImage(out vec4 fragColor,in vec2 fragCoord){}",
                        noChecks()
                ),
                Arguments.of(
                        "void main(){}",
                        "void main(){int a=0;}",
                        list(variable("a", 0))
                ),
                Arguments.of(
                        "void main(){}",
                        "int a=0;void f(){a=1;}void main(){}",
                        list(variable("a", 0), function("f", 0))
                ),
                Arguments.of(
                        "void main(){float b=1;blackhole=2+b;}",
                        "void main(){float b=1;blackhole=2+b;}",
                        list(variable("b", 1))
                ),
                Arguments.of(
                        "struct S{float a;} s={1};void main(){blackhole=s.a;}",
                        "struct S{float a;} s={1}; void main(){blackhole=s.a;}",
                        list(variable("s", 1))
                ),
                Arguments.of(
                        "struct S{float a;};S s={1};void main(){blackhole=s.a;}",
                        "struct S{float a;}; S s={1}; void main(){blackhole=s.a;}",
                        list(variable("s", 1), type("S", 1))
                ),
                Arguments.of(
                        "struct{float a;} s={1};void main(){blackhole=s.a;}",
                        "struct{float a;} s={1}; void main(){blackhole=s.a;}",
                        list(variable("s", 1))
                ),
                Arguments.of(
                        "void main(){}",
                        "struct S{float a;}; void main(){S b;}",
                        list(variable("b", 0), type("S", 0))
                ),
        };
    }
}

package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.language.BuiltInType;
import com.tazadum.glsl.language.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.type.FullySpecifiedType;
import com.tazadum.glsl.parser.variable.VariableRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Created by Erik on 2016-10-20.
 */
public class FunctionInlineOptimizerTest extends BaseOptimizerTest {
    @Override
    protected OptimizerType[] getOptimizerTypes() {
        return new OptimizerType[]{OptimizerType.FunctionInline, OptimizerType.DeadCodeEliminationType};
    }

    @BeforeEach
    public void setup() {
        testInit();

        ParserContext parserContext = optimizerContext.parserContext();
        VariableRegistry registry = parserContext.getVariableRegistry();
        registry.declareVariable(parserContext.globalContext(), new VariableDeclarationNode(true, new FullySpecifiedType(BuiltInType.FLOAT), "blackhole", null, null));
    }

    @Test
    @DisplayName("Basic inlining")
    public void testBasicSingleLine() {
        assertEquals(
                "void main(){blackhole=2;}",
                optimize("float func(float a){return a;}void main(){blackhole=func(2);}")
        );

        assertEquals(
                "void main(){blackhole=2*2;}",
                optimize("float func(float a){return 2*a;}void main(){blackhole=func(2);}")
        );

        assertEquals(
                "void main(){float b=1;blackhole=2*(2+b);}",
                optimize("float func(float a){return 2*a;}void main(){float b=1;blackhole=func(2+b);}")
        );
    }


    @Test
    @DisplayName("Basic multiline inlinng")
    public void testBasicMultiLine() {
        // TODO: use sourceEquals
        assertEquals(
                "void main(){float b=1;float c=(2+b)+1;blackhole=c;}",
                optimize("float func(float a){float c=a+1; return c;}void main(){float b=1;blackhole=func(2+b);}")
        );
    }

    private void sourceEquals(String expected, String actual) {
        if (expected.contains("?")) {
            if (expected.length() != actual.length()) {
                assertEquals(expected, actual);
            }

            for (int i = 0; i < expected.length(); i++) {
                char ch = expected.charAt(i);
                if (ch == '?') {
                    continue;
                }
                if (ch != actual.charAt(i)) {
                    fail("Expected " + expected + " but was " + actual);
                }
            }
        }
        assertEquals(expected, actual);
    }
}

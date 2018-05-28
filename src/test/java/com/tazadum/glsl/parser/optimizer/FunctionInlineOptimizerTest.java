package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.language.BuiltInType;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.type.FullySpecifiedType;
import com.tazadum.glsl.parser.variable.VariableRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Erik on 2016-10-20.
 */
public class FunctionInlineOptimizerTest extends BaseOptimizerTest {
    @Override
    protected OptimizerType[] getOptimizerTypes() {
        return new OptimizerType[] { OptimizerType.FunctionInline, OptimizerType.DeadCodeEliminationType };
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
    public void testBasic() {
        assertEquals(
                "void main(){blackhole=2;}",
                optimize("float func(float a){return a;}void main(){blackhole=func(2);}")
        );

        assertEquals(
                "void main(){blackhole=2*2;}",
                optimize("float func(float a){return 2*a;}void main(){blackhole=func(2);}")
        );

    }

    @Test
    @DisplayName("Basic inline of terms")
    public void testBasicTerms() {

        showDebug = true;
        // This probably fails because of reference errors in usage

        assertEquals(
                "void main(){float b=1;blackhole=2*(2+b);}",
                optimize("float func(float a){return 2*a;}void main(){float b=1;blackhole=func(2+b);}")
        );
    }
}

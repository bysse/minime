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
        registry.declare(parserContext.globalContext(), new VariableDeclarationNode(true, new FullySpecifiedType(BuiltInType.FLOAT), "blackhole", null, null));
    }

    @Test
    @DisplayName("Cases that should be ignored")
    public void testIgnore() {
        assertEquals(
                "float func(float a){return 2*a;}float a=func(2);void main(){float a=func(2);}",
                optimize("float func(float a){return 2*a;}float a=func(2);void main(){float a=func(2);}")
        );
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
}

package com.tazadum.glsl.parser.optimizer;

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
        return new OptimizerType[] { OptimizerType.FunctionInline };
    }

    @BeforeEach
    public void setup() {
        testInit();
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
               "void main(){float a=func(2);}",
                optimize("float func(float a){return 2*a;}void main(){float a=func(2);}")
        );
    }
}

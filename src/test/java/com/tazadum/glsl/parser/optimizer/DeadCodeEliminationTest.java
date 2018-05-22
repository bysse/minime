package com.tazadum.glsl.parser.optimizer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Erik on 2016-10-20.
 */
public class DeadCodeEliminationTest extends BaseOptimizerTest {
    @Override
    protected OptimizerType[] getOptimizerTypes() {
        return new OptimizerType[] { OptimizerType.DeadCodeEliminationType };
    }

    @BeforeEach
    public void setup() {
        testInit();
    }

    @Test
    public void test_basic_preservation() {
        assertEquals("void main(){}", optimize("void main(){}"));
        assertEquals("void mainImage(out vec4 fragColor,in vec2 fragCoord){}", optimize("void mainImage(out vec4 fragColor,in vec2 fragCoord){}"));
    }

    @Test
    public void test_basic_elimination() {
        assertEquals("void main(){}", optimize("void main(){int a=0;}"));
    }

    @Test
    public void test_advanced_elimination() {
        assertEquals("void main(){}", optimize("int a=0;void f(){a=1;}void main(){}"));
    }
}

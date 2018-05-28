package com.tazadum.glsl.simplification;

import com.tazadum.glsl.parser.optimizer.BaseOptimizerTest;
import com.tazadum.glsl.parser.optimizer.OptimizerType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Erik on 2018-03-30.
 */
public class RuleOptimizerTest extends BaseOptimizerTest {
    @Override
    protected OptimizerType[] getOptimizerTypes() {
        return new OptimizerType[] {OptimizerType.ArithmeticOptimizerType };
    }

    @BeforeEach
    public void setUp() throws Exception {
        testInit();
    }

    @Test
    @DisplayName("Rearrangement rules")
    public void testReArrangement() {
        test("float a=1,b=a*2;", "float a=1,b=2*a;");

        test("float a=1,b=2+a;", "float a=1,b=a+2;");

        test("float a=1,b=(1+a);", "float a=1,b=(a+1);");
    }

    @Test
    @DisplayName("Simple arithmetic simplifications")
    public void testSimple() throws Exception {
        test("float a=0*5;", "float a=0;");
        test("float a=5*0;", "float a=0;");
        test("float b=2,a=0*b;", "float b=2,a=0;");

        test("float a=1*5;", "float a=5;");
        test("float a=5*1;", "float a=5;");
        test("float b=2,a=1*b;", "float b=2,a=b;");

        test("float a=0+1;", "float a=1;");
        test("float a=1-0;", "float a=1;");

        test("float a=1-1;", "float a=0;");
    }

    @Test
    @DisplayName("More advanced optimizations")
    public void testDivision() {
        test("float a=1,b=a/a;", "float a=1,b=1;");
        test("float a=1,b=(1+a)/(1+a);", "float a=1,b=1;");
        test("float a=1,b=(1+a)/(a+1);", "float a=1,b=1;");
    }

    @Test
    @DisplayName("Functional replacement")
    public void testFunctional() {
        test("float a=pow(2,1);", "float a=2;");
        test("float a=pow(3,2);", "float a=3*3;");

        test("float a=abs(2);", "float a=2;");
        //test("float a=abs(-2);", "float a=2;");
    }


    private void test(String expression, String expected) {
        String result = optimize(expression);
        assertEquals(expected, result.trim());
    }
}
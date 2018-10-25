package com.tazadum.glsl.simplification;

import com.tazadum.glsl.language.BuiltInType;
import com.tazadum.glsl.language.Numeric;
import com.tazadum.glsl.language.ast.arithmetic.FloatLeafNode;
import com.tazadum.glsl.language.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.parser.GLSLContext;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.optimizer.BaseOptimizerTest;
import com.tazadum.glsl.parser.optimizer.OptimizerType;
import com.tazadum.glsl.parser.type.FullySpecifiedType;
import com.tazadum.glsl.parser.variable.VariableRegistry;
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

        ParserContext parserContext = optimizerContext.parserContext();
        GLSLContext global = parserContext.globalContext();
        VariableRegistry registry = parserContext.getVariableRegistry();
        registry.declareVariable(global, new VariableDeclarationNode(true, new FullySpecifiedType(BuiltInType.FLOAT), "x", null, new FloatLeafNode(new Numeric(1, 0, true))));
        registry.declareVariable(global, new VariableDeclarationNode(true, new FullySpecifiedType(BuiltInType.FLOAT), "y", null, new FloatLeafNode(new Numeric(1, 0, true))));
    }

    @Test
    @DisplayName("Rearrangement rules")
    public void testReArrangement() {
        test("float b=x*2;", "float b=2*x;");
        test("float b=3*x*2;", "float b=2*3*x;");
        test("float b=x*y*2;", "float b=2*x*y;");
        test("float b=x*x*2*y*y;", "float b=2*x*x*y*y;");

        test("float b=2+x;", "float b=x+2;");

        test("float b=(1+x);", "float b=(x+1);");
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
        test("float b=x/x;", "float b=1;");
        test("float b=0/x;", "float b=0;");
        test("float b=(1+x)/(1+x);", "float b=1;");
        test("float b=(1+x)/(x+1);", "float b=1;");
    }

    @Test
    @DisplayName("Functional replacement")
    public void testFunctional() {
        test("float a=pow(2,1);", "float a=2;");
        test("float a=pow(3,2);", "float a=3*3;");

        test("float a=abs(2);", "float a=2;");
        test("float a=abs(-2);", "float a=2;");

        test("float a=sin(0);", "float a=0;");
        test("float a=cos(0);", "float a=1;");
    }

    private void test(String expression, String expected) {
        String result = optimize(expression);
        assertEquals(expected, result.trim());
    }
}

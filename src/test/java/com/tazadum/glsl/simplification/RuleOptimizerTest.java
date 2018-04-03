package com.tazadum.glsl.simplification;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.output.Output;
import com.tazadum.glsl.output.OutputConfig;
import com.tazadum.glsl.output.OutputSizeDecider;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.TestUtils;
import com.tazadum.glsl.parser.optimizer.Optimizer;
import com.tazadum.glsl.parser.optimizer.RuleOptimizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Erik on 2018-03-30.
 */
public class RuleOptimizerTest {
    private ParserContext parserContext;
    private Output output;
    private OutputConfig config;
    private RuleOptimizer optimizer;
    private OutputSizeDecider decider;

    @BeforeEach
    public void setUp() throws Exception {
        output = new Output();
        config = new OutputConfig();
        optimizer = new RuleOptimizer(new RuleSet().getRules());
        decider = new OutputSizeDecider();

        parserContext = TestUtils.parserContext();
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

    private void test(String expression, String expected) {
        Node node = compile(expression);
        Optimizer.OptimizerResult result = optimizer.run(parserContext, decider, node);
        assertEquals(expected, render(result.getNode()).trim());
    }

    private Node compile(String expression) {
        return TestUtils.parse(parserContext, Node.class, expression);
    }

    private String render(Node node) {
        return output.render(node, config);
    }
}
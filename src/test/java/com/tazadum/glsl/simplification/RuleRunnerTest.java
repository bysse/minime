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
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Erik on 2018-03-30.
 */
public class RuleRunnerTest {
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
    public void testSimple() throws Exception {
        test("0*5", "0");
    }

    private void test(String expression, String expected) {
        Node node = compile(expression);
        Optimizer.OptimizerResult result = optimizer.run(parserContext, decider, node);
        assertEquals(expected, render(result.getNode()));
    }

    private Node compile(String expression) {
        return TestUtils.parse(parserContext, Node.class, expression);
    }

    private String render(Node node) {
        return output.render(node, config);
    }
}
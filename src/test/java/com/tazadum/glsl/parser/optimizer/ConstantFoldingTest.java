package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.language.BuiltInType;
import com.tazadum.glsl.language.GLSLParser;
import com.tazadum.glsl.output.Output;
import com.tazadum.glsl.output.OutputConfig;
import com.tazadum.glsl.output.OutputSizeDecider;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.TestUtils;
import com.tazadum.glsl.parser.type.FullySpecifiedType;
import com.tazadum.glsl.parser.variable.VariableRegistry;
import com.tazadum.glsl.parser.visitor.ContextVisitor;
import com.tazadum.glsl.parser.type.TypeChecker;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Erik on 2016-10-20.
 */
public class ConstantFoldingTest {
    final private OutputSizeDecider decider = new OutputSizeDecider();

    private ParserContext parserContext;
    private Output output;
    private ConstantFolding constantFolding;
    private TypeChecker typeChecker;

    @Before
    public void setup() {
        parserContext = TestUtils.parserContext();
        output = new Output();
        constantFolding = new ConstantFolding();
        typeChecker = new TypeChecker();

        decider.getConfig().setMaxDecimals(3);
    }

    @Test
    public void test_parenthesis() {
        assertEquals("1", optimize("(1)"));
        assertEquals("1", optimize("(((1)))"));
    }

    @Test
    public void test_0_elimination_mul() {
        assertEquals("0", optimize("0*1"));
        assertEquals("0", optimize("2*0"));
        assertEquals("0", optimize("0.*1"));
        assertEquals("0", optimize("0*2."));
        assertEquals("0", optimize("0.*2."));
        assertEquals("0", optimize("2.*0."));
    }

    @Test
    public void test_0_elimination_div() {
        assertEquals("1", optimize("2/2"));
        assertEquals("0", optimize("0/2"));
        assertEquals("5", optimize("5/1"));
    }

    @Test
    public void test_0_elimination_add() {
        assertEquals("1", optimize("1+0"));
        assertEquals("1", optimize("0+1"));
    }

    @Test
    public void test_0_elimination_sub() {
        assertEquals("1", optimize("1-0"));
        assertEquals("-1", optimize("0-1"));
    }

        @Test
    public void test_1_elimination() {
        VariableRegistry registry = parserContext.getVariableRegistry();
        registry.declare(parserContext.currentContext(), new VariableDeclarationNode(true, new FullySpecifiedType(BuiltInType.FLOAT), "var", null, null));

        assertEquals("var", optimize("var*1"));
        assertEquals("5", optimize("1*5"));
    }

    @Test
    public void test_folding_basic() {
        assertEquals("2", optimize("1+1"));
        assertEquals("4", optimize("2*2"));
        assertEquals("3", optimize("6/2"));
        assertEquals("4", optimize("6-2"));

        assertEquals(".9", optimize("1-0.1"));
        assertEquals("1.1", optimize("1+0.1"));
        assertEquals("1.21", optimize("1.1*1.1"));
        assertEquals("4.29", optimize("9/2.1"));

        // the result is actually larger than the input
        assertEquals("4/3", optimize("4/3"));
    }

    @Test
    public void test_folding_adv() {
        VariableRegistry registry = parserContext.getVariableRegistry();
        registry.declare(parserContext.currentContext(), new VariableDeclarationNode(true, new FullySpecifiedType(BuiltInType.FLOAT), "var", null, null));

        assertEquals(".0121", optimize(".11*.11"));
        assertEquals("9", optimize("3*(2+var/var)"));
        assertEquals("3", optimize("(1+2)+(0*var)"));
    }

    @Test
    public void test_folding_chain() {
        VariableRegistry registry = parserContext.getVariableRegistry();
        registry.declare(parserContext.currentContext(), new VariableDeclarationNode(true, new FullySpecifiedType(BuiltInType.FLOAT), "var", null, null));

        assertEquals("4*var", optimize("2 * var * 2"));
        assertEquals("2+var", optimize("1 + var + 1"));
        assertEquals("-var", optimize("1 - var - 1"));
        assertEquals("4*var*var", optimize("2 * var * 2 * var"));
    }

    private String optimize(String source) {
        try {
            final CommonTokenStream stream = TestUtils.tokenStream(source);
            final GLSLParser parser = TestUtils.parser(stream);
            final ContextVisitor visitor = new ContextVisitor(parserContext);
            Node node = parser.expression().accept(visitor);
            typeChecker.check(parserContext, node);
            Optimizer.OptimizerResult result = constantFolding.run(parserContext, decider, node);
            return output.render(result.getNode(), new OutputConfig());
        } catch (Exception e) {
            for (Token token : TestUtils.getTokens(TestUtils.tokenStream(source))) {
                System.out.println(token);
            }
            throw e;
        }
    }
}
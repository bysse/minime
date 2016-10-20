package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.language.GLSLParser;
import com.tazadum.glsl.output.Output;
import com.tazadum.glsl.output.OutputConfig;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.TestUtils;
import com.tazadum.glsl.parser.visitor.ContextVisitor;
import com.tazadum.glsl.type.TypeChecker;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Erik on 2016-10-20.
 */
public class ConstantFoldingTest {
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
    }

    @Test
    public void test_int0elimination() {
        assertEquals("0", optimize("0*1"));
        assertEquals("0", optimize("1*0"));
        assertEquals("0.", optimize("0.*1"));
        assertEquals("0.", optimize("0*1."));
        assertEquals("0.", optimize("0.*1."));
        assertEquals("0.", optimize("1.*0."));
    }

    private String optimize(String source) {
        try {
            final CommonTokenStream stream = TestUtils.tokenStream(source);
            final GLSLParser parser = TestUtils.parser(stream);
            final ContextVisitor visitor = new ContextVisitor(parserContext);
            Node node = parser.expression().accept(visitor);
            typeChecker.check(parserContext, node);
            Optimizer.OptimizerResult result = constantFolding.run(node);
            return output.render(result.getNode(), new OutputConfig());
        } catch (Exception e) {
            for (Token token : TestUtils.getTokens(TestUtils.tokenStream(source))) {
                System.out.println(token);
            }
            throw e;
        }
    }
}
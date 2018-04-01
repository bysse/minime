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
import com.tazadum.glsl.parser.type.TypeChecker;
import com.tazadum.glsl.parser.variable.VariableRegistry;
import com.tazadum.glsl.parser.visitor.ContextVisitor;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Erik on 2016-10-20.
 */
public class ConstantFoldingTest {
    final private OutputSizeDecider decider = new OutputSizeDecider();

    private ParserContext parserContext;
    private Output output;
    private ConstantFolding constantFolding;
    private TypeChecker typeChecker;

    @BeforeEach
    public void setup() {
        parserContext = TestUtils.parserContext();
        output = new Output();
        constantFolding = new ConstantFolding();
        typeChecker = new TypeChecker();

        decider.getConfig().setMaxDecimals(3);
    }

    @Test
    public void test_vector_construction_single_arg() throws Exception {
        VariableRegistry registry = parserContext.getVariableRegistry();
        registry.declare(parserContext.currentContext(), new VariableDeclarationNode(true, new FullySpecifiedType(BuiltInType.VEC2), "v2", null, null));
        registry.declare(parserContext.currentContext(), new VariableDeclarationNode(true, new FullySpecifiedType(BuiltInType.VEC3), "v3", null, null));
        registry.declare(parserContext.currentContext(), new VariableDeclarationNode(true, new FullySpecifiedType(BuiltInType.VEC4), "v4", null, null));

        assertEquals("v2", optimize("vec2(v2)"));
        assertEquals("v3", optimize("vec3(v3)"));
        assertEquals("v4", optimize("vec4(v4)"));

        assertEquals("vec2(0,1)", optimize("vec2(vec2(0,1))"));
    }

    @Test
    public void test_vector_construction_parameter_collapsing() throws Exception {
        VariableRegistry registry = parserContext.getVariableRegistry();
        registry.declare(parserContext.currentContext(), new VariableDeclarationNode(true, new FullySpecifiedType(BuiltInType.VEC2), "v2", null, null));
        registry.declare(parserContext.currentContext(), new VariableDeclarationNode(true, new FullySpecifiedType(BuiltInType.VEC3), "v3", null, null));
        registry.declare(parserContext.currentContext(), new VariableDeclarationNode(true, new FullySpecifiedType(BuiltInType.VEC4), "v4", null, null));

        assertEquals("v2.xy", optimize("vec2(v2.x,v2.y)"));
        assertEquals("v3.xyz", optimize("vec3(v3.x,v3.y,v3.z)"));
        assertEquals("v4.xyzw", optimize("vec4(v4.x,v4.y,v4.z,v4.w)"));

        assertEquals("vec4(v4.x,v4.y,v4.w,v4.z)", optimize("vec4(v4.x,v4.y,v4.w,v4.z)"));
        assertEquals("vec4(v2.x,v4.y,v4.z,v4.w)", optimize("vec4(v2.x,v4.y,v4.z,v4.w)"));
    }

    @Test
    public void test_field_selection_removal() throws Exception {
        VariableRegistry registry = parserContext.getVariableRegistry();
        registry.declare(parserContext.currentContext(), new VariableDeclarationNode(true, new FullySpecifiedType(BuiltInType.VEC2), "v2", null, null));
        registry.declare(parserContext.currentContext(), new VariableDeclarationNode(true, new FullySpecifiedType(BuiltInType.VEC3), "v3", null, null));
        registry.declare(parserContext.currentContext(), new VariableDeclarationNode(true, new FullySpecifiedType(BuiltInType.VEC4), "v4", null, null));

        assertEquals("v2=v2", optimize("v2=v2.xy"));
        assertEquals("v3=v3", optimize("v3=v3.xyz"));
        assertEquals("v4=v4", optimize("v4=v4.xyzw)"));

        assertEquals("v2=v2.yx", optimize("v2=v2.yx"));
        assertEquals("v3=v3.xyy", optimize("v3=v3.xyy"));
        assertEquals("v4=v4.xywz", optimize("v4=v4.xywz)"));
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

    /*
    @Test
    public void test_folding_chain_div() {
        VariableRegistry registry = parserContext.getVariableRegistry();
        registry.declare(parserContext.currentContext(), new VariableDeclarationNode(true, new FullySpecifiedType(BuiltInType.FLOAT), "var", null, null));

        assertEquals("var", optimize("2 * var / 2"));
    }
    */

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
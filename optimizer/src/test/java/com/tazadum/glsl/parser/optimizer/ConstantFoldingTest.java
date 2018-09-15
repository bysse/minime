package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.language.BuiltInType;
import com.tazadum.glsl.language.GLSLParser;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.type.FullySpecifiedType;
import com.tazadum.glsl.parser.variable.VariableRegistry;
import org.antlr.v4.runtime.ParserRuleContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Erik on 2016-10-20.
 */
public class ConstantFoldingTest extends BaseOptimizerTest {
    @Override
    protected OptimizerType[] getOptimizerTypes() {
        return new OptimizerType[]{OptimizerType.ConstantFoldingType};
    }

    @Override
    protected ParserRuleContext extractContext(GLSLParser parser) {
        return parser.expression();
    }

    @BeforeEach
    public void setup() {
        testInit(2);
    }

    @Test
    public void test_vector_construction_single_arg() throws Exception {
        ParserContext parserContext = optimizerContext.parserContext();
        VariableRegistry registry = parserContext.getVariableRegistry();
        registry.declareVariable(parserContext.currentContext(), new VariableDeclarationNode(true, new FullySpecifiedType(BuiltInType.VEC2), "v2", null, null));
        registry.declareVariable(parserContext.currentContext(), new VariableDeclarationNode(true, new FullySpecifiedType(BuiltInType.VEC3), "v3", null, null));
        registry.declareVariable(parserContext.currentContext(), new VariableDeclarationNode(true, new FullySpecifiedType(BuiltInType.VEC4), "v4", null, null));

        assertEquals("v2", optimize("vec2(v2)"));
        assertEquals("v3", optimize("vec3(v3)"));
        assertEquals("v4", optimize("vec4(v4)"));

        assertEquals("vec2(0,1)", optimize("vec2(vec2(0,1))"));
    }

    @Test
    public void test_vector_construction_parameter_collapsing() throws Exception {
        ParserContext parserContext = optimizerContext.parserContext();
        VariableRegistry registry = parserContext.getVariableRegistry();
        registry.declareVariable(parserContext.currentContext(), new VariableDeclarationNode(true, new FullySpecifiedType(BuiltInType.VEC2), "v2", null, null));
        registry.declareVariable(parserContext.currentContext(), new VariableDeclarationNode(true, new FullySpecifiedType(BuiltInType.VEC3), "v3", null, null));
        registry.declareVariable(parserContext.currentContext(), new VariableDeclarationNode(true, new FullySpecifiedType(BuiltInType.VEC4), "v4", null, null));

        assertEquals("v2", optimize("vec2(v2.x,v2.y)"));
        assertEquals("v3", optimize("vec3(v3.x,v3.y,v3.z)"));
        assertEquals("v4", optimize("vec4(v4.r,v4.y,v4.z,v4.w)"));

        assertEquals("v2.yx", optimize("vec2(v2.y,v2.x)"));
        assertEquals("v3.xxz", optimize("vec3(v3.x,v3.x,v3.z)"));
        assertEquals("v4.yxzw", optimize("vec4(v4.y,v4.x,v4.z,v4.w)"));

        assertEquals("vec4(v2.x,v4.y,v4.z,v4.w)", optimize("vec4(v2.x,v4.y,v4.z,v4.w)"));
        //assertEquals("vec4(v2.x,v4.yzw)", optimize("vec4(v2.x,v4.y,v4.z,v4.w)"));
        assertEquals("v4.xywz", optimize("vec4(v4.x,v4.y,v4.w,v4.z)"));
        assertEquals("v2.xyxy", optimize("vec4(v2.x,v2.y,v2.x,v2.y)"));
    }

    @Test
    public void test_field_selection_removal() throws Exception {
        ParserContext parserContext = optimizerContext.parserContext();
        VariableRegistry registry = parserContext.getVariableRegistry();
        registry.declareVariable(parserContext.currentContext(), new VariableDeclarationNode(true, new FullySpecifiedType(BuiltInType.VEC2), "v2", null, null));
        registry.declareVariable(parserContext.currentContext(), new VariableDeclarationNode(true, new FullySpecifiedType(BuiltInType.VEC3), "v3", null, null));
        registry.declareVariable(parserContext.currentContext(), new VariableDeclarationNode(true, new FullySpecifiedType(BuiltInType.VEC4), "v4", null, null));

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
    public void test_folding_basic() {
        assertEquals("2", optimize("1+1"));
        assertEquals("4", optimize("2*2"));
        assertEquals("3", optimize("6/2"));
        assertEquals("4", optimize("6-2"));

        assertEquals(".9", optimize("1-0.1"));
        assertEquals("1.1", optimize("1+0.1"));
        assertEquals("1.21", optimize("1.1*1.1"));
        assertEquals("4.29", optimize("9/2.1"));
    }

    @Test
    @DisplayName("Optimizations that aren't bigger")
    public void test_branching() {
        // the result is actually larger than the input
        assertEquals("4/3", optimize("4/3"));
    }

    @Test
    public void test_folding_adv() {
        assertEquals(".0121", optimize(".11*.11"));
        assertEquals("6", optimize("3*2"));
        assertEquals("3", optimize("(1+2)"));
    }
}
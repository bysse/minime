package com.tazadum.glsl.optimizer;

import com.tazadum.glsl.language.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.language.context.GLSLContext;
import com.tazadum.glsl.language.type.FullySpecifiedType;
import com.tazadum.glsl.language.variable.VariableRegistry;
import com.tazadum.glsl.parser.GLSLParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static com.tazadum.glsl.language.type.PredefinedType.*;
import static com.tazadum.glsl.util.SourcePosition.TOP;
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
        return parser.assignment_expression();
    }

    @BeforeEach
    public void setup() {
        testInit(3);
    }

    @ParameterizedTest(name = "optimize: {1}")
    @DisplayName("Optimizations that should work")
    @MethodSource("getPositiveCases")
    void testOptimizerPositive(String expected, String source) {
        GLSLContext context = parserContext.currentContext();
        VariableRegistry registry = parserContext.getVariableRegistry();
        registry.declareVariable(context, new VariableDeclarationNode(TOP, true, new FullySpecifiedType(FLOAT), "v1", null, null, null));
        registry.declareVariable(context, new VariableDeclarationNode(TOP, true, new FullySpecifiedType(VEC2), "v2", null, null, null));
        registry.declareVariable(context, new VariableDeclarationNode(TOP, true, new FullySpecifiedType(VEC3), "v3", null, null, null));
        registry.declareVariable(context, new VariableDeclarationNode(TOP, true, new FullySpecifiedType(VEC4), "v4", null, null, null));
        registry.declareVariable(context, new VariableDeclarationNode(TOP, true, new FullySpecifiedType(IVEC4), "i4", null, null, null));

        assertEquals(expected, toString(optimize(source)));
    }

    private static Arguments[] getPositiveCases() {
        return new Arguments[]{
            Arguments.of("2", "1+1"),
            Arguments.of("4", "2*2"),
            Arguments.of("3", "6/2"),
            Arguments.of("4", "6-2"),
            Arguments.of(".9", "1-0.1"),
            Arguments.of("1.1", "1+0.1"),
            Arguments.of("1.21", "1.1*1.1"),
            Arguments.of("1.23", "1.11*1.11"),
            Arguments.of("1.21", "1.1*1.10"),
            Arguments.of("4.29", "9/2.1"),
            Arguments.of("1", "(1)"),
            Arguments.of("1", "(((1)))"),
            Arguments.of(".0121", ".11*.11"),
            Arguments.of("6", "3*2"),
            Arguments.of("3", "(1+2)"),
            Arguments.of("4/3", "4./3."),
            Arguments.of("v2", "vec2(v2)"),
            Arguments.of("v3", "vec3(v3)"),
            Arguments.of("v4", "vec4(v4)"),
            Arguments.of("vec2(0,1)", "vec2(vec2(0,1))"),
            Arguments.of("v2=v2", "v2=v2.xy"),
            Arguments.of("v3=v3", "v3=v3.xyz"),
            Arguments.of("v4=v4", "v4=v4.xyzw)"),
            Arguments.of("v2=v2.yx", "v2=v2.yx"),
            Arguments.of("v3=v3.xyy", "v3=v3.xyy"),
            Arguments.of("v4=v4.xywz", "v4=v4.xywz)"),
            Arguments.of("v1", "float(v1.x)"),
            Arguments.of("v2.y", "float(v2.y)"),
            Arguments.of("v2", "vec2(v2.x,v2.y)"),
            Arguments.of("v2", "vec2(v2.x,v2.y)"),
            Arguments.of("vec2(v2.x,v3.y)", "vec2(v2.x,v3.y)"),
            Arguments.of("v4.xy", "vec2(v4.x,v4.y)"),
            Arguments.of("v3", "vec3(v3.x,v3.y,v3.z)"),
            Arguments.of("v4", "vec4(v4.r,v4.y,v4.z,v4.w)"),
            Arguments.of("v2.yx", "vec2(v2.y,v2.x)"),
            Arguments.of("v3.xxz", "vec3(v3.x,v3.x,v3.z)"),
            Arguments.of("v4.yxzw", "vec4(v4.y,v4.x,v4.z,v4.w)"),
            Arguments.of("v4", "vec4(v4.r,v4.y,v4.z,v4.w)"),
            Arguments.of("v4", "vec4(v4.r,v4.yz,v4.w)"),
            Arguments.of("i4", "vec4(i4.x,i4.y,i4.z,i4.w)"),
            Arguments.of("vec4(v2.x,v4.yzw)", "vec4(v2.x,v4.y,v4.z,v4.w)"),
            Arguments.of("vec4(v4.yzw,v2.x)", "vec4(v4.y,v4.z,v4.w,v2.x)"),
            Arguments.of("vec4(v2,v4.zw)", "vec4(v2.x,v2.y,v4.z,v4.w)"),
            Arguments.of("vec4(i4.xy,v4.zw)", "vec4(i4.x,i4.y,v4.z,v4.w)"),
            Arguments.of("v4.xywz", "vec4(v4.x,v4.y,v4.w,v4.z)"),
            Arguments.of("v2.xyxy", "vec4(v2.x,v2.y,v2.x,v2.y)"),
        };
    }

    @ParameterizedTest(name = "optimize: {0}")
    @DisplayName("Optimizations that should not be done")
    @MethodSource("getNegativeCases")
    void testOptimizerNegative(String source) {
        GLSLContext context = parserContext.currentContext();
        VariableRegistry registry = parserContext.getVariableRegistry();
        registry.declareVariable(context, new VariableDeclarationNode(TOP, true, new FullySpecifiedType(INT), "i1", null, null, null));
        registry.declareVariable(context, new VariableDeclarationNode(TOP, true, new FullySpecifiedType(IVEC3), "i3", null, null, null));
        registry.declareVariable(context, new VariableDeclarationNode(TOP, true, new FullySpecifiedType(VEC2), "v2", null, null, null));
        registry.declareVariable(context, new VariableDeclarationNode(TOP, true, new FullySpecifiedType(VEC4), "v4", null, null, null));

        assertEquals(source, toString(optimize(source)));
    }

    private static String[] getNegativeCases() {
        return new String[]{
            "i3=i3.zyx",
        };
    }
}

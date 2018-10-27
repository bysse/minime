package com.tazadum.glsl.parser.visitor;

import com.tazadum.glsl.TestUtil;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.language.ast.variable.ParameterDeclarationNode;
import com.tazadum.glsl.language.model.StorageQualifier;
import com.tazadum.glsl.parser.GLSLParser;
import com.tazadum.glsl.parser.ParserContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.junit.jupiter.api.Test;

import static com.tazadum.glsl.language.type.PredefinedType.*;
import static org.junit.jupiter.api.Assertions.*;

public class FunctionContextVisitorTest {
    private ParserContext parserContext;

    @Test
    public void test_void_void() {
        FunctionPrototypeNode node;

        node = parse("void main()");
        assertEquals("main", node.getIdentifier().original());
        assertEquals(VOID, node.getReturnType().getType());
        assertEquals(0, node.getChildCount());

        node = parse("void main(void)");
        assertEquals("main", node.getIdentifier().original());
        assertEquals(VOID, node.getReturnType().getType());
        assertEquals(0, node.getChildCount());
    }

    @Test
    public void test_type_void() {
        FunctionPrototypeNode node;

        node = parse("vec3 main()");
        assertEquals("main", node.getIdentifier().original());
        assertEquals(VEC3, node.getReturnType().getType());
        assertEquals(0, node.getChildCount());

        node = parse("float main(void)");
        assertEquals("main", node.getIdentifier().original());
        assertEquals(FLOAT, node.getReturnType().getType());
        assertEquals(0, node.getChildCount());
    }

    @Test
    public void test_void_type() {
        FunctionPrototypeNode node;
        ParameterDeclarationNode parameter;

        node = parse("void main(float a)");
        assertEquals("main", node.getIdentifier().original());
        assertEquals(VOID, node.getReturnType().getType());
        assertEquals(1, node.getChildCount());

        parameter = node.getChildren(ParameterDeclarationNode.class).iterator().next();
        assertEquals(FLOAT, parameter.getFullySpecifiedType().getType());
        assertEquals("a", parameter.getIdentifier().original());

        node = parse("void main(out float a)");
        assertEquals("main", node.getIdentifier().original());
        assertEquals(VOID, node.getReturnType().getType());
        assertEquals(1, node.getChildCount());

        parameter = node.getChildren(ParameterDeclarationNode.class).iterator().next();
        assertEquals(FLOAT, parameter.getFullySpecifiedType().getType());
        assertTrue(parameter.getFullySpecifiedType().getQualifiers().contains(StorageQualifier.OUT));
        assertEquals("a", parameter.getIdentifier().original());
    }

    @Test
    public void test_void_types() {
        FunctionPrototypeNode node;

        node = parse("void main(float a, float b)");
        assertEquals("main", node.getIdentifier().original());
        assertEquals(VOID, node.getReturnType().getType());
        assertEquals(2, node.getChildCount());
    }

    private FunctionPrototypeNode parse(String source) {
        System.out.println("Parsing '" + source + "'");

        final ParserRuleContext context = TestUtil.parse(source, GLSLParser::function_declarator);
        final ParserContext parserContext = TestUtil.parserContext();
        final Node node = TestUtil.ast(context, parserContext);

        if (node instanceof FunctionPrototypeNode) {
            return (FunctionPrototypeNode) node;
        }

        fail("Visitor did not return an instance of " + FunctionPrototypeNode.class.getSimpleName());
        return null;
    }
}

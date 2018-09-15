package com.tazadum.glsl.parser.listener;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.ast.variable.ParameterDeclarationNode;
import com.tazadum.glsl.language.BuiltInType;
import com.tazadum.glsl.language.GLSLParser;
import com.tazadum.glsl.language.TypeQualifier;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.TestUtils;
import com.tazadum.glsl.parser.visitor.ContextVisitor;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class FunctionContextVisitorTest {
    private ParserContext parserContext;

    @Test
    public void test_void_void() {
        FunctionPrototypeNode node;

        node = parse("void main()");
        assertEquals("main", node.getIdentifier().original());
        assertEquals(BuiltInType.VOID, node.getReturnType().getType());
        assertEquals(0, node.getChildCount());

        node = parse("void main(void)");
        assertEquals("main", node.getIdentifier().original());
        assertEquals(BuiltInType.VOID, node.getReturnType().getType());
        assertEquals(0, node.getChildCount());
    }

    @Test
    public void test_type_void() {
        FunctionPrototypeNode node;

        node = parse("vec3 main()");
        assertEquals("main", node.getIdentifier().original());
        assertEquals(BuiltInType.VEC3, node.getReturnType().getType());
        assertEquals(0, node.getChildCount());

        node = parse("float main(void)");
        assertEquals("main", node.getIdentifier().original());
        assertEquals(BuiltInType.FLOAT, node.getReturnType().getType());
        assertEquals(0, node.getChildCount());
    }

    @Test
    public void test_void_type() {
        FunctionPrototypeNode node;
        ParameterDeclarationNode parameter;

        node = parse("void main(float a)");
        assertEquals("main", node.getIdentifier().original());
        assertEquals(BuiltInType.VOID, node.getReturnType().getType());
        assertEquals(1, node.getChildCount());

        parameter = node.getChildren(ParameterDeclarationNode.class).iterator().next();
        assertEquals(BuiltInType.FLOAT, parameter.getFullySpecifiedType().getType());
        assertEquals("a", parameter.getIdentifier().original());

        node = parse("void main(out float a)");
        assertEquals("main", node.getIdentifier().original());
        assertEquals(BuiltInType.VOID, node.getReturnType().getType());
        assertEquals(1, node.getChildCount());

        parameter = node.getChildren(ParameterDeclarationNode.class).iterator().next();
        assertEquals(BuiltInType.FLOAT, parameter.getFullySpecifiedType().getType());
        assertEquals(TypeQualifier.OUT, parameter.getFullySpecifiedType().getQualifier());
        assertEquals("a", parameter.getIdentifier().original());
    }

    @Test
    public void test_void_types() {
        FunctionPrototypeNode node;

        node = parse("void main(float a, float b)");
        assertEquals("main", node.getIdentifier().original());
        assertEquals(BuiltInType.VOID, node.getReturnType().getType());
        assertEquals(2, node.getChildCount());
    }

    private FunctionPrototypeNode parse(String code) {
        System.out.println("Parsing '" + code + "'");

        final CommonTokenStream tokenStream = TestUtils.tokenStream(code);
        final GLSLParser glslParser = TestUtils.parser(tokenStream);

        parserContext = TestUtils.parserContext();

        final ContextVisitor visitor = new ContextVisitor(parserContext);
        final Node node = glslParser.function_declarator().accept(visitor);

        if (node instanceof FunctionPrototypeNode) {
            return (FunctionPrototypeNode) node;
        }

        fail("Visitor did not return an instance of " + FunctionPrototypeNode.class.getSimpleName());
        return null;
    }
}

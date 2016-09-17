package com.tazadum.glsl.parser.visitor;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.function.FunctionDeclarationNode;
import com.tazadum.glsl.ast.variable.ParameterDeclarationNode;
import com.tazadum.glsl.language.BuiltInType;
import com.tazadum.glsl.language.GLSLParser;
import com.tazadum.glsl.language.TypeQualifier;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.TestUtils;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class FunctionDeclarationVisitorTest {
    private ParserContext parserContext;

    @Test
    public void test_void_void() {
        FunctionDeclarationNode node;

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
        FunctionDeclarationNode node;

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
        FunctionDeclarationNode node;
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
        FunctionDeclarationNode node;

        node = parse("void main(float a, float b)");
        assertEquals("main", node.getIdentifier().original());
        assertEquals(BuiltInType.VOID, node.getReturnType().getType());
        assertEquals(2, node.getChildCount());
    }

    private FunctionDeclarationNode parse(String code) {
        System.out.println("Parsing '" + code + "'");

        final CommonTokenStream tokenStream = TestUtils.tokenStream(code);
        final GLSLParser glslParser = TestUtils.parser(tokenStream);

        parserContext = TestUtils.parserContext();

        final FunctionDeclarationVisitor visitor = new FunctionDeclarationVisitor(parserContext);
        final Node node = glslParser.function_declarator().accept(visitor);

        if (node instanceof FunctionDeclarationNode) {
            return (FunctionDeclarationNode) node;
        }

        fail("Visitor did not return an instance of " + FunctionDeclarationNode.class.getSimpleName());
        return null;
    }
}

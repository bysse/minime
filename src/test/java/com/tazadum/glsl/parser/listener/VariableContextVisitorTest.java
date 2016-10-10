package com.tazadum.glsl.parser.listener;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.variable.VariableDeclarationListNode;
import com.tazadum.glsl.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.language.BuiltInType;
import com.tazadum.glsl.language.GLSLParser;
import com.tazadum.glsl.language.GLSLType;
import com.tazadum.glsl.language.PrecisionQualifier;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.TestUtils;
import com.tazadum.glsl.parser.Usage;
import com.tazadum.glsl.parser.type.FullySpecifiedType;
import com.tazadum.glsl.parser.variable.ResolutionResult;
import com.tazadum.glsl.parser.variable.VariableRegistry;
import com.tazadum.glsl.parser.visitor.ContextVisitor;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class VariableContextVisitorTest {
    private ParserContext parserContext;

    @Before
    public void setUp() {
        this.parserContext = TestUtils.parserContext();
    }

    @Test
    public void testDeclaration_single() {
        VariableDeclarationListNode node = parse("mediump float variable");

        assertNotNull(node);

        // test the type
        FullySpecifiedType fst = node.getFullySpecifiedType();
        assertEquals(BuiltInType.FLOAT, fst.getType());
        assertEquals(PrecisionQualifier.MEDIUM_PRECISION, fst.getPrecision());
        assertEquals(null, fst.getQualifier());

        assertEquals(1, node.getChildCount());

        // test the variable declaration
        VariableDeclarationNode child = node.getChildren(VariableDeclarationNode.class).iterator().next();
        assertNotNull(child.getIdentifier());
        assertEquals("variable", child.getIdentifier().original());
        assertEquals(null, child.getArraySpecifier());
        assertEquals(null, child.getInitializer());
        assertEquals(fst, child.getFullySpecifiedType());

        // test variable registration
        VariableRegistry variableRegistry = parserContext.getVariableRegistry();
        ResolutionResult result = variableRegistry.resolve(parserContext.currentContext(), "variable");
        assertNotNull(result);

        // test declaration
        VariableDeclarationNode declaration = result.getDeclaration();
        assertNotNull(declaration);
        assertEquals(child, declaration);

        // TODO: test usage
        //Usage<VariableDeclarationNode> variableUsage = result.getUsage();
        //assertNotNull(variableUsage);
        //assertEquals(1, variableUsage.getUsageNodes().size());

        // test type usage
        Usage<GLSLType> usage = parserContext.getTypeRegistry().usagesOf(fst);
        assertNotNull(usage);
        assertEquals(1, usage.getUsageNodes().size());
    }


    private VariableDeclarationListNode parse(String code) {
        System.out.println("Parsing '" + code + "'");
        CommonTokenStream stream = TestUtils.tokenStream(code);
        GLSLParser parser = TestUtils.parser(stream);
        ContextVisitor visitor = new ContextVisitor(parserContext);

        final Node node = parser.init_declarator_list().accept(visitor);

        if (node instanceof VariableDeclarationListNode) {
            return (VariableDeclarationListNode) node;
        }

        fail("Visitor did not return an instance of " + VariableDeclarationListNode.class.getSimpleName());
        return null;
    }

}

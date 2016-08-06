package com.tazadum.glsl.parser.listener;

import com.tazadum.glsl.ast.VariableDeclarationListNode;
import com.tazadum.glsl.ast.VariableDeclarationNode;
import com.tazadum.glsl.language.BuiltInType;
import com.tazadum.glsl.language.GLSLParser;
import com.tazadum.glsl.language.GLSLType;
import com.tazadum.glsl.language.PrecisionQualifier;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.TestUtils;
import com.tazadum.glsl.parser.Usage;
import com.tazadum.glsl.parser.type.FullySpecifiedType;
import com.tazadum.glsl.parser.variable.VariableRegistry;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class VariableDeclarationListenerTest {
    private ParserContext context;

    @Before
    public void setUp() {
        this.context = TestUtils.parserContext();
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
        assertEquals("variable", child.getIdentifier());
        assertEquals(null, child.getArraySpecifier());
        assertEquals(null, child.getInitializer());
        assertEquals(fst, child.getFullySpecifiedType());

        // test variable registration
        VariableRegistry variableRegistry = context.getVariableRegistry();
        VariableDeclarationNode variable = variableRegistry.resolve("variable");
        assertNotNull(variable);

        Usage<VariableDeclarationNode> variableUsage = variableRegistry.usagesOf(variable);
        assertNotNull(variableUsage);
        assertEquals(1, variableUsage.getUsageNodes().size());

        // test type usage
        Usage<GLSLType> usage = context.getTypeRegistry().usagesOf(fst);
        assertNotNull(usage);
        assertEquals(1, usage.getUsageNodes().size());
    }


    private VariableDeclarationListNode parse(String code) {
        System.out.println("Parsing '" + code + "'");
        CommonTokenStream stream = TestUtils.tokenStream(code);
        GLSLParser parser = TestUtils.parser(stream);
        VariableDeclarationListener listener = new VariableDeclarationListener(context);

        listener.walk(parser.init_declarator_list());

        return listener.getResult();
    }

}

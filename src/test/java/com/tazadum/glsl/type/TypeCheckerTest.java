package com.tazadum.glsl.type;

import com.tazadum.glsl.ast.Identifier;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.TestUtils;
import com.tazadum.glsl.parser.Usage;
import com.tazadum.glsl.parser.function.FunctionPrototype;
import com.tazadum.glsl.parser.function.FunctionRegistry;
import com.tazadum.glsl.parser.type.FullySpecifiedType;
import com.tazadum.glsl.parser.variable.VariableRegistry;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.Map;

/**
 * Created by Erik on 2016-10-17.
 */
@RunWith(Parameterized.class)
public class TypeCheckerTest {
    private final String shaderSource;

    private ParserContext parserContext;
    private TypeChecker typeChecker;

    public TypeCheckerTest(String name, String shaderSource) {
        this.shaderSource = shaderSource;
    }

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<Object[]> shaders() {
        return TestUtils.loadShaders("preformatted");
    }

    @Before
    public void setup() {
        parserContext = TestUtils.parserContext();
        typeChecker = new TypeChecker();
    }

    @Test
    public void testShaderOutput() {
        testOutput(shaderSource);

        displayUsedVariables();
        displayUsedFunctions();
    }

    private void displayUsedFunctions() {
        System.out.println("Function usage:");
        final FunctionRegistry functionRegistry = parserContext.getFunctionRegistry();

        for (Map.Entry<Identifier, Usage<FunctionPrototypeNode>> entry : functionRegistry.getUsedFunctions().entrySet()) {
            final Usage<FunctionPrototypeNode> nodeUsage = entry.getValue();
            final FunctionPrototype prototype = nodeUsage.getTarget().getPrototype();
            System.out.println(String.format("\t%d : %s : %s", nodeUsage.getUsageNodes().size(), entry.getKey().original(), prototype));
        }
    }

    private void displayUsedVariables() {
        System.out.println("Variable usage:");
        final VariableRegistry variableRegistry = parserContext.getVariableRegistry();
        for (Map.Entry<Identifier, Usage<VariableDeclarationNode>> entry : variableRegistry.getUsedVariables().entrySet()) {
            final Usage<VariableDeclarationNode> nodeUsage = entry.getValue();
            final FullySpecifiedType fullySpecifiedType = nodeUsage.getTarget().getFullySpecifiedType();
            System.out.println(String.format("\t%d : %s : %s", nodeUsage.getUsageNodes().size(), entry.getKey().original(), fullySpecifiedType));
        }
    }

    private Node testOutput(String shader) {
        final Node node = TestUtils.parse(parserContext, Node.class, shader);
        typeChecker.check(parserContext, node);
        return node;
    }
}
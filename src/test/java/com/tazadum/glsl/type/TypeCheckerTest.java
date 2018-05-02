package com.tazadum.glsl.type;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.TestUtils;
import com.tazadum.glsl.parser.Usage;
import com.tazadum.glsl.parser.function.FunctionPrototype;
import com.tazadum.glsl.parser.function.FunctionRegistry;
import com.tazadum.glsl.parser.type.FullySpecifiedType;
import com.tazadum.glsl.parser.type.TypeChecker;
import com.tazadum.glsl.parser.variable.VariableRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collection;

/**
 * Created by Erik on 2016-10-17.
 */
public class TypeCheckerTest {
    private ParserContext parserContext;
    private TypeChecker typeChecker;

    public static Collection<String> loadShaders() {
        return TestUtils.loadShaders("preformatted");
    }

    @BeforeEach
    public void setup() {
        parserContext = TestUtils.parserContext();
        typeChecker = new TypeChecker();
    }

    @Test
    @DisplayName("arrays")
    public void testArrays() {
        testOutput("uniform vec2 a[2];" +
                "void main() {" +
                "float b = a[1].x;" +
                "}");
    }


    @ParameterizedTest
    @MethodSource("loadShaders")
    public void testShaderOutput(String shaderSource) {
        testOutput(shaderSource);

        displayUsedVariables();
        displayUsedFunctions();
    }

    private void displayUsedFunctions() {
        System.out.println("Function usage:");
        final FunctionRegistry functionRegistry = parserContext.getFunctionRegistry();

        for (Usage<FunctionPrototypeNode> nodeUsage : functionRegistry.getUsedFunctions()) {
            final FunctionPrototype prototype = nodeUsage.getTarget().getPrototype();
            System.out.println(String.format("\t%d : %s : %s", nodeUsage.getUsageNodes().size(), nodeUsage.getTarget().getIdentifier().original(), prototype));
        }
    }

    private void displayUsedVariables() {
        System.out.println("Variable usage:");
        final VariableRegistry variableRegistry = parserContext.getVariableRegistry();
        for (Usage<VariableDeclarationNode> nodeUsage : variableRegistry.getAllVariables()) {
            final FullySpecifiedType fullySpecifiedType = nodeUsage.getTarget().getFullySpecifiedType();
            System.out.println(String.format("\t%d : %s : %s", nodeUsage.getUsageNodes().size(), nodeUsage.getTarget().getIdentifier().original(), fullySpecifiedType));
        }
    }

    private Node testOutput(String shader) {
        final Node node = TestUtils.parse(parserContext, Node.class, shader);
        typeChecker.check(parserContext, node);
        return node;
    }
}

package com.tazadum.glsl.optimizer;

import com.tazadum.glsl.exception.TypeException;
import com.tazadum.glsl.language.ast.Identifier;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.language.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.language.function.FunctionRegistry;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.language.type.PredefinedType;
import com.tazadum.glsl.language.type.TypeRegistry;
import com.tazadum.glsl.language.variable.VariableRegistry;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.Usage;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

public class RefCheck {
    private final String id;
    private final int usage;
    private final Type checkType;
    private final PredefinedType predefinedType;

    enum Type {
        Variable,
        Function,
        Type
    }

    public static List<RefCheck> noChecks() {
        return Collections.emptyList();
    }

    public static List<RefCheck> list(RefCheck... checks) {
        return Arrays.asList(checks);
    }

    public static RefCheck variable(String id, int usage) {
        return new RefCheck(id, usage, Type.Variable, null);
    }

    public static RefCheck function(String id, int usage) {
        return new RefCheck(id, usage, Type.Function, null);
    }

    public static RefCheck type(String id, int usage) {
        return new RefCheck(id, usage, Type.Type, null);
    }

    public static RefCheck type(PredefinedType type, int usage) {
        return new RefCheck(null, usage, Type.Type, type);
    }

    public static void runChecks(List<RefCheck> checks, ParserContext parserContext) {
        checks.forEach((check) -> check.runCheck(parserContext));
    }

    RefCheck(String id, int usage, Type checkType, PredefinedType predefinedType) {
        this.id = id;
        this.usage = usage;
        this.checkType = checkType;
        this.predefinedType = predefinedType;
    }

    public void runCheck(ParserContext parserContext) {
        switch (checkType) {
            case Variable:
                assertUniqueVariable(parserContext, id, usage);
                break;
            case Function:
                assertUniqueFunction(parserContext, id, usage);
                break;
            case Type:
                assertUniqueType(parserContext, id, usage, predefinedType);
                break;
        }
    }

    void assertUniqueVariable(ParserContext parserContext, String identifier, int expectedUsageCount) {
        VariableRegistry registry = parserContext.getVariableRegistry();

        boolean found = false;
        for(Usage<VariableDeclarationNode> usage : registry.getAllVariables()) {
            final Identifier id = usage.getTarget().getIdentifier();
            if (identifier.equals(id.original())) {
                if (found) {
                    fail("The variable '" + identifier + "' is declared more than once");
                }
                found = true;

                int usageCount = usage.getUsageNodes().size();
                if (expectedUsageCount != usageCount) {
                    for (Node node : usage.getUsageNodes()) {
                        System.out.println("Usage of " + node + ": " + node.getParentNode());
                    }

                    fail("The variable '" + identifier + "' is used by " + usageCount + " nodes. Expected " + expectedUsageCount + " usages");
                }
            }
        }

        if (!found && expectedUsageCount > 0) {
            fail("The variable '" + identifier + "' is not declared");
        }
    }

    void assertUniqueFunction(ParserContext parserContext, String identifier, int expectedUsageCount) {
        FunctionRegistry registry = parserContext.getFunctionRegistry();

        boolean found = false;
        for(Usage<FunctionPrototypeNode> usage : registry.getUsedFunctions()) {
            final Identifier id = usage.getTarget().getIdentifier();
            if (identifier.equals(id.original())) {
                if (found) {
                    fail("The function '" + identifier + "' is declared more than once");
                }
                found = true;

                int usageCount = usage.getUsageNodes().size();
                if (expectedUsageCount != usageCount) {
                    for (Node node : usage.getUsageNodes()) {
                        System.out.println("Usage of " + node + ": " + node.getParentNode());
                    }

                    fail("The function '" + identifier + "' is used by " + usageCount + " nodes. Expected " + expectedUsageCount + " usages");
                }
            }
        }

        if (!found && expectedUsageCount > 0) {
            fail("The function '" + identifier + "' is not declared");
        }
    }

    void assertUniqueType(ParserContext parserContext, String identifier, int expectedUsageCount, PredefinedType predefinedType) {
        TypeRegistry registry = parserContext.getTypeRegistry();

        try {
            GLSLType glslType = predefinedType == null ? registry.resolve(identifier) : predefinedType;

            Usage<GLSLType> usage = registry.usagesOf(glslType);
            int usageCount = usage.getUsageNodes().size();
            if (expectedUsageCount != usageCount) {
                for (Node node : usage.getUsageNodes()) {
                    System.out.println("Usage of " + node + ": " + node.getParentNode());
                }

                fail("The type '" + identifier + "' is used by " + usageCount + " nodes. Expected " + expectedUsageCount + " usages");
            }

        } catch (TypeException e) {
            if (expectedUsageCount != 0) {
                fail("The type '" + identifier + "' could not be resolved");
            }
        }
    }

}

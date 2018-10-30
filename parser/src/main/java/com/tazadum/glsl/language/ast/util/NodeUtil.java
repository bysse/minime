package com.tazadum.glsl.language.ast.util;


import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.function.FunctionCallNode;
import com.tazadum.glsl.language.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.language.ast.variable.*;
import com.tazadum.glsl.language.model.StorageQualifier;
import com.tazadum.glsl.language.type.TypeQualifierList;
import com.tazadum.glsl.language.variable.VariableRegistry;
import com.tazadum.glsl.parser.Usage;
import com.tazadum.glsl.util.SourcePosition;

import java.util.Set;
import java.util.SortedSet;

/**
 * Created by erikb on 2018-10-13.
 */
public class NodeUtil {
    public static <T extends Node> T cast(Node node) {
        return (T) node;
    }

    public static SourcePosition getSourcePosition(Node node) {
        if (node instanceof ArrayIndexNode) {
            return getSourcePosition(((ArrayIndexNode) node).getExpression());
        }
        if (node instanceof FieldSelectionNode) {
            return getSourcePosition(((FieldSelectionNode) node).getExpression());
        }
        return node.getSourcePosition();
    }


    /**
     * Check if a set of variables are mutated between fromId and toId.
     *
     * @param variables The set of variables to check.
     * @param fromId    The starting id. Ie the id of the intended destination.
     * @param toId      The end id. Ie the id the of current declaration.
     * @return True if the variables are being mutated in the range, otherwise false.
     */
    public static boolean variablesMutated(VariableRegistry variableRegistry, SortedSet<VariableNode> variables, int fromId, int toId) {
        for (VariableNode variable : variables) {
            if (variablesMutated(variableRegistry, variable.getDeclarationNode(), fromId, toId)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check if a variable declaration is mutated between fromId and toId.
     *
     * @param declarationNode The declaration node to check.
     * @param fromId          The starting id. Ie the id of the intended destination.
     * @param toId            The end id. Ie the id the of current declaration.
     * @return True if the variables are being mutated in the range, otherwise false.
     */
    public static boolean variablesMutated(VariableRegistry variableRegistry, VariableDeclarationNode declarationNode, int fromId, int toId) {
        // check if the variable declaration is after the intended target position
        if (fromId < declarationNode.getId()) {
            return true;
        }

        final Usage<VariableDeclarationNode> usage = variableRegistry.resolve(declarationNode);
        final Set<Node> usagesBetween = usage.getUsagesBetween(fromId, toId);

        if (usagesBetween.isEmpty()) {
            // no other usages of the variable in the range
            return false;
        }

        // check all usages of the variable and verify that the variable wasn't mutated
        for (Node usageNode : usagesBetween) {
            if (NodeFinder.isMutated(usageNode)) {
                return true;
            }

            // check if the variable was used as an argument for a function call
            final FunctionCallNode functionCall = NodeFinder.findNearestFunctionCall(usageNode);
            if (functionCall == null) {
                continue;
            }

            // checks if the function can mutate the variable.
            if (functionCallMutates(functionCall, usageNode)) {
                return true;
            }
        }

        return false;
    }


    /**
     * Checks if an argument part of a function can mutate that argument.
     *
     * @param functionCall The function call to check.
     * @param argumentNode The argument passed to the function.
     * @return True if the function call can mutate the argument, otherwise false.
     */
    public static boolean functionCallMutates(FunctionCallNode functionCall, Node argumentNode) {
        final FunctionPrototypeNode functionDeclaration = functionCall.getDeclarationNode();
        if (functionDeclaration.getPrototype().isBuiltIn()) {
            // built-in functions doesn't mutate parameter state
            return false;
        }

        // for a parameter to be modified inside of a function the parameter
        // declaration must have one of the storage qualifiers OUT or INOUT
        for (int i = 0; i < functionCall.getChildCount(); i++) {
            // check if argument i is equal to node
            if (argumentNode.equals(functionCall.getChild(i))) {
                // we found the correct parameter, now check the qualifiers
                final ParameterDeclarationNode parameterDeclaration = functionDeclaration.getChildAs(i);
                final TypeQualifierList qualifiers = parameterDeclaration.getFullySpecifiedType().getQualifiers();

                if (qualifiers.isEmpty()) {
                    break;
                }
                if (qualifiers.contains(StorageQualifier.INOUT) || qualifiers.contains(StorageQualifier.OUT)) {
                    // it's very likely that the variable is modified in one of it's usages since
                    // it's used as an OUT/INOUT argument to a function call in the targeted range
                    return true;
                }
                break;
            }
        }
        return false;
    }

}

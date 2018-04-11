package com.tazadum.glsl.parser.type;

import com.tazadum.glsl.ast.DefaultASTVisitor;
import com.tazadum.glsl.ast.ParentNode;
import com.tazadum.glsl.ast.arithmetic.NumericOperationNode;
import com.tazadum.glsl.ast.conditional.TernaryConditionNode;
import com.tazadum.glsl.ast.expression.AssignmentNode;
import com.tazadum.glsl.ast.function.FunctionCallNode;
import com.tazadum.glsl.ast.function.FunctionDefinitionNode;
import com.tazadum.glsl.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.ast.variable.*;
import com.tazadum.glsl.exception.ParserException;
import com.tazadum.glsl.exception.TypeException;
import com.tazadum.glsl.language.BuiltInType;
import com.tazadum.glsl.language.GLSLType;
import com.tazadum.glsl.language.TypeCategory;
import com.tazadum.glsl.language.TypeQualifier;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.finder.NodeFinder;
import com.tazadum.glsl.parser.function.FunctionPrototypeMatcher;

/**
 * Created by Erik on 2016-10-16.
 */
public class TypeVisitor extends DefaultASTVisitor<GLSLType> {
    private ParserContext parserContext;

    public TypeVisitor(ParserContext parserContext) {
        this.parserContext = parserContext;
    }

    @Override
    public GLSLType visitFieldSelection(FieldSelectionNode node) {
        super.visitFieldSelection(node);

        final GLSLType glslType = node.getExpression().getType();
        if (!(glslType instanceof BuiltInType)) {
            throw ParserException.notSupported("Custom types are not supported");
        }

        final BuiltInType type = (BuiltInType) glslType;
        final GLSLType selectionType = type.fieldType(node.getSelection());

        node.setType(selectionType);
        return selectionType;
    }

    @Override
    public GLSLType visitArrayIndex(ArrayIndexNode node) {
        super.visitArrayIndex(node);

        final GLSLType expressionType = node.getExpression().getType();
        final GLSLType indexType = node.getIndex().getType();

        if (BuiltInType.INT != indexType) {
            throw new TypeException("Array index is not of type int!");
        }

        // TODO: Perform some clever transform of the expressionType to the element type

        return expressionType;
    }

    @Override
    public GLSLType visitFunctionCall(FunctionCallNode node) {
        super.visitFunctionCall(node);

        final GLSLType[] parameterTypes = new GLSLType[node.getChildCount()];
        for (int i = 0; i < node.getChildCount(); i++) {
            parameterTypes[i] = node.getChild(i).getType();
        }

        final FunctionPrototypeMatcher prototypeMatcher = new FunctionPrototypeMatcher(FunctionPrototypeMatcher.ANY, parameterTypes);
        final FunctionPrototypeNode prototypeNode = parserContext.getFunctionRegistry().resolve(node.getIdentifier(), prototypeMatcher);

        if (prototypeNode == null) {
            throw TypeException.incompatibleTypes(node.getIdentifier().original(), prototypeMatcher);
        }

        node.setDeclarationNode(prototypeNode);
        parserContext.getFunctionRegistry().usage(prototypeNode, node);

        return prototypeNode.getReturnType().getType();
    }

    @Override
    public GLSLType visitFunctionDefinition(FunctionDefinitionNode node) {
        super.visitFunctionDefinition(node);

        // try to find out if this function can mutate global state

        // check if the parameters are writable
        for (int i = 0; i < node.getFunctionPrototype().getChildCount(); i++) {
            final ParameterDeclarationNode declarationNode = node.getFunctionPrototype().getChild(i, ParameterDeclarationNode.class);
            final TypeQualifier typeQualifier = declarationNode.getFullySpecifiedType().getQualifier();
            if (typeQualifier != null && (typeQualifier == TypeQualifier.OUT || typeQualifier == TypeQualifier.INOUT)) {
                node.setMutatesGlobalState(true);
                node.setUsesGlobalState(false);
                return null;
            }
        }

        // check if the body contains calls to other functions that mutates state
        for (FunctionCallNode functionCallNode : NodeFinder.findAll(node.getStatements(), FunctionCallNode.class)) {
            if (functionCallNode.getDeclarationNode().getPrototype().isBuiltIn()) {
                continue;
            }

            final ParentNode functionDefinition = functionCallNode.getDeclarationNode().getParentNode();
            if (functionDefinition instanceof FunctionDefinitionNode) {
                final FunctionDefinitionNode definition = (FunctionDefinitionNode) functionDefinition;
                if (definition.mutatesGlobalState()) {
                    node.setMutatesGlobalState(true);
                    node.setUsesGlobalState(false);
                    return null;
                }
                if (!definition.usesGlobalState()) {
                    node.setUsesGlobalState(false);
                }
            }
        }

        // check if the body contains global variable mutations
        for (VariableNode variableNode : NodeFinder.findAll(node.getStatements(), VariableNode.class)) {
            final VariableDeclarationNode declarationNode = variableNode.getDeclarationNode();
            if (declarationNode.isBuiltIn()) {
                continue;
            }
            // check if this is a global variable
            if (parserContext.globalContext().equals(parserContext.findContext(declarationNode))) {
                node.setUsesGlobalState(false);
                if (NodeFinder.isMutated(variableNode)) {
                    node.setMutatesGlobalState(true);
                    return null;
                }
            }
        }

        return null;
    }

    @Override
    public GLSLType visitAssignment(AssignmentNode node) {
        super.visitAssignment(node);

        // TODO: verify assignment types

        return null;
    }

    @Override
    public GLSLType visitTernaryCondition(TernaryConditionNode node) {
        super.visitTernaryCondition(node);

        final GLSLType thenType = node.getThen().getType();
        final GLSLType elseType = node.getElse().getType();

        if (!thenType.isAssignableBy(elseType)) {
            throw TypeException.incompatibleTypes(thenType.token(), elseType.token());
        }

        return thenType;
    }

    @Override
    public GLSLType visitNumericOperation(NumericOperationNode node) {
        super.visitNumericOperation(node);

        final BuiltInType left = (BuiltInType) node.getLeft().getType();
        final BuiltInType right = (BuiltInType) node.getRight().getType();

        if (left.category() == TypeCategory.Special || right.category() == TypeCategory.Special) {
            throw TypeException.types(left, right, " are not compatible together in an arithmetic operation!");
        }

        final GLSLType type = getTypeCombination(left, right);
        node.setType(type);
        return type;
    }

    private GLSLType getTypeCombination(BuiltInType left, BuiltInType right) {
        switch (left.category()) {
            case Scalar:
                switch (right.category()) {
                    case Scalar:
                        return floatOrInt(left, right);
                    case Vector:
                        return right;
                    case Matrix:
                        return right;
                }
                break;
            case Vector:
                switch (right.category()) {
                    case Scalar:
                        return left;
                    case Vector:
                        return left;
                    case Matrix:
                        return left;
                }
                break;
            case Matrix:
                switch (right.category()) {
                    case Scalar:
                        return left;
                    case Vector:
                        return right;
                    case Matrix:
                        if (left != right) throw TypeException.types(left, right, " are not compatible!");
                        return left;
                }
                break;
        }
        throw TypeException.types(left, right, " are not compatible together in arithmetic operations!");
    }

    private GLSLType floatOrInt(BuiltInType left, BuiltInType right) {
        if (left == BuiltInType.FLOAT || right == BuiltInType.FLOAT) {
            return BuiltInType.FLOAT;
        }
        return BuiltInType.INT;
    }
}

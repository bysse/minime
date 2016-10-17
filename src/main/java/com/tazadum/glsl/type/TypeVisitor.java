package com.tazadum.glsl.type;

import com.tazadum.glsl.ast.*;
import com.tazadum.glsl.ast.arithmetic.*;
import com.tazadum.glsl.ast.conditional.*;
import com.tazadum.glsl.ast.expression.AssignmentNode;
import com.tazadum.glsl.ast.expression.ConstantExpressionNode;
import com.tazadum.glsl.ast.function.FunctionCallNode;
import com.tazadum.glsl.ast.function.FunctionDefinitionNode;
import com.tazadum.glsl.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.ast.iteration.DoWhileIterationNode;
import com.tazadum.glsl.ast.iteration.ForIterationNode;
import com.tazadum.glsl.ast.iteration.WhileIterationNode;
import com.tazadum.glsl.ast.logical.BooleanLeafNode;
import com.tazadum.glsl.ast.logical.LogicalOperationNode;
import com.tazadum.glsl.ast.logical.RelationalOperationNode;
import com.tazadum.glsl.ast.variable.*;
import com.tazadum.glsl.exception.ParserException;
import com.tazadum.glsl.exception.TypeException;
import com.tazadum.glsl.language.BuiltInType;
import com.tazadum.glsl.language.GLSLType;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.function.FunctionPrototypeMatcher;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Erik on 2016-10-16.
 */
public class TypeVisitor implements ASTVisitor<GLSLType> {
    private ParserContext parserContext;
    private Map<Node, GLSLType> map;

    public Map<Node, GLSLType> getLookup() {
        return map;
    }

    public TypeVisitor(ParserContext parserContext) {
        this.parserContext = parserContext;
        this.map = new HashMap<>();
    }

    @Override
    public GLSLType visitBoolean(BooleanLeafNode node) {
        map.put(node, BuiltInType.BOOL);
        return BuiltInType.BOOL;
    }

    @Override
    public GLSLType visitStatementList(StatementListNode node) {
        visitChildren(node);
        return null;
    }

    @Override
    public GLSLType visitParenthesis(ParenthesisNode node) {
        final GLSLType type = node.getExpression().accept(this);
        map.put(node, type);
        return type;
    }

    @Override
    public GLSLType visitVariable(VariableNode node) {
        final GLSLType type = node.getDeclarationNode().getFullySpecifiedType().getType();
        map.put(node, type);
        return type;
    }

    @Override
    public GLSLType visitVariableDeclaration(VariableDeclarationNode node) {
        final GLSLType type = node.getFullySpecifiedType().getType();
        map.put(node, type);
        return type;
    }

    @Override
    public GLSLType visitVariableDeclarationList(VariableDeclarationListNode node) {
        final GLSLType type = node.getFullySpecifiedType().getType();
        map.put(node, type);
        return type;
    }

    @Override
    public GLSLType visitPrecision(PrecisionDeclarationNode node) {
        return null;
    }

    @Override
    public GLSLType visitParameterDeclaration(ParameterDeclarationNode node) {
        final GLSLType type = node.getFullySpecifiedType().getType();
        map.put(node, type);
        return type;
    }

    @Override
    public GLSLType visitFieldSelection(FieldSelectionNode node) {
        final GLSLType glslType = node.getExpression().accept(this);
        if (!(glslType instanceof BuiltInType)) {
            throw ParserException.notSupported("Custom types are not supported");
        }

        final BuiltInType type = (BuiltInType) glslType;
        final GLSLType fieldType = type.fieldType(node.getSelection());

        map.put(node, fieldType);
        return fieldType;
    }

    @Override
    public GLSLType visitArrayIndex(ArrayIndexNode node) {
        GLSLType expressionType = node.getExpression().accept(this);
        GLSLType indexType = node.getIndex().accept(this);

        if (BuiltInType.INT != indexType) {
            throw new TypeException("Array index is not of type int!");
        }

        // TODO: Perform some clever transform of the expressionType to the element type

        map.put(node, expressionType);
        return expressionType;
    }

    @Override
    public GLSLType visitRelationalOperation(RelationalOperationNode node) {
        visitChildren(node);
        map.put(node, BuiltInType.BOOL);
        return BuiltInType.BOOL;
    }

    @Override
    public GLSLType visitLogicalOperation(LogicalOperationNode node) {
        visitChildren(node);
        map.put(node, BuiltInType.BOOL);
        return BuiltInType.BOOL;
    }

    @Override
    public GLSLType visitWhileIteration(WhileIterationNode node) {
        visitChildren(node);
        return null;
    }

    @Override
    public GLSLType visitForIteration(ForIterationNode node) {
        visitChildren(node);
        return null;
    }

    @Override
    public GLSLType visitDoWhileIteration(DoWhileIterationNode node) {
        return null;
    }

    @Override
    public GLSLType visitFunctionPrototype(FunctionPrototypeNode node) {
        visitChildren(node);
        return null;
    }

    @Override
    public GLSLType visitFunctionDefinition(FunctionDefinitionNode node) {
        // function definitions has no type
        return null;
    }

    @Override
    public GLSLType visitFunctionCall(FunctionCallNode node) {
        final GLSLType[] parameterTypes = new GLSLType[node.getChildCount()];
        for (int i = 0; i < node.getChildCount(); i++) {
            parameterTypes[i] = node.getChild(i).accept(this);
        }

        final FunctionPrototypeMatcher prototypeMatcher = new FunctionPrototypeMatcher(FunctionPrototypeMatcher.ANY, parameterTypes);
        final FunctionPrototypeNode prototypeNode = parserContext.getFunctionRegistry().resolve(node.getIdentifier(), prototypeMatcher);

        if (prototypeNode == null) {
            throw TypeException.incompatibleTypes(node.getIdentifier().original(), prototypeMatcher);
        }

        node.setDeclarationNode(prototypeNode);
        parserContext.getFunctionRegistry().usage(prototypeNode, node);

        final GLSLType returnType = prototypeNode.getReturnType().getType();
        map.put(node, returnType);
        return returnType;
    }

    @Override
    public GLSLType visitConstantExpression(ConstantExpressionNode node) {
        GLSLType type = node.getExpression().accept(this);
        map.put(node, type);
        return type;
    }

    @Override
    public GLSLType visitAssignment(AssignmentNode node) {
        visitChildren(node);
        return null;
    }

    @Override
    public GLSLType visitTernaryCondition(TernaryConditionNode node) {
        node.getCondition().accept(this);

        final GLSLType thenType = node.getThen().accept(this);
        final GLSLType elseType = node.getElse().accept(this);

        if (!thenType.isAssignableBy(elseType)) {
            throw TypeException.incompatibleTypes(thenType.token(), elseType.token());
        }

        map.put(node, thenType);
        return thenType;
    }

    @Override
    public GLSLType visitReturn(ReturnNode node) {
        if (node.getExpression() == null) {
            map.put(node, BuiltInType.VOID);
            return BuiltInType.VOID;
        }

        GLSLType type = node.getExpression().accept(this);
        map.put(node, type);
        return type;
    }

    @Override
    public GLSLType visitDiscard(DiscardLeafNode node) {
        return null;
    }

    @Override
    public GLSLType visitContinue(ContinueLeafNode node) {
        return null;
    }

    @Override
    public GLSLType visitCondition(ConditionNode node) {
        visitChildren(node);
        return null;
    }

    @Override
    public GLSLType visitBreak(BreakLeafNode node) {
        return null;
    }

    @Override
    public GLSLType visitUnaryOperation(UnaryOperationNode node) {
        // TODO: implement
        return null;
    }

    @Override
    public GLSLType visitPrefixOperation(PrefixOperationNode node) {
        // TODO: implement
        return null;
    }

    @Override
    public GLSLType visitPostfixOperation(PostfixOperationNode node) {
        // TODO: implement
        return null;
    }

    @Override
    public GLSLType visitNumericOperation(NumericOperationNode node) {
        // TODO: implement
        return null;
    }

    @Override
    public GLSLType visitInt(IntLeafNode node) {
        map.put(node, BuiltInType.INT);
        return BuiltInType.INT;
    }

    @Override
    public GLSLType visitFloat(FloatLeafNode node) {
        map.put(node, BuiltInType.FLOAT);
        return BuiltInType.FLOAT;
    }


    private <T extends ParentNode> String visitChildren(T node) {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < node.getChildCount(); i++) {
            builder.append(node.getChild(i).accept(this));
        }
        return builder.toString();
    }
}

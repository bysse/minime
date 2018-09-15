package com.tazadum.glsl.util;

import com.tazadum.glsl.ast.DefaultASTVisitor;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.ParentNode;
import com.tazadum.glsl.ast.arithmetic.FloatLeafNode;
import com.tazadum.glsl.ast.arithmetic.IntLeafNode;
import com.tazadum.glsl.ast.conditional.BreakLeafNode;
import com.tazadum.glsl.ast.conditional.ContinueLeafNode;
import com.tazadum.glsl.ast.conditional.DiscardLeafNode;
import com.tazadum.glsl.ast.conditional.ReturnNode;
import com.tazadum.glsl.ast.logical.BooleanLeafNode;
import com.tazadum.glsl.ast.variable.VariableDeclarationListNode;
import com.tazadum.glsl.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.ast.variable.VariableNode;

/**
 * Created by Erik on 2016-10-24.
 */
public class IdVisitor extends DefaultASTVisitor<Void> {
    String indentation = "";

    public void reset() {
        indentation = "";
    }

    @Override
    public Void visitBoolean(BooleanLeafNode node) {
        return output(node);
    }

    @Override
    public Void visitVariableDeclaration(VariableDeclarationNode node) {
        System.out.format(indentation + "id=%d: %s(%s)\n", node.getId(), node.getClass().getSimpleName(), node.getIdentifier().original());
        super.visitChildren(node);
        return null;
    }

    @Override
    public Void visitVariable(VariableNode node) {
        VariableDeclarationNode declarationNode = node.getDeclarationNode();
        if (declarationNode == null) {
            System.out.format(indentation + "id=%d: %s(null)\n", node.getId(), node.getClass().getSimpleName());
        } else {
            System.out.format(indentation + "id=%d: %s(%s)\n", node.getId(), node.getClass().getSimpleName(), declarationNode.getIdentifier().original());
        }
        return null;
    }

    @Override
    public Void visitReturn(ReturnNode node) {
        return output(node);
    }

    @Override
    public Void visitDiscard(DiscardLeafNode node) {
        return output(node);
    }

    @Override
    public Void visitContinue(ContinueLeafNode node) {
        return output(node);
    }

    @Override
    public Void visitBreak(BreakLeafNode node) {
        return output(node);
    }

    @Override
    public Void visitInt(IntLeafNode node) {
        return output(node);
    }

    @Override
    public Void visitFloat(FloatLeafNode node) {
        return output(node);
    }

    @Override
    public Void visitVariableDeclarationList(VariableDeclarationListNode node) {
        System.out.format(indentation + "id=%d: %s(%s)\n", node.getId(), node.getClass().getSimpleName(), node.getType());
        return super.visitVariableDeclarationList(node);
    }

    @Override
    protected <T extends ParentNode> void visitChildren(T node) {
        output(node);
        String old = indentation;
        indentation += " ";
        super.visitChildren(node);
        indentation = old;
    }

    private Void output(Node node) {
        System.out.format(indentation + "id=%d: %s\n", node.getId(), node.getClass().getSimpleName());
        return null;
    }
}

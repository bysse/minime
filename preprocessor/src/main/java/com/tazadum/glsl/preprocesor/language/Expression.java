package com.tazadum.glsl.preprocesor.language;

import com.tazadum.glsl.preprocesor.language.ast.expression.*;

public interface Expression extends Node {
    void accept(Visitor visitor);

    interface Visitor {
        void visit(IntegerNode node);

        void visit(IdentifierNode node);

        void visit(DefinedNode node);

        void visit(UnaryExpressionNode node);

        void visit(BinaryExpressionNode node);

        void visit(ComparativeExpressionNode node);

        void visit(OrExpressionNode node);

        void visit(AndExpressionNode node);

        default void visit(ParenthesisNode node) {
            node.getExpression().accept(this);
        }
    }
}

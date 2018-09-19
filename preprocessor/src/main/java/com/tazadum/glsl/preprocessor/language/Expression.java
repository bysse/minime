package com.tazadum.glsl.preprocessor.language;

import com.tazadum.glsl.preprocessor.language.ast.expression.*;

public interface Expression extends Node {
    void accept(Visitor visitor);

    interface Visitor {
        void visit(IntegerNode node);

        void visit(IdentifierNode node);

        void visit(DefinedNode node);

        void visit(UnaryExpressionNode node);

        void visit(BinaryExpressionNode node);

        void visit(RelationalExpressionNode node);

        void visit(OrExpressionNode node);

        void visit(AndExpressionNode node);

        default void visit(ParenthesisNode node) {
            node.getExpression().accept(this);
        }
    }
}

package com.tazadum.glsl.preprocessor.language;

import com.tazadum.glsl.preprocessor.language.ast.expression.*;

public interface Expression extends Node {
    int accept(Visitor visitor);

    interface Visitor {
        int visit(IntegerNode node);

        int visit(IdentifierNode node);

        int visit(DefinedNode node);

        int visit(UnaryExpressionNode node);

        int visit(BinaryExpressionNode node);

        int visit(RelationalExpressionNode node);

        int visit(OrExpressionNode node);

        int visit(AndExpressionNode node);

        default int visit(ParenthesisNode node) {
            return node.getExpression().accept(this);
        }
    }
}

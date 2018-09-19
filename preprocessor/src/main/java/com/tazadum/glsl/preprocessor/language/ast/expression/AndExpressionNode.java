package com.tazadum.glsl.preprocessor.language.ast.expression;

import com.tazadum.glsl.preprocessor.language.Expression;
import com.tazadum.glsl.preprocessor.language.ast.BaseNode;
import com.tazadum.glsl.util.SourcePosition;

/**
 * Node for all types of binary numeric expressions.
 */
public class AndExpressionNode extends BaseNode implements Expression {
    private Expression left;
    private Expression right;

    public AndExpressionNode(SourcePosition sourcePosition, Expression left, Expression right) {
        super(sourcePosition);
        this.left = left;
        this.right = right;
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    @Override
    public int accept(Visitor visitor) {
        return visitor.visit(this);
    }

    public String toString() {
        return left + " && " + right;
    }
}

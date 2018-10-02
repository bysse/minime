package com.tazadum.glsl.preprocessor.language.ast.expression;

import com.tazadum.glsl.preprocessor.language.Expression;
import com.tazadum.glsl.preprocessor.language.ast.BaseNode;
import com.tazadum.glsl.util.SourcePositionId;

/**
 * Node for all types of binary numeric expressions.
 */
public class OrExpressionNode extends BaseNode implements Expression {
    private Expression left;
    private Expression right;

    public OrExpressionNode(SourcePositionId sourcePosition, Expression left, Expression right) {
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
        return left + " || " + right;
    }
}

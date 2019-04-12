package com.tazadum.glsl.language.ast.conditional;

import com.tazadum.glsl.language.ast.*;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.context.GLSLContext;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.util.SourcePosition;

/**
 * Created by Erik on 2016-10-07.
 */
public class ConditionNode extends FixedChildParentNode {
    public ConditionNode(SourcePosition position, Node condition, Node thenNode, Node elseNode) {
        this(position, null, condition, thenNode, elseNode);
    }

    public ConditionNode(SourcePosition position, ParentNode parentNode, Node condition, Node thenNode, Node elseNode) {
        super(position, 3, parentNode);

        setChild(0, condition);
        setChild(1, thenNode);
        setChild(2, elseNode);
    }

    public Node getCondition() {
        return getChild(0);
    }

    public Node getThen() {
        return getChild(1);
    }

    public Node getElse() {
        return getChild(2);
    }

    @Override
    public ParentNode clone(ParentNode newParent) {
        final ConditionNode node = new ConditionNode(getSourcePosition(), newParent, null, null, null);
        return CloneUtils.cloneChildren(this, node);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitCondition(this);
    }

    @Override
    public GLSLType getType() {
        return getThen().getType();
    }

    public String toString() {
        String base = "if (" + getCondition() + ") " + getThen() + " ";
        if (getElse() != null) {
            base += " else " + getElse() + " ";
        }
        return base;
    }
}

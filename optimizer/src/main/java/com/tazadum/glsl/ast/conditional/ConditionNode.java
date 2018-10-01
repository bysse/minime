package com.tazadum.glsl.ast.conditional;

import com.tazadum.glsl.ast.ASTVisitor;
import com.tazadum.glsl.ast.FixedChildParentNode;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.ParentNode;
import com.tazadum.glsl.language.GLSLType;
import com.tazadum.glsl.util.CloneUtils;

/**
 * Created by Erik on 2016-10-07.
 */
public class ConditionNode extends FixedChildParentNode {
    public ConditionNode(Node condition, Node thenNode, Node elseNode) {
        this(null, condition, thenNode, elseNode);
    }

    public ConditionNode(ParentNode parentNode, Node condition, Node thenNode, Node elseNode) {
        super(3, parentNode);
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
        final ConditionNode node = new ConditionNode(newParent, null, null, null);
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
}
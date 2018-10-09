package com.tazadum.glsl.language.ast.conditional;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.FixedChildParentNode;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.type.GLSLType;

/**
 * Created by Erik on 2016-10-07.
 */
public class TernaryConditionNode extends FixedChildParentNode {
    public TernaryConditionNode(Node condition, Node thenNode, Node elseNode) {
        this(null, condition, thenNode, elseNode);
    }

    public TernaryConditionNode(ParentNode parentNode, Node condition, Node thenNode, Node elseNode) {
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
        final TernaryConditionNode node = new TernaryConditionNode(newParent, null, null, null);
        for (int i = 0; i < 3; i++) {
            node.setChild(i, CloneUtils.clone(getChild(i), node));
        }
        return node;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitTernaryCondition(this);
    }

    @Override
    public GLSLType getType() {
        return getThen().getType();
    }
}

package com.tazadum.glsl.language.ast;

import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.type.GLSLType;

public class ParenthesisNode extends FixedChildParentNode {
    public ParenthesisNode(Node node) {
        super(1);
        setChild(0, node);
    }

    private ParenthesisNode(ParentNode parentNode, Node node) {
        super(1, parentNode);
        setChild(0, node);
    }

    public void setExpression(Node node) {
        setChild(0, node);
    }

    public Node getExpression() {
        return getChild(0);
    }

    @Override
    public ParentNode clone(ParentNode newParent) {
        ParenthesisNode node = new ParenthesisNode(newParent, null);
        node.setExpression(CloneUtils.clone(getExpression(), node));
        return node;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitParenthesis(this);
    }

    @Override
    public GLSLType getType() {
        return getExpression().getType();
    }
}

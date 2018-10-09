package com.tazadum.glsl.language.ast;

import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.util.SourcePositionId;

public class ParenthesisNode extends FixedChildParentNode {
    public ParenthesisNode(SourcePositionId position, Node node) {
        super(position, 1);
        setChild(0, node);
    }

    private ParenthesisNode(SourcePositionId position, ParentNode parentNode, Node node) {
        super(position, 1, parentNode);
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
        ParenthesisNode node = new ParenthesisNode(getSourcePositionId(), newParent, null);
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

package com.tazadum.glsl.language.ast;

import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.type.GLSLType;

public class ParenthesisNode extends FixedChildParentNode {
    public ParenthesisNode(SourcePosition position, Node node) {
        super(position, 1);
        setChild(0, node);
    }

    private ParenthesisNode(SourcePosition position, ParentNode parentNode, Node node) {
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
        ParenthesisNode node = new ParenthesisNode(getSourcePosition(), newParent, null);
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

package com.tazadum.glsl.ast;

import com.tazadum.glsl.language.GLSLType;
import com.tazadum.glsl.util.CloneUtils;

public class ParenthesisNode extends FixedChildParentNode {
    public ParenthesisNode(Node node) {
        super(1);
        setChild(0, node);
    }

    private ParenthesisNode(ParentNode parentNode, Node node) {
        super(1, parentNode);
        setChild(0, node);
    }


    public Node getExpression() {
        return nodes[0];
    }

    @Override
    public ParentNode clone(ParentNode newParent) {
        return new ParenthesisNode(newParent, CloneUtils.clone(getChild(0)));
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

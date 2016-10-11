package com.tazadum.glsl.ast.conditional;

import com.tazadum.glsl.ast.FixedChildParentNode;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.ParentNode;
import com.tazadum.glsl.util.CloneUtils;

public class ReturnNode extends FixedChildParentNode {

    public ReturnNode() {
        this(null);
    }

    public ReturnNode(ParentNode parentNode) {
        super(1, parentNode);
    }

    public void setExpression(Node expression) {
        setChild(0, expression);
    }

    public Node getExpression() {
        return getChild(0);
    }

    public boolean hasExpression() {
        return getExpression() != null;
    }

    @Override
    public ReturnNode clone(ParentNode newParent) {
        return CloneUtils.cloneChildren(this, new ReturnNode(newParent));
    }
}

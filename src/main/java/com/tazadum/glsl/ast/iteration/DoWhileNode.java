package com.tazadum.glsl.ast.iteration;

import com.tazadum.glsl.ast.FixedChildParentNode;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.ParentNode;
import com.tazadum.glsl.util.CloneUtils;

public class DoWhileNode extends FixedChildParentNode {
    public DoWhileNode() {
        this(null);
    }

    public DoWhileNode(ParentNode parentNode) {
        super(2, parentNode);
    }

    public void setCondition(Node condition) {
        setChild(0, condition);
    }

    public Node getCondition() {
        return getChild(0);
    }

    public Node getStatement() {
        return getChild(1);
    }

    public void setStatement(Node statement) {
        setChild(1, statement);
    }

    @Override
    public ParentNode clone(ParentNode newParent) {
        return CloneUtils.cloneChildren(this, new DoWhileNode(newParent));
    }
}

package com.tazadum.glsl.ast.iteration;

import com.tazadum.glsl.ast.FixedChildParentNode;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.ParentNode;
import com.tazadum.glsl.util.CloneUtils;

public class ForIterationNode extends FixedChildParentNode {
    public ForIterationNode() {
        this(null);
    }

    public ForIterationNode(ParentNode parentNode) {
        super(3, parentNode);
    }

    public void setInitialization(Node initialization) {
        setChild(0, initialization);
    }

    public Node getInitialization() {
        return getChild(0);
    }

    public Node getCondition() {
        return getChild(1);
    }

    public void setCondition(Node condition) {
        setChild(1, condition);
    }

    public Node getExpression() {
        return getChild(2);
    }

    public void setExpression(Node expression) {
        setChild(2, expression);
    }

    @Override
    public ParentNode clone(ParentNode newParent) {
        return CloneUtils.cloneChildren(this, new ForIterationNode(newParent));
    }
}

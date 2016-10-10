package com.tazadum.glsl.ast.conditional;

import com.tazadum.glsl.ast.FixedChildParentNode;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.ParentNode;
import com.tazadum.glsl.util.CloneUtils;

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

    @Override
    public ParentNode clone(ParentNode newParent) {
        Node condition = CloneUtils.clone(getChild(0));
        Node thenNode = CloneUtils.clone(getChild(1));
        Node elseNode = CloneUtils.clone(getChild(2));
        return new TernaryConditionNode(newParent, condition, thenNode, elseNode);
    }
}

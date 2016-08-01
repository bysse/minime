package com.tazadum.glsl.ast;

import java.util.ArrayList;
import java.util.List;

public abstract class FixedChildParentNode extends ParentNode {
    abstract protected Node[] getChildNodes();

    @Override
    public ParentNode addChild(Node node) {
        throw new UnsupportedOperationException("addChild is not supported");
    }

    @Override
    public ParentNode addChild(Node node, int index) {
        throw new UnsupportedOperationException("addChild is not supported");
    }

    @Override
    public ParentNode clone(ParentNode newParent) {
        throw new UnsupportedOperationException("clone is not supported");
    }

    @Override
    public int getChildCount() {
        int count = 0;
        for (Node node : getChildNodes()) {
            if (node != null) {
                count++;
            }
        }
        return count;
    }

    @Override
    public Iterable<Node> getChildren() {
        List<Node> list = new ArrayList<>();
        for (Node node : getChildNodes()) {
            if (node != null) {
                list.add(node);
            }
        }
        return list;

    }

    @Override
    public ParentNode removeChild(Node node) {
        throw new UnsupportedOperationException("removeChild is not supported");
    }
}

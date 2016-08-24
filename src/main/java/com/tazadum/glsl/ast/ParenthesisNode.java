package com.tazadum.glsl.ast;

public class ParenthesisNode extends FixedChildParentNode {
    private Node[] nodes;

    public ParenthesisNode(Node node) {
        this.nodes = new Node[]{node};
    }

    @Override
    protected Node[] getChildNodes() {
        return nodes;
    }

    public Node getExpression() {
        return nodes[0];
    }
}

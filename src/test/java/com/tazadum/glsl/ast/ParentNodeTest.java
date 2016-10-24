package com.tazadum.glsl.ast;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ParentNodeTest {
    @Test
    public void testId() {
        ParentNode root = new ParentNode();

        Node a = new LeafNode(root);
        Node b = new LeafNode(root);
        Node c = new LeafNode(root);

        ParentNode child = new ParentNode(root);
        Node d = new LeafNode(child);
        Node e = new LeafNode(child);

        ParentNode child2 = new ParentNode(root);
        Node f = new LeafNode(child2);
        Node g = new LeafNode(child2);

        root.addChild(a)
                .addChild(b)
                .addChild(child)
                .addChild(c);

        child2.addChild(f)
                .addChild(g);

        child.addChild(d)
                .addChild(e)
                .addChild(child2);

        output(root, "");

        assertEquals(1, root.getId());
        assertEquals(2, a.getId());
        assertEquals(3, b.getId());
        assertEquals(4, child.getId());
        assertEquals(5, d.getId());
        assertEquals(6, e.getId());
        assertEquals(10, c.getId());

        Node h = new LeafNode(root);
        root.setChild(0, h);

        assertEquals(10, c.getId());
        assertEquals(8, f.getId());
        assertEquals(2, h.getId());
    }

    private void output(ParentNode node, String indentation) {
        System.out.format(indentation + "id=%d: %s\n", node.getId(), node.getClass().getSimpleName());
        indentation += "  ";

        for (int i = 0; i < node.getChildCount(); i++) {
            Node child = node.getChild(i);
            if (child instanceof ParentNode) {
                output((ParentNode) child, indentation);
            } else {
                System.out.format(indentation + "id=%d: %s\n", child.getId(), child.getClass().getSimpleName());
            }
        }
    }
}

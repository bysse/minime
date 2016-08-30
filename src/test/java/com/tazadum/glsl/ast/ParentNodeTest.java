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

        root.addChild(a)
            .addChild(b)
            .addChild(child)
            .addChild(c);

        child.addChild(d)
            .addChild(e);

        assertEquals(1, root.getId());
        assertEquals(2, a.getId());
        assertEquals(3, b.getId());
        assertEquals(4, child.getId());
        assertEquals(5, d.getId());
        assertEquals(6, e.getId());
        assertEquals(7, c.getId());

        Node f = new LeafNode(root);
        root.addChild(f, 0);

        assertEquals(8, c.getId());
    }
}

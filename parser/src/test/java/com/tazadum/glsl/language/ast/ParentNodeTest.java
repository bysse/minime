package com.tazadum.glsl.language.ast;

import org.junit.jupiter.api.Test;

import static com.tazadum.glsl.util.SourcePositionId.DEFAULT;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParentNodeTest {
    @Test
    public void testId() {
        ParentNode root = new ParentNode(DEFAULT);

        Node a = new LeafNode(DEFAULT, root);
        Node b = new LeafNode(DEFAULT, root);
        Node c = new LeafNode(DEFAULT, root);

        ParentNode child = new ParentNode(DEFAULT, root);
        Node d = new LeafNode(DEFAULT, child);
        Node e = new LeafNode(DEFAULT, child);

        ParentNode child2 = new ParentNode(DEFAULT, root);
        Node f = new LeafNode(DEFAULT, child2);
        Node g = new LeafNode(DEFAULT, child2);

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

        Node h = new LeafNode(DEFAULT, root);
        root.setChild(0, h);

        assertEquals(10, c.getId());
        assertEquals(8, f.getId());
        assertEquals(2, h.getId());
    }

    @Test
    public void testId_2() {
        ParentNode root = new ParentNode(DEFAULT), child1 = new ParentNode(DEFAULT), child2 = new ParentNode(DEFAULT);
        Node a = new LeafNode(DEFAULT), b = new LeafNode(DEFAULT), c = new LeafNode(DEFAULT), d = new LeafNode(DEFAULT);

        root.addChild(child1);
        root.addChild(child2);
        child1.addChild(a);
        child1.addChild(b);
        child2.addChild(c);
        child2.addChild(d);

        assertEquals(3, a.getId());
        assertEquals(6, c.getId());

        output(root, "");

        child2.addChild(a);

        output(root, "");

        assertEquals(7, a.getId());
        assertEquals(5, c.getId());
    }

    private void output(ParentNode node, String indentation) {
        System.out.format(indentation + "id=%d: %s\n", node.getId(), node.getClass().getSimpleName());
        indentation += "  ";

        for (int i = 0; i < node.getChildCount(); i++) {
            Node child = node.getChild(i);
            if (child instanceof ParentNode) {
                output((ParentNode) child, indentation);
            } else {
                System.out.format(indentation + "id=%d: %s [0x%08x]\n", child.getId(), child.getClass().getSimpleName(), System.identityHashCode(child));
            }
        }
    }
}

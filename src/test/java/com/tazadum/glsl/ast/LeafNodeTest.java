package com.tazadum.glsl.ast;

import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;

public class LeafNodeTest {
    @Test
    public void testGetId() {
        assertEquals(1, new LeafNode().getId());
    }

    @Test
    public void testClone() {
        ParentNode root = new ParentNode();
        LeafNode leaf = new LeafNode(root);
        root.addChild(leaf);

        LeafNode clone = leaf.clone(null);

        assertEquals(null, clone.getParentNode());
        assertFalse(leaf.equals(clone));
    }
}
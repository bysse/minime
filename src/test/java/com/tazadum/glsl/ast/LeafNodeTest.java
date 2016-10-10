package com.tazadum.glsl.ast;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LeafNodeTest {
    @Test
    public void testGetId() {
        assertEquals(1, new LeafNode().getId());
    }
}
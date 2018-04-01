package com.tazadum.glsl.ast;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class LeafNodeTest {
    @Test
    public void testGetId() {
        assertEquals(1, new LeafNode().getId());
    }

}
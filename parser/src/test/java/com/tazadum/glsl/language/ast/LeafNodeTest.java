package com.tazadum.glsl.language.ast;

import com.tazadum.glsl.util.SourcePosition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class LeafNodeTest {
    @Test
    void testGetId() {
        assertEquals(1, new LeafNode(SourcePosition.TOP).getId());
    }
}

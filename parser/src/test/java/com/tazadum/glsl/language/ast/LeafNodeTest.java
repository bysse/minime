package com.tazadum.glsl.language.ast;

import com.tazadum.glsl.util.SourcePositionId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class LeafNodeTest {
    @Test
    void testGetId() {
        assertEquals(1, new LeafNode(SourcePositionId.DEFAULT).getId());
    }
}

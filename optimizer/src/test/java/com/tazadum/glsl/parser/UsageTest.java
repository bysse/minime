package com.tazadum.glsl.parser;

import com.tazadum.glsl.language.Numeric;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.StatementListNode;
import com.tazadum.glsl.language.ast.arithmetic.IntLeafNode;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class UsageTest {
    @Test
    public void testRemapping() throws Exception {
        StatementListNode oldRoot = new StatementListNode();
        IntLeafNode oldLeafNode = new IntLeafNode(new Numeric(2.0, 0, false));
        oldRoot.addChild(new IntLeafNode(new Numeric(1.0, 0, false)));
        oldRoot.addChild(oldLeafNode);
        oldRoot.addChild(new IntLeafNode(new Numeric(3.0, 0, false)));

        StatementListNode newRoot = new StatementListNode();
        IntLeafNode newLeafNode = new IntLeafNode(new Numeric(2.0, 0, false));
        newRoot.addChild(new IntLeafNode(new Numeric(1.0, 0, false)));
        newRoot.addChild(newLeafNode);
        newRoot.addChild(new IntLeafNode(new Numeric(3.0, 0, false)));


        final Usage<Boolean> usage = new Usage<>(Boolean.TRUE);
        usage.add(oldLeafNode);
        final Usage<Boolean> usage2 = usage.remap(newRoot);
        final List<Node> nodes = usage2.getUsageNodes();

        assertEquals(1, nodes.size());
        assertTrue(Objects.equals(newLeafNode, nodes.get(0)));
        assertFalse(Objects.equals(oldLeafNode, nodes.get(0)));
    }
}

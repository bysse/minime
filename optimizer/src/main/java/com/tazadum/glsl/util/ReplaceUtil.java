package com.tazadum.glsl.util;

import com.tazadum.glsl.ast.LeafNode;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.ParentNode;
import com.tazadum.glsl.ast.ReplacingASTVisitor;
import com.tazadum.glsl.ast.arithmetic.FloatLeafNode;
import com.tazadum.glsl.ast.arithmetic.IntLeafNode;
import com.tazadum.glsl.language.Numeric;
import com.tazadum.glsl.parser.ParserContext;

/**
 * Created by erikb on 2018-03-26.
 */
public class ReplaceUtil {
    public static <T extends ParentNode> T removeNumeric(ParserContext context, T parent, LeafNode remove) {
        ReplacingASTVisitor visitor = new ReplacingASTVisitor(context, false) {
            @Override
            public Node visitInt(IntLeafNode node) {
                if (node.getId() == remove.getId()) {
                    return REMOVE;
                }
                return super.visitInt(node);
            }

            @Override
            public Node visitFloat(FloatLeafNode node) {
                if (node.getId() == remove.getId()) {
                    return REMOVE;
                }
                return super.visitFloat(node);
            }
        };
        parent.accept(visitor);
        return parent;
    }

    public static <T extends ParentNode> T replaceNumeric(ParserContext context, T parent, LeafNode remove, Numeric numeric) {

        LeafNode replacement = numeric.isFloat() ? new FloatLeafNode(numeric) : new IntLeafNode(numeric);

        ReplacingASTVisitor visitor = new ReplacingASTVisitor(context, false) {
            @Override
            public Node visitInt(IntLeafNode node) {
                if (node.getId() == remove.getId()) {
                    return replacement;
                }
                return super.visitInt(node);
            }

            @Override
            public Node visitFloat(FloatLeafNode node) {
                if (node.getId() == remove.getId()) {
                    return replacement;
                }
                return super.visitFloat(node);
            }
        };
        parent.accept(visitor);
        return parent;
    }

    public static <T extends ParentNode> T replace(ParserContext context, T parent, Node nodeToRemove, Node replacement) {
        ReplacingASTVisitor visitor = new ReplacingASTVisitor(context, true) {
            protected void processParentNode(ParentNode node) {
                for (int i = 0; i < node.getChildCount(); i++) {
                    Node child = node.getChild(i);
                    if (child == null) {
                        continue;
                    }
                    if (nodeToRemove.hasEqualId(child)) {
                        parserContext.dereferenceTree(child);
                        if (replacement.equals(REMOVE)) {
                            node.removeChild(child);
                            i--;
                        } else {
                            node.setChild(i, replacement);
                        }
                    } else {
                        child.accept(this);
                    }
                }
            }
        };
        parent.accept(visitor);
        return parent;
    }
}

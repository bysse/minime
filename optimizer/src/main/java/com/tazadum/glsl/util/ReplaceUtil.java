package com.tazadum.glsl.util;

import com.tazadum.glsl.ast.ReplacingASTVisitor;
import com.tazadum.glsl.language.ast.LeafNode;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.arithmetic.NumericLeafNode;
import com.tazadum.glsl.language.type.Numeric;
import com.tazadum.glsl.parser.ParserContext;

/**
 * Created by erikb on 2018-03-26.
 */
public class ReplaceUtil {
    public static <T extends ParentNode> T removeNumeric(ParserContext context, T parent, LeafNode remove) {
        ReplacingASTVisitor visitor = new ReplacingASTVisitor(context, false) {
            @Override
            public Node visitNumeric(NumericLeafNode node) {
                if (node.getId() == remove.getId()) {
                    return REMOVE;
                }
                return super.visitNumeric(node);
            }
        };
        parent.accept(visitor);
        return parent;
    }

    public static <T extends ParentNode> T replaceNumeric(ParserContext context, T parent, LeafNode remove, Numeric numeric) {
        NumericLeafNode replacement = new NumericLeafNode(remove.getSourcePosition(), numeric);

        ReplacingASTVisitor visitor = new ReplacingASTVisitor(context, false) {
            @Override
            public Node visitNumeric(NumericLeafNode node) {
                if (node.getId() == remove.getId()) {
                    return replacement;
                }
                return super.visitNumeric(node);
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

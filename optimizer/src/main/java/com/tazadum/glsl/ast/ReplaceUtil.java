package com.tazadum.glsl.ast;

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
        return replace(context, parent, nodeToRemove, replacement, true, true);
    }

    public static <T extends ParentNode> T replace(ParserContext context, T parent, Node nodeToRemove, Node replacement, boolean dereference, boolean reference) {
        ReplacingASTVisitor visitor = new ReplacingASTVisitor(context, dereference, reference) {
            boolean found = false;
            protected void processParentNode(ParentNode node) {
                if (found) {
                    return;
                }
                for (int i = 0; i < node.getChildCount(); i++) {
                    Node child = node.getChild(i);
                    if (child == null) {
                        continue;
                    }
                    if (nodeToRemove.hasEqualId(child)) {
                        if (dereference) {
                            parserContext.dereferenceTree(child);
                        }
                        if (replacement.equals(REMOVE)) {
                            node.removeChild(child);
                            i--;
                        } else {
                            node.setChild(i, replacement);
                            if (reference) {
                                parserContext.referenceTree(replacement);
                            }
                            found = true;
                            break;
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

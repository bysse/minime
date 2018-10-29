package com.tazadum.glsl.language.ast.util;

import com.tazadum.glsl.language.ast.Identifier;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.traits.HasConstState;
import com.tazadum.glsl.language.ast.traits.HasMutableType;
import com.tazadum.glsl.language.ast.traits.HasNumeric;
import com.tazadum.glsl.language.ast.traits.HasSharedState;
import com.tazadum.glsl.language.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.language.ast.variable.VariableNode;
import com.tazadum.glsl.language.context.ContextAware;
import com.tazadum.glsl.language.context.GLSLContext;
import com.tazadum.glsl.language.context.GLSLContextImpl;
import com.tazadum.glsl.language.type.Numeric;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Erik on 2016-10-07.
 */
public class CloneUtils {
    public static <T extends Node> T clone(T node, ParentNode parent) {
        if (node == null) {
            return null;
        }
        T clonedNode = (T) node.clone(parent);
        if (parent == null) {
            clonedNode.calculateId(node.getId());
        }
        return clonedNode;
    }

    public static <T extends ParentNode> T cloneChildren(T source, T clone) {
        for (int i = 0; i < source.getChildCount(); i++) {
            final Node clonedChild = clone(source.getChild(i), clone);
            clone.setChild(i, clonedChild);
        }

        if (source instanceof HasMutableType) {
            ((HasMutableType) clone).setType(source.getType());
        }

        if (source instanceof HasSharedState) {
            ((HasSharedState) clone).setShared(((HasSharedState) source).isShared());
        }

        if (source instanceof HasConstState) {
            ((HasConstState) clone).setConstant(((HasConstState) source).isConstant());
        }

        return clone;
    }

    public static Node getRoot(Node node) {
        while (node.getParentNode() != null) {
            node = node.getParentNode();
        }
        return node;
    }

    /**
     * Finds the node with the same id in another Node tree.
     *
     * @param base Base of the tree to start searching in.
     * @param node The node to find in the base-tree.
     * @return
     */
    public static <T extends Node> T remap(Node base, T node) {
        final Node remappedNode = base.find(node.getId());

        if (remappedNode == null) {
            throw new IllegalStateException("Unable to find node with id " + node.getId() + " in the new node tree.");
        }

        if (!remappedNode.getClass().equals(node.getClass())) {
            String message = String.format("The remapped node has a different class for node id=%d : %s != %s",
                node.getId(), node.getClass().getSimpleName(), remappedNode.getClass().getSimpleName());
            throw new IllegalStateException(message);
        }

        return (T) remappedNode;
    }

    public static <T extends Node> List<T> remap(Node base, List<T> nodes) {
        final List<T> remapped = new ArrayList<>(nodes.size());
        for (T node : nodes) {
            remapped.add(remap(base, node));
        }
        return remapped;
    }

    public static GLSLContext remap(Node base, GLSLContext context) {
        if (context instanceof GLSLContextImpl) {
            return new GLSLContextImpl();
        }
        return (GLSLContext) remap(base, (Node) context);
    }

    public static GLSLContext remapContext(ContextAware contextAware, GLSLContext context) {
        if (context instanceof GLSLContextImpl) {
            return contextAware.globalContext();
        }

        final int contextId = ((Node) context).getId();
        for (GLSLContext ctx : contextAware.contexts()) {
            if (ctx instanceof Node && ((Node) ctx).getId() == contextId) {
                return ctx;
            }
        }

        throw new IllegalStateException("Unable to remap the GLSLContext");
    }

    public static boolean equal(Node nodeA, Node nodeB) {
        return equal(nodeA, nodeB, true);
    }

    /**
     * Does a functional comparision between two node trees.
     */
    public static boolean equal(Node nodeA, Node nodeB, boolean compareIdentifiers) {
        if (nodeA == null && nodeB == null) {
            return true;
        }

        if (nodeA == null || nodeB == null) {
            return false;
        }

        // HasNumeric nodes can be of different class but have the same value
        if (nodeA instanceof HasNumeric && nodeB instanceof HasNumeric) {
            Numeric a = ((HasNumeric) nodeA).getValue();
            Numeric b = ((HasNumeric) nodeB).getValue();
            return equal(a, b);
        }

        // not the same class
        if (!nodeA.getClass().equals(nodeB.getClass())) {
            return false;
        }

        if (nodeA instanceof ParentNode && nodeB instanceof ParentNode) {
            ParentNode parentA = (ParentNode) nodeA;
            ParentNode parentB = (ParentNode) nodeB;

            if (parentA.getChildCount() != parentB.getChildCount()) {
                return false;
            }

            for (int i = 0; i < parentA.getChildCount(); i++) {
                Node childA = parentA.getChild(i);
                Node childB = parentB.getChild(i);

                if (!equal(childA, childB, compareIdentifiers)) {
                    return false;
                }
            }
        }

        if (compareIdentifiers) {
            if (nodeA instanceof VariableDeclarationNode && nodeB instanceof VariableDeclarationNode) {
                return equal(((VariableDeclarationNode) nodeA).getIdentifier(), ((VariableDeclarationNode) nodeB).getIdentifier());
            }

            if (nodeA instanceof VariableNode && nodeB instanceof VariableNode) {
                return equal(((VariableNode) nodeA).getDeclarationNode(), ((VariableNode) nodeB).getDeclarationNode());
            }
        }

        return true;
    }

    private static boolean equal(Identifier a, Identifier b) {
        return a.original().equals(b.original());
    }

    private static boolean equal(Numeric a, Numeric b) {
        return a.compareTo(b) == 0;
    }

}

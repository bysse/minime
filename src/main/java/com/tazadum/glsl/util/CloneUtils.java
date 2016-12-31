package com.tazadum.glsl.util;

import com.tazadum.glsl.ast.HasMutableType;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.ParentNode;
import com.tazadum.glsl.parser.ContextAware;
import com.tazadum.glsl.parser.GLSLContext;
import com.tazadum.glsl.parser.GLSLContextImpl;

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
        return (T) node.clone(parent);
    }

    public static <T extends ParentNode> T cloneChildren(T source, T clone) {
        for (int i = 0; i < source.getChildCount(); i++) {
            final Node clonedChild = clone(source.getChild(i), clone);
            clone.setChild(i, clonedChild);
        }

        if (source instanceof HasMutableType) {
            ((HasMutableType) clone).setType(source.getType());
        }

        return clone;
    }

    public static Node getRoot(Node node) {
        while (node.getParentNode() != null) {
            node = node.getParentNode();
        }
        return node;
    }

    public static <T extends Node> T remap(Node base, T node) {
        final Node remappedNode = base.find(node.getId());

        if (!remappedNode.getClass().equals(node.getClass())) {
            throw new IllegalStateException("The remapped node has a different class for node id = " + node.getId());
        }

        if (remappedNode == null) {
            throw new IllegalStateException("Unable to find node with id " + node.getId() + " in the new node tree.");
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

}

package com.tazadum.glsl.language.ast;

import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.util.SourcePosition;

/**
 * Special Node type used for passing data through the visitor.
 */
public class DataNode<T> implements Node {
    public static <T> DataNode<T> cast(Class<T> type, Node node) {
        if (!(node instanceof DataNode)) {
            throw new UnsupportedOperationException("Provided node is not a DataNode");
        }
        return (DataNode<T>) node;
    }

    private SourcePosition sourcePosition;
    private T data;

    public DataNode(SourcePosition sourcePosition, T data) {
        this.sourcePosition = sourcePosition;
        this.data = data;
    }

    public T getData() {
        return data;
    }

    @Override
    public int getId() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public int calculateId(int id) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public SourcePosition getSourcePosition() {
        return sourcePosition;
    }

    @Override
    public ParentNode getParentNode() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void setParentNode(ParentNode parentNode) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Node clone(ParentNode newParent) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public GLSLType getType() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Node find(int id) {
        throw new UnsupportedOperationException("Not implemented");
    }
}

package com.tazadum.glsl.language.ast.variable;

import com.tazadum.glsl.language.ast.*;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.util.SourcePosition;

/**
 * Created by Erik on 2016-10-07.
 */
public class ArrayIndexNode extends FixedChildParentNode implements HasMutableType {
    private GLSLType type;

    public ArrayIndexNode(SourcePosition position, Node expression, Node index) {
        this(position, null, expression, index);
    }

    public ArrayIndexNode(SourcePosition position, ParentNode parent, Node expression, Node index) {
        super(position, 2, parent);
        setChild(0, expression);
        setChild(1, index);
    }

    public Node getExpression() {
        return getChild(0);
    }

    public Node getIndex() {
        return getChild(1);
    }

    public void setExpression(Node expression) {
        setChild(0, expression);
    }

    public void setIndex(Node node) {
        setChild(1, node);
    }

    @Override
    public ParentNode clone(ParentNode newParent) {
        final ArrayIndexNode node = new ArrayIndexNode(getSourcePosition(), newParent, null, null);
        node.setExpression(CloneUtils.clone(getExpression(), node));
        node.setIndex(CloneUtils.clone(getIndex(), node));
        return node;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitArrayIndex(this);
    }

    @Override
    public GLSLType getType() {
        return type;
    }

    @Override
    public void setType(GLSLType type) {
        this.type = type;
    }
}

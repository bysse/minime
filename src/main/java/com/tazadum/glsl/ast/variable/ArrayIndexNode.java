package com.tazadum.glsl.ast.variable;

import com.tazadum.glsl.ast.*;
import com.tazadum.glsl.language.GLSLType;
import com.tazadum.glsl.util.CloneUtils;

/**
 * Created by Erik on 2016-10-07.
 */
public class ArrayIndexNode extends FixedChildParentNode implements HasMutableType {
    private GLSLType type;

    public ArrayIndexNode(Node expression, Node index) {
        this(null, expression, index);
    }

    public ArrayIndexNode(ParentNode parent, Node expression, Node index) {
        super(2, parent);
        setChild(0, expression);
        setChild(1, index);
    }

    public Node getExpression() {
        return getChild(0);
    }

    public Node getIndex() {
        return getChild(1);
    }

    @Override
    public ParentNode clone(ParentNode newParent) {
        Node expression = CloneUtils.clone(getExpression());
        Node index = CloneUtils.clone(getIndex());
        return new ArrayIndexNode(newParent, expression, index);
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

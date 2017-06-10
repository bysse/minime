package com.tazadum.glsl.ast.variable;

import com.tazadum.glsl.ast.*;
import com.tazadum.glsl.language.GLSLType;
import com.tazadum.glsl.util.CloneUtils;

/**
 * Created by Erik on 2016-10-07.
 */
public class FieldSelectionNode extends FixedChildParentNode implements HasMutableType {
    private String selection;
    private GLSLType type;

    public FieldSelectionNode(String selection) {
        this(null, selection);
    }

    public FieldSelectionNode(ParentNode parentNode, String selection) {
        super(1, parentNode);
        this.selection = selection;
    }

    public String getSelection() {
        return selection;
    }

    public Node getExpression() {
        return getChild(0);
    }

    public void setExpression(Node expression) {
        setChild(0, expression);
    }

    @Override
    public FieldSelectionNode clone(ParentNode newParent) {
        return CloneUtils.cloneChildren(this, new FieldSelectionNode(newParent, selection));
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitFieldSelection(this);
    }

    @Override
    public GLSLType getType() {
        return type;
    }

    @Override
    public void setType(GLSLType type) {
        this.type = type;
    }

    public String toString() {
        return "FieldSelection(" + selection + ")";
    }
}

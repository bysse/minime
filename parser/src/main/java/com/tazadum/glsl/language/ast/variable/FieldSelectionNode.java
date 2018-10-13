package com.tazadum.glsl.language.ast.variable;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.FixedChildParentNode;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.traits.HasMutableType;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.util.SourcePosition;

/**
 * Created by Erik on 2016-10-07.
 */
public class FieldSelectionNode extends FixedChildParentNode implements HasMutableType {
    private String selection;
    private GLSLType type;

    public FieldSelectionNode(SourcePosition position, String selection) {
        this(position, null, selection);
    }

    public FieldSelectionNode(SourcePosition position, ParentNode parentNode, String selection) {
        super(position, 1, parentNode);
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
        return CloneUtils.cloneChildren(this, new FieldSelectionNode(getSourcePosition(), newParent, selection));
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

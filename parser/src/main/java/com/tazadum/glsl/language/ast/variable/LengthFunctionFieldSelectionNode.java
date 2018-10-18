package com.tazadum.glsl.language.ast.variable;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.language.type.PredefinedType;
import com.tazadum.glsl.util.SourcePosition;

/**
 * Created by Erik on 2016-10-07.
 */
public class LengthFunctionFieldSelectionNode extends FieldSelectionNode {
    public static final String LENGTH_FUNCTION = "length()";
    private GLSLType type;

    public LengthFunctionFieldSelectionNode(SourcePosition position) {
        this(position, null);
    }

    public LengthFunctionFieldSelectionNode(SourcePosition position, ParentNode parentNode) {
        super(position, parentNode, LENGTH_FUNCTION);
    }

    public Node getExpression() {
        return getChild(0);
    }

    public void setExpression(Node expression) {
        setChild(0, expression);
    }

    @Override
    public LengthFunctionFieldSelectionNode clone(ParentNode newParent) {
        return CloneUtils.cloneChildren(this, new LengthFunctionFieldSelectionNode(getSourcePosition(), newParent));
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitFieldSelection(this);
    }

    @Override
    public GLSLType getType() {
        return PredefinedType.UINT;
    }

    @Override
    public void setType(GLSLType type) {
        this.type = type;
    }
}

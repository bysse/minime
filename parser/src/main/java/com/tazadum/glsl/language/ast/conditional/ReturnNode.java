package com.tazadum.glsl.language.ast.conditional;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.FixedChildParentNode;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.language.type.PredefinedType;
import com.tazadum.glsl.util.SourcePosition;

public class ReturnNode extends FixedChildParentNode {

    public ReturnNode(SourcePosition position) {
        this(position, null);
    }

    public ReturnNode(SourcePosition position, ParentNode parentNode) {
        super(position, 1, parentNode);
    }

    public void setExpression(Node expression) {
        setChild(0, expression);
    }

    public Node getExpression() {
        return getChild(0);
    }

    public boolean hasExpression() {
        return getExpression() != null;
    }

    @Override
    public ReturnNode clone(ParentNode newParent) {
        return CloneUtils.cloneChildren(this, new ReturnNode(getSourcePosition(), newParent));
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitReturn(this);
    }

    @Override
    public GLSLType getType() {
        if (hasExpression()) {
            return getExpression().getType();
        }
        return PredefinedType.VOID;
    }
}

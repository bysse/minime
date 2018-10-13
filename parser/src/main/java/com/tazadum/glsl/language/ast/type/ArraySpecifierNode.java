package com.tazadum.glsl.language.ast.type;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.FixedChildParentNode;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.language.type.PredefinedType;
import com.tazadum.glsl.util.SourcePosition;

public class ArraySpecifierNode extends FixedChildParentNode {
    public ArraySpecifierNode(SourcePosition position, Node sizeExpression) {
        this(position, null, sizeExpression);
    }

    public ArraySpecifierNode(SourcePosition position, ParentNode parentNode, Node sizeExpression) {
        super(position, 1, parentNode);
        setChild(0, sizeExpression);
    }

    public Node getExpression() {
        return getChild(0);
    }

    @Override
    public ParentNode clone(ParentNode newParent) {
        ArraySpecifierNode node = new ArraySpecifierNode(getSourcePosition(), null);
        return CloneUtils.cloneChildren(this, node);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitArrayTypeNode(this);
    }

    @Override
    public GLSLType getType() {
        return PredefinedType.UINT;
    }
}

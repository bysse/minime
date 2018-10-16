package com.tazadum.glsl.language.ast.type;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.FixedChildParentNode;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.util.SourcePosition;

/**
 * Node for holding a single TypeQualifier
 */
public class LayoutQualifierIdNode extends FixedChildParentNode {
    private String identifier;

    public LayoutQualifierIdNode(SourcePosition position, String identifier, Node expression) {
        this(position, null, identifier, expression);
    }

    public LayoutQualifierIdNode(SourcePosition position, ParentNode parentNode, String identifier, Node expression) {
        super(position, 1, parentNode);
        this.identifier = identifier;
        setChild(0, expression);
    }

    public String getIdentifier() {
        return identifier;
    }

    public Node getExpression() {
        return getChild(0);
    }

    @Override
    public ParentNode clone(ParentNode newParent) {
        LayoutQualifierIdNode node = new LayoutQualifierIdNode(getSourcePosition(), newParent, identifier, null);
        return CloneUtils.cloneChildren(this, node);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitLayoutQualifierIdNode(this);
    }

    @Override
    public GLSLType getType() {
        return null;
    }
}

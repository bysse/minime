package com.tazadum.glsl.language.ast.type;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.FixedChildParentNode;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.language.type.TypeQualifierList;
import com.tazadum.glsl.util.SourcePosition;

import java.util.List;

/**
 * Node for holding a single TypeQualifier declaration.
 */
public class TypeQualifierDeclarationNode extends FixedChildParentNode {
    private List<String> identifiers;

    public TypeQualifierDeclarationNode(SourcePosition position, TypeQualifierListNode qualifiers, List<String> identifiers) {
        this(position, null, qualifiers, identifiers);
    }

    public TypeQualifierDeclarationNode(SourcePosition position, ParentNode parentNode, TypeQualifierListNode qualifiers, List<String> identifiers) {
        super(position, 1, parentNode);
        this.identifiers = identifiers;
        setChild(0, qualifiers);
    }

    public TypeQualifierList getQualifiers() {
        return getChildAs(0);
    }

    @Override
    public ParentNode clone(ParentNode newParent) {
        TypeQualifierDeclarationNode node = new TypeQualifierDeclarationNode(getSourcePosition(), newParent, null, identifiers);
        return CloneUtils.cloneChildren(this, node);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitTypeQualifierDeclarationNode(this);
    }

    @Override
    public GLSLType getType() {
        return null;
    }
}

package com.tazadum.glsl.language.ast.unresolved;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.FixedChildParentNode;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.traits.UnresolvedNode;
import com.tazadum.glsl.language.ast.type.ArraySpecifierListNode;
import com.tazadum.glsl.util.SourcePosition;

/**
 * Created by erikb on 2018-10-15.
 */
public class UnresolvedStructFieldNode extends FixedChildParentNode implements UnresolvedNode {
    private String identifier;

    public UnresolvedStructFieldNode(SourcePosition position, String identifier, ArraySpecifierListNode arraySpecifier) {
        this(position, null, identifier, arraySpecifier);
    }

    public UnresolvedStructFieldNode(SourcePosition position, ParentNode parentNode, String identifier, ArraySpecifierListNode arraySpecifier) {
        super(position, 1, parentNode);
        this.identifier = identifier;
        setChild(0, arraySpecifier);
    }

    public String getIdentifier() {
        return identifier;
    }

    public ArraySpecifierListNode getArraySpecifier() {
        return getChildAs(0);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitUnresolvedStructFieldNode(this);
    }
}

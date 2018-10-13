package com.tazadum.glsl.language.ast.unresolved;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.LeafNode;
import com.tazadum.glsl.language.ast.traits.UnresolvedNode;
import com.tazadum.glsl.util.SourcePosition;

public class UnresolvedVariableNode extends LeafNode implements UnresolvedNode {
    private final String identifier;

    public UnresolvedVariableNode(SourcePosition position, String identifier) {
        super(position);
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitVariable(this);
    }

    @Override
    public String toString() {
        return "UnresolvedVariable('" + identifier + "')";
    }
}

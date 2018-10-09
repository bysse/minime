package com.tazadum.glsl.language.ast.variable;

import com.tazadum.glsl.language.ast.*;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.type.FullySpecifiedType;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.util.SourcePosition;

public class VariableDeclarationNode extends FixedChildParentNode implements HasSharedState {
    private boolean builtIn;
    protected final FullySpecifiedType type;

    protected Identifier identifier;
    private boolean shared;

    public VariableDeclarationNode(SourcePosition position, boolean builtIn, FullySpecifiedType fst, String identifier, Node arraySpecifier, Node initializer) {
        this(position, null, builtIn, fst, new Identifier(identifier), arraySpecifier, initializer);
    }

    protected VariableDeclarationNode(SourcePosition position, ParentNode newParent, boolean builtIn, FullySpecifiedType fst, Identifier identifier, Node arraySpecifier, Node initializer) {
        super(position, 2, newParent);

        this.builtIn = builtIn;
        this.type = fst;
        this.identifier = identifier;

        setArraySpecifier(arraySpecifier);
        setInitializer(initializer);
    }

    public boolean isBuiltIn() {
        return builtIn;
    }

    public Node getArraySpecifier() {
        return getChild(0);
    }

    public void setArraySpecifier(Node arraySpecifier) {
        setChild(0, arraySpecifier);
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public Node getInitializer() {
        return getChild(1);
    }

    public void setInitializer(Node initializer) {
        setChild(1, initializer);
    }

    public FullySpecifiedType getFullySpecifiedType() {
        return type;
    }

    @Override
    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    @Override
    public ParentNode clone(ParentNode newParent) {
        final VariableDeclarationNode node = new VariableDeclarationNode(getSourcePosition(), newParent, builtIn, type, identifier, null, null);
        node.setArraySpecifier(CloneUtils.clone(getArraySpecifier(), node));
        node.setInitializer(CloneUtils.clone(getInitializer(), node));
        return node;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitVariableDeclaration(this);
    }

    @Override
    public GLSLType getType() {
        return type.getType();
    }

    @Override
    public String toString() {
        return "VariableDeclaration('" + identifier + "')";
    }
}

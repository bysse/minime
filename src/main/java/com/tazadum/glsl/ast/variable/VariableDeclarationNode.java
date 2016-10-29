package com.tazadum.glsl.ast.variable;

import com.tazadum.glsl.ast.*;
import com.tazadum.glsl.language.GLSLType;
import com.tazadum.glsl.parser.type.FullySpecifiedType;
import com.tazadum.glsl.util.CloneUtils;

public class VariableDeclarationNode extends FixedChildParentNode {
    private boolean builtIn;
    protected final FullySpecifiedType type;
    protected final Node arraySpecifier;
    protected final Node initializer;

    protected Identifier identifier;

    public VariableDeclarationNode(boolean builtIn, FullySpecifiedType fst, String identifier, Node arraySpecifier, Node initializer) {
        this(null, builtIn, fst, new Identifier(identifier), arraySpecifier, initializer);
    }

    protected VariableDeclarationNode(ParentNode newParent, boolean builtIn, FullySpecifiedType fst, Identifier identifier, Node arraySpecifier, Node initializer) {
        super(2, newParent);

        this.builtIn = builtIn;
        this.type = fst;
        this.identifier = identifier;
        this.arraySpecifier = arraySpecifier;
        this.initializer = initializer;

        setArraySpecifier(arraySpecifier);
        setInitializer(initializer);
    }

    public boolean isBuiltIn() {
        return builtIn;
    }

    public Node getArraySpecifier() {
        return arraySpecifier;
    }

    public void setArraySpecifier(Node arraySpecifier) {
        setChild(0, arraySpecifier);
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public Node getInitializer() {
        return initializer;
    }

    public void setInitializer(Node initializer) {
        setChild(1, initializer);
    }

    public FullySpecifiedType getFullySpecifiedType() {
        return type;
    }

    @Override
    public ParentNode clone(ParentNode newParent) {
        return new VariableDeclarationNode(newParent, builtIn, type, identifier, CloneUtils.clone(arraySpecifier), CloneUtils.clone(initializer));
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

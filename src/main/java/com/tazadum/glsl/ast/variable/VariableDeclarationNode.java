package com.tazadum.glsl.ast.variable;

import com.tazadum.glsl.ast.*;
import com.tazadum.glsl.parser.type.FullySpecifiedType;
import com.tazadum.glsl.util.CloneUtils;

public class VariableDeclarationNode extends FixedChildParentNode {
    protected final FullySpecifiedType type;
    protected final Node arraySpecifier;
    protected final Node initializer;

    protected Identifier identifier;

    public VariableDeclarationNode(FullySpecifiedType fst, String identifier, Node arraySpecifier, Node initializer) {
        this(null, fst, new Identifier(identifier), arraySpecifier, initializer);
    }

    protected VariableDeclarationNode(ParentNode newParent, FullySpecifiedType fst, Identifier identifier, Node arraySpecifier, Node initializer) {
        super(2, newParent);

        this.type = fst;
        this.identifier = identifier;
        this.arraySpecifier = arraySpecifier;
        this.initializer = initializer;
        setChild(0, arraySpecifier);
        setChild(1, initializer);
    }

    public Node getArraySpecifier() {
        return arraySpecifier;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public Node getInitializer() {
        return initializer;
    }

    public FullySpecifiedType getFullySpecifiedType() {
        return type;
    }

    @Override
    public ParentNode clone(ParentNode newParent) {
        return new VariableDeclarationNode(newParent, type, identifier, CloneUtils.clone(arraySpecifier), CloneUtils.clone(initializer));
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitVariableDeclaration(this);
    }
}

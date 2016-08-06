package com.tazadum.glsl.ast;

import com.tazadum.glsl.parser.type.FullySpecifiedType;

public class VariableDeclarationNode extends FixedChildParentNode {
    private final FullySpecifiedType type;
    private final Context arraySpecifier;
    private final Context initializer;
    private final Node[] nodes;

    private String identifier;

    public VariableDeclarationNode(FullySpecifiedType fst, String identifier, Context arraySpecifier, Context initializer) {
        this.type = fst;
        this.identifier = identifier;
        this.arraySpecifier = arraySpecifier;
        this.initializer = initializer;

        this.nodes = new Node[] {
            arraySpecifier, initializer
        };
    }

    @Override
    protected Node[] getChildNodes() {
        return nodes;
    }

    public Context getArraySpecifier() {
        return arraySpecifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Context getInitializer() {
        return initializer;
    }

    public Node[] getNodes() {
        return nodes;
    }

    public FullySpecifiedType getFullySpecifiedType() {
        return type;
    }
}

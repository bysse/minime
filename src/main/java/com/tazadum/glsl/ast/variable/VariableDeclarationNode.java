package com.tazadum.glsl.ast.variable;

import com.tazadum.glsl.ast.FixedChildParentNode;
import com.tazadum.glsl.ast.Identifier;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.parser.type.FullySpecifiedType;

public class VariableDeclarationNode extends FixedChildParentNode {
    private final FullySpecifiedType type;
    private final Node arraySpecifier;
    private final Node initializer;
    private final Node[] nodes;

    private Identifier identifier;

    public VariableDeclarationNode(FullySpecifiedType fst, String identifier, Node arraySpecifier, Node initializer) {
        this.type = fst;
        this.identifier = new Identifier(identifier);
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

    public Node getArraySpecifier() {
        return arraySpecifier;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public Node getInitializer() {
        return initializer;
    }

    public Node[] getNodes() {
        return nodes;
    }

    public FullySpecifiedType getFullySpecifiedType() {
        return type;
    }
}

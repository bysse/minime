package com.tazadum.glsl.ast.variable;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.parser.type.FullySpecifiedType;

public class ParameterDeclarationNode extends VariableDeclarationNode {
    public ParameterDeclarationNode(FullySpecifiedType fst, String identifier, Node arraySpecifier) {
        super(fst, identifier, arraySpecifier, null);
    }
}

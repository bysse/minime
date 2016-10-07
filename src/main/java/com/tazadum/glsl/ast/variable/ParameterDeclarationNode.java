package com.tazadum.glsl.ast.variable;

import com.tazadum.glsl.ast.Identifier;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.ParentNode;
import com.tazadum.glsl.parser.type.FullySpecifiedType;
import com.tazadum.glsl.util.CloneUtils;

public class ParameterDeclarationNode extends VariableDeclarationNode {
    public ParameterDeclarationNode(FullySpecifiedType fst, String identifier, Node arraySpecifier) {
        super(fst, identifier, arraySpecifier, null);
    }

    protected ParameterDeclarationNode(ParentNode newParent, FullySpecifiedType fst, Identifier identifier, Node arraySpecifier, Node initializer) {
        super(newParent, fst, identifier, arraySpecifier, initializer);
    }

    @Override
    public ParentNode clone(ParentNode newParent) {
        return new ParameterDeclarationNode(newParent, type, identifier, CloneUtils.clone(arraySpecifier), CloneUtils.clone(initializer));
    }
}

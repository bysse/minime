package com.tazadum.glsl.language.ast.unresolved;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.traits.HasSharedState;
import com.tazadum.glsl.util.SourcePosition;

public class UnresolvedFunctionPrototypeNode extends ParentNode implements HasSharedState {
    private String identifier;
    private boolean shared;

    public UnresolvedFunctionPrototypeNode(SourcePosition position, String identifierName, UnresolvedTypeNode returnType) {
        this(position, null, identifierName, returnType);
    }

    public UnresolvedFunctionPrototypeNode(SourcePosition position, ParentNode parentNode, String identifier, UnresolvedTypeNode returnType) {
        super(position, parentNode);
        this.identifier = identifier;

        addChild(returnType);
    }

    public UnresolvedTypeNode getReturnType() {
        return getChildAs(0);
    }

    public void addParameter(UnresolvedParameterDeclarationNode parameter) {
        addChild(parameter);
    }

    public int getParameterCount() {
        return getChildCount() - 1;
    }

    public UnresolvedParameterDeclarationNode getParameter(int index) {
        assert index > 0 : "Invalid index";
        return getChildAs(index + 1);
    }


    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitFunctionPrototype(this);
    }

    @Override
    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }
}

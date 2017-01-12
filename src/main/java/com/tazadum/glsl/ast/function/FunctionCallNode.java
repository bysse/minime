package com.tazadum.glsl.ast.function;

import com.tazadum.glsl.ast.ASTVisitor;
import com.tazadum.glsl.ast.Identifier;
import com.tazadum.glsl.ast.ParentNode;
import com.tazadum.glsl.language.GLSLType;
import com.tazadum.glsl.util.CloneUtils;

public class FunctionCallNode extends ParentNode {
    private Identifier identifier;
    private FunctionPrototypeNode declarationNode;

    public FunctionCallNode(String functionName) {
        this.identifier = new Identifier(functionName);
    }

    public FunctionCallNode(ParentNode parentNode, Identifier identifier) {
        super(parentNode);
        this.identifier = identifier;
    }

    public Identifier getIdentifier() {
        if (declarationNode != null) {
            return declarationNode.getIdentifier();
        }
        return identifier;
    }

    public void setDeclarationNode(FunctionPrototypeNode declarationNode) {
        this.declarationNode = declarationNode;
    }

    public FunctionPrototypeNode getDeclarationNode() {
        return declarationNode;
    }

    @Override
    public String toString() {
        return identifier.toString();
    }

    @Override
    public ParentNode clone(ParentNode newParent) {
        FunctionCallNode clone = CloneUtils.cloneChildren(this, new FunctionCallNode(newParent, identifier));
        // we don't clone the declaration here
        clone.setDeclarationNode(declarationNode);
        return clone;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitFunctionCall(this);
    }

    @Override
    public GLSLType getType() {
        return declarationNode.getType();
    }
}

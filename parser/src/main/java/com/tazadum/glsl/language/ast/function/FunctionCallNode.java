package com.tazadum.glsl.language.ast.function;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.Identifier;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.util.SourcePositionId;

public class FunctionCallNode extends ParentNode {
    private Identifier identifier;
    private FunctionPrototypeNode declarationNode;

    public FunctionCallNode(SourcePositionId position, String functionName) {
        super(position);
        this.identifier = new Identifier(functionName);
    }

    public FunctionCallNode(SourcePositionId position, ParentNode parentNode, Identifier identifier) {
        super(position, parentNode);
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
        FunctionCallNode clone = CloneUtils.cloneChildren(this, new FunctionCallNode(getSourcePositionId(), newParent, identifier));
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

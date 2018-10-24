package com.tazadum.glsl.language.ast.function;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.Identifier;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.traits.HasConstState;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.model.ArraySpecifiers;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.util.SourcePosition;

public class FunctionCallNode extends ParentNode implements HasConstState {
    private Identifier identifier;
    private ArraySpecifiers arraySpecifiers;
    private FunctionPrototypeNode declarationNode;
    private boolean constant;

    public FunctionCallNode(SourcePosition position, String functionName) {
        this(position, functionName, null);
    }

    public FunctionCallNode(SourcePosition position, String functionName, ArraySpecifiers arraySpecifiers) {
        super(position);
        this.identifier = new Identifier(functionName);
        this.arraySpecifiers = arraySpecifiers;
    }

    public FunctionCallNode(SourcePosition position, ParentNode parentNode, Identifier identifier, ArraySpecifiers arraySpecifiers) {
        super(position, parentNode);
        this.identifier = identifier;
        this.arraySpecifiers = arraySpecifiers;
    }

    public Identifier getIdentifier() {
        if (declarationNode != null) {
            return declarationNode.getIdentifier();
        }
        return identifier;
    }

    public ArraySpecifiers getArraySpecifiers() {
        return arraySpecifiers;
    }

    public void setDeclarationNode(FunctionPrototypeNode declarationNode) {
        this.declarationNode = declarationNode;
    }

    public FunctionPrototypeNode getDeclarationNode() {
        return declarationNode;
    }

    @Override
    public boolean isConstant() {
        return constant;
    }

    @Override
    public void setConstant(boolean constant) {
        this.constant = constant;
    }

    @Override
    public String toString() {
        return identifier.toString();
    }

    @Override
    public ParentNode clone(ParentNode newParent) {
        FunctionCallNode clone = CloneUtils.cloneChildren(this, new FunctionCallNode(getSourcePosition(), newParent, identifier, arraySpecifiers));
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

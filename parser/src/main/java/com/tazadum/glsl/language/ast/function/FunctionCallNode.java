package com.tazadum.glsl.language.ast.function;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.Identifier;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.traits.HasConstState;
import com.tazadum.glsl.language.ast.traits.HasDeclarationReference;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.model.ArraySpecifiers;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.util.SourcePosition;

import java.util.Objects;

public class FunctionCallNode extends ParentNode implements HasConstState, HasDeclarationReference<FunctionPrototypeNode> {
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

    public void setLocalIdentifier(Identifier identifier) {
        this.identifier = identifier;
    }

    public ArraySpecifiers getArraySpecifiers() {
        return arraySpecifiers;
    }

    @Override
    public FunctionPrototypeNode getDeclarationNode() {
        return declarationNode;
    }

    @Override
    public void setDeclarationNode(FunctionPrototypeNode declarationNode) {
        this.declarationNode = declarationNode;
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
    public ParentNode clone(ParentNode newParent) {
        FunctionCallNode clone = CloneUtils.cloneChildren(this, new FunctionCallNode(getSourcePosition(), newParent, new Identifier(identifier), arraySpecifiers));
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

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < getChildCount(); i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(Objects.toString(getChild(i)));
        }
        return identifier.original() + "(" + builder.toString() + ")";
    }
}

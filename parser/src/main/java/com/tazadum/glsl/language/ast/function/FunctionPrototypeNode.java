package com.tazadum.glsl.language.ast.function;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.Identifier;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.traits.HasSharedState;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.ast.variable.ParameterDeclarationNode;
import com.tazadum.glsl.language.function.FunctionPrototype;
import com.tazadum.glsl.language.type.FullySpecifiedType;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.util.SourcePosition;

import java.util.Objects;

public class FunctionPrototypeNode extends ParentNode implements HasSharedState {
    private Identifier identifier;
    private FullySpecifiedType returnType;
    private FunctionPrototype prototype;
    private boolean mutatesGlobalState = false;
    private boolean usesGlobalState = true;
    private boolean shared;

    public FunctionPrototypeNode(SourcePosition position, String functionName, FullySpecifiedType returnType) {
        super(position);
        this.identifier = new Identifier(functionName);
        this.returnType = returnType;
    }

    public FunctionPrototypeNode(SourcePosition position, ParentNode parentNode, Identifier identifier, FullySpecifiedType returnType) {
        super(position, parentNode);
        this.identifier = identifier;
        this.returnType = returnType;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public FullySpecifiedType getReturnType() {
        return returnType;
    }

    public FunctionPrototype getPrototype() {
        return prototype;
    }

    public void setPrototype(FunctionPrototype prototype) {
        this.prototype = prototype;
    }

    @Override
    public boolean isShared() {
        return shared;
    }

    public void addParameter(ParameterDeclarationNode parameter) {
        addChild(parameter);
    }

    public ParameterDeclarationNode getParameter(int index) {
        return getChildAs(index);
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    @Override
    public ParentNode clone(ParentNode newParent) {
        final FunctionPrototypeNode node = CloneUtils.cloneChildren(this, new FunctionPrototypeNode(getSourcePosition(), newParent, new Identifier(identifier), returnType));
        node.setPrototype(prototype);
        node.setMutatesGlobalState(mutatesGlobalState);
        node.setUsesGlobalState(usesGlobalState);

        return node;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitFunctionPrototype(this);
    }

    @Override
    public GLSLType getType() {
        return returnType.getType();
    }

    public void setMutatesGlobalState(boolean value) {
        mutatesGlobalState = value;
    }

    public boolean mutatesGlobalState() {
        return mutatesGlobalState;
    }

    public boolean usesGlobalState() {
        return usesGlobalState;
    }

    public void setUsesGlobalState(boolean usesGlobalState) {
        this.usesGlobalState = usesGlobalState;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(returnType).append(' ').append(identifier.original());

        builder.append('(');
        for (int i = 0; i < getChildCount(); i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(Objects.toString(getParameter(i)));
        }
        builder.append(')');
        return builder.toString();
    }
}

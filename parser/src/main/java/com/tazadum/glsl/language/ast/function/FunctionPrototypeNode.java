package com.tazadum.glsl.language.ast.function;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.Identifier;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.traits.HasSharedState;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.context.GLSLContext;
import com.tazadum.glsl.language.function.FunctionPrototype;
import com.tazadum.glsl.language.type.FullySpecifiedType;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.util.SourcePosition;

public class FunctionPrototypeNode extends ParentNode implements HasSharedState {
    private Identifier identifier;
    private FullySpecifiedType returnType;
    private FunctionPrototype prototype;
    private GLSLContext context;
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

    public GLSLContext getContext() {
        return context;
    }

    public void setContext(GLSLContext context) {
        this.context = context;
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

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    @Override
    public ParentNode clone(ParentNode newParent) {
        final FunctionPrototypeNode prototypeNode = CloneUtils.cloneChildren(this, new FunctionPrototypeNode(getSourcePosition(), newParent, identifier, returnType));

        if (newParent instanceof GLSLContext) {
            prototypeNode.setContext((GLSLContext) newParent);
        }

        return prototypeNode;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitFunctionPrototype(this);
    }

    @Override
    public GLSLType getType() {
        return returnType.getType();
    }

    public String toString() {
        return "function(" + identifier + ")";
    }
}

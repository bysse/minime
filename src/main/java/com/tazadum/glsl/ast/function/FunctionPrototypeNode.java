package com.tazadum.glsl.ast.function;

import com.tazadum.glsl.ast.ASTVisitor;
import com.tazadum.glsl.ast.Identifier;
import com.tazadum.glsl.ast.ParentNode;
import com.tazadum.glsl.language.GLSLType;
import com.tazadum.glsl.parser.GLSLContext;
import com.tazadum.glsl.parser.function.FunctionPrototype;
import com.tazadum.glsl.parser.type.FullySpecifiedType;
import com.tazadum.glsl.util.CloneUtils;

public class FunctionPrototypeNode extends ParentNode {
    private Identifier identifier;
    private FullySpecifiedType returnType;
    private FunctionPrototype prototype;
    private GLSLContext context;

    public FunctionPrototypeNode(String functionName, FullySpecifiedType returnType) {
        this.identifier = new Identifier(functionName);
        this.returnType = returnType;
    }

    public FunctionPrototypeNode(ParentNode parentNode, Identifier identifier, FullySpecifiedType returnType) {
        super(parentNode);
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
    public ParentNode clone(ParentNode newParent) {
        final FunctionPrototypeNode prototypeNode = CloneUtils.cloneChildren(this, new FunctionPrototypeNode(newParent, identifier, returnType));

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
}

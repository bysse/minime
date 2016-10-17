package com.tazadum.glsl.ast.function;

import com.tazadum.glsl.ast.ASTVisitor;
import com.tazadum.glsl.ast.Identifier;
import com.tazadum.glsl.ast.ParentNode;
import com.tazadum.glsl.parser.GLSLContext;
import com.tazadum.glsl.parser.function.FunctionPrototype;
import com.tazadum.glsl.parser.type.FullySpecifiedType;
import com.tazadum.glsl.util.CloneUtils;

public class FunctionPrototypeNode extends ParentNode implements GLSLContext {
    private Identifier identifier;
    private FullySpecifiedType returnType;
    private FunctionPrototype prototype;
    private GLSLContext parentContext;

    public FunctionPrototypeNode(String functionName, FullySpecifiedType returnType) {
        this.identifier = new Identifier(functionName);
        this.returnType = returnType;
    }

    public FunctionPrototypeNode(ParentNode parentNode, Identifier identifier, FullySpecifiedType returnType) {
        super(parentNode);
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
    public ParentNode clone(ParentNode newParent) {
        return CloneUtils.cloneChildren(this, new FunctionPrototypeNode(newParent, identifier, returnType));
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitFunctionPrototype(this);
    }

    @Override
    public GLSLContext getParent() {
        return parentContext;
    }

    @Override
    public void setParent(GLSLContext parentContext) {
        this.parentContext = parentContext;
    }
}

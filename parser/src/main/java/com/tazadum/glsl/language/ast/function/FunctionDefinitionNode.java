package com.tazadum.glsl.language.ast.function;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.FixedChildParentNode;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.StatementListNode;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.context.GLSLContext;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.util.SourcePosition;

public class FunctionDefinitionNode extends FixedChildParentNode implements GLSLContext {
    private GLSLContext parentContext;

    public FunctionDefinitionNode(SourcePosition position, FunctionPrototypeNode prototype, StatementListNode statementList) {
        this(position, null, prototype, statementList);
    }

    public FunctionDefinitionNode(SourcePosition position, ParentNode parent, FunctionPrototypeNode prototype, StatementListNode statementList) {
        super(position, 2, parent);
        setChild(0, prototype);
        setChild(1, statementList);
    }

    public void setFunctionPrototype(FunctionPrototypeNode prototype) {
        setChild(0, prototype);
    }

    public FunctionPrototypeNode getFunctionPrototype() {
        return getChildAs(0);
    }

    public void setStatements(StatementListNode node) {
        setChild(1, node);
    }

    public StatementListNode getStatements() {
        return getChildAs(1);
    }

    @Override
    public ParentNode clone(ParentNode newParent) {
        final FunctionDefinitionNode node = new FunctionDefinitionNode(getSourcePosition(), newParent, null, null);
        node.setFunctionPrototype(CloneUtils.clone(getFunctionPrototype(), node));
        node.setStatements(CloneUtils.clone(getStatements(), node));
        return node;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitFunctionDefinition(this);
    }

    @Override
    public GLSLType getType() {
        return null;
    }

    @Override
    public GLSLContext getParent() {
        return parentContext;
    }

    @Override
    public void setParent(GLSLContext parentContext) {
        this.parentContext = parentContext;
    }

    @Override
    public boolean isGlobal() {
        return false;
    }

    public String toString() {
        return String.valueOf(getChild(0));
    }
}

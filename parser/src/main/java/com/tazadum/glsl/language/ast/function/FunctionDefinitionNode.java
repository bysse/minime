package com.tazadum.glsl.language.ast.function;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.FixedChildParentNode;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.StatementListNode;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.context.GLSLContext;
import com.tazadum.glsl.language.type.GLSLType;

public class FunctionDefinitionNode extends FixedChildParentNode implements GLSLContext {
    private GLSLContext parentContext;
    private boolean mutatesGlobalState = false;
    private boolean usesGlobalState = true;

    public FunctionDefinitionNode(FunctionPrototypeNode prototype, StatementListNode statementList) {
        this(null, prototype, statementList);
    }

    public FunctionDefinitionNode(ParentNode parent, FunctionPrototypeNode prototype, StatementListNode statementList) {
        super(2, parent);
        setChild(0, prototype);
        setChild(1, statementList);
    }

    public void setFunctionPrototype(FunctionPrototypeNode prototype) {
        setChild(0, prototype);
    }

    public FunctionPrototypeNode getFunctionPrototype() {
        return getChild(0, FunctionPrototypeNode.class);
    }

    public void setStatements(StatementListNode node) {
        setChild(1, node);
    }

    public StatementListNode getStatements() {
        return getChild(1, StatementListNode.class);
    }

    @Override
    public ParentNode clone(ParentNode newParent) {
        final FunctionDefinitionNode node = new FunctionDefinitionNode(newParent, null, null);
        node.setMutatesGlobalState(mutatesGlobalState);
        node.setUsesGlobalState(usesGlobalState);
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
        return String.valueOf(getChild(0));
    }
}

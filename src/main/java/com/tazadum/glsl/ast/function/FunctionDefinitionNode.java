package com.tazadum.glsl.ast.function;

import com.tazadum.glsl.ast.ASTVisitor;
import com.tazadum.glsl.ast.FixedChildParentNode;
import com.tazadum.glsl.ast.ParentNode;
import com.tazadum.glsl.ast.StatementListNode;
import com.tazadum.glsl.language.GLSLType;
import com.tazadum.glsl.parser.GLSLContext;
import com.tazadum.glsl.util.CloneUtils;

public class FunctionDefinitionNode extends FixedChildParentNode implements GLSLContext {
    private GLSLContext parentContext;
    private boolean mutatesGlobalState = false;

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
        final FunctionPrototypeNode prototype = CloneUtils.clone(getFunctionPrototype());
        final StatementListNode statementList = CloneUtils.clone(getStatements());
        final FunctionDefinitionNode node = new FunctionDefinitionNode(newParent, prototype, statementList);
        node.setMutatesGlobalState(mutatesGlobalState);
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

    public void setMutatesGlobalState(boolean value) {
        mutatesGlobalState = value;
    }

    public boolean mutatesGlobalState() {
        return mutatesGlobalState;
    }
}

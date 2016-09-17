package com.tazadum.glsl.ast.function;

import com.tazadum.glsl.ast.FixedChildParentNode;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.StatementListNode;

public class FunctionDefinitionNode extends FixedChildParentNode {
    private final FunctionPrototypeNode declaration;
    private final StatementListNode statementList;

    public FunctionDefinitionNode(FunctionPrototypeNode prototype, StatementListNode statementList) {
        this.declaration = prototype;
        this.statementList = statementList;
    }

    @Override
    protected Node[] getChildNodes() {
        return new Node[]{declaration, statementList};
    }
}

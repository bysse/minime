package com.tazadum.glsl.ast.function;

import com.tazadum.glsl.ast.ASTVisitor;
import com.tazadum.glsl.ast.FixedChildParentNode;
import com.tazadum.glsl.ast.ParentNode;
import com.tazadum.glsl.ast.StatementListNode;
import com.tazadum.glsl.util.CloneUtils;

public class FunctionDefinitionNode extends FixedChildParentNode {

    public FunctionDefinitionNode(FunctionPrototypeNode prototype, StatementListNode statementList) {
        this(null, prototype, statementList);
    }

    public FunctionDefinitionNode(ParentNode parent, FunctionPrototypeNode prototype, StatementListNode statementList) {
        super(2, parent);
        setChild(0, prototype);
        setChild(1, statementList);
    }

    @Override
    public ParentNode clone(ParentNode newParent) {
        final FunctionPrototypeNode prototype = CloneUtils.clone(getChild(0, FunctionPrototypeNode.class));
        final StatementListNode statementList = CloneUtils.clone(getChild(1, StatementListNode.class));
        return new FunctionDefinitionNode(newParent, prototype, statementList);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitFunctionDefinition(this);
    }
}

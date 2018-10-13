package com.tazadum.glsl.language.ast.unresolved;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.FixedChildParentNode;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.StatementListNode;
import com.tazadum.glsl.language.ast.traits.UnresolvedNode;
import com.tazadum.glsl.util.SourcePosition;

public class UnresolvedFunctionDefinitionNode extends FixedChildParentNode implements UnresolvedNode {
    public UnresolvedFunctionDefinitionNode(SourcePosition position, UnresolvedFunctionPrototypeNode prototype, StatementListNode statementList) {
        this(position, null, prototype, statementList);
    }

    public UnresolvedFunctionDefinitionNode(SourcePosition position, ParentNode parent, UnresolvedFunctionPrototypeNode prototype, StatementListNode statementList) {
        super(position, 2, parent);
        setChild(0, prototype);
        setChild(1, statementList);
    }

    public void setFunctionPrototype(UnresolvedFunctionPrototypeNode prototype) {
        setChild(0, prototype);
    }

    public UnresolvedFunctionPrototypeNode getFunctionPrototype() {
        return getChildAs(0);
    }

    public void setStatements(StatementListNode node) {
        setChild(1, node);
    }

    public StatementListNode getStatements() {
        return getChildAs(1);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitFunctionDefinition(this);
    }
}

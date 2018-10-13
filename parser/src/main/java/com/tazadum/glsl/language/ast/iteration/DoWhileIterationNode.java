package com.tazadum.glsl.language.ast.iteration;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.FixedChildParentNode;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.traits.IterationNode;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.util.SourcePosition;

public class DoWhileIterationNode extends FixedChildParentNode implements IterationNode {
    public DoWhileIterationNode(SourcePosition position) {
        this(position, null);
    }

    public DoWhileIterationNode(SourcePosition position, ParentNode parentNode) {
        super(position, 2, parentNode);
    }

    public void setCondition(Node condition) {
        setChild(0, condition);
    }

    public Node getCondition() {
        return getChild(0);
    }

    public Node getStatement() {
        return getChild(1);
    }

    public void setStatement(Node statement) {
        setChild(1, statement);
    }

    @Override
    public ParentNode clone(ParentNode newParent) {
        return CloneUtils.cloneChildren(this, new DoWhileIterationNode(getSourcePosition(), newParent));
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitDoWhileIteration(this);
    }

    @Override
    public GLSLType getType() {
        return null;
    }
}

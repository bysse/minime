package com.tazadum.glsl.language.ast.conditional;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.FixedChildParentNode;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.util.SourcePosition;

/**
 * Created by Erik on 2018-10-12.
 */
public class CaseNode extends FixedChildParentNode {
    public CaseNode(SourcePosition position, Node label, Node statements) {
        this(position, null, label, statements);
    }

    public CaseNode(SourcePosition position, ParentNode parentNode, Node label, Node statements) {
        super(position, 2, parentNode);
        setChild(0, label);
        setChild(1, statements);
    }

    /**
     * Must be a scalar integer.
     */
    public Node getLabel() {
        return getChild(0);
    }

    public Node getStatements() {
        return getChild(1);
    }

    @Override
    public ParentNode clone(ParentNode newParent) {
        final CaseNode node = new CaseNode(getSourcePosition(), newParent, null, null);
        return CloneUtils.cloneChildren(this, node);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitSwitchCase(this);
    }

    @Override
    public GLSLType getType() {
        return null;
    }
}

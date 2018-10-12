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
public class DefaultCaseNode extends FixedChildParentNode {
    public DefaultCaseNode(SourcePosition position, Node statements) {
        this(position, null, statements);
    }

    public DefaultCaseNode(SourcePosition position, ParentNode parentNode, Node statements) {
        super(position, 1, parentNode);
        setChild(0, statements);
    }

    public Node getStatements() {
        return getChild(0);
    }

    @Override
    public ParentNode clone(ParentNode newParent) {
        final DefaultCaseNode node = new DefaultCaseNode(getSourcePosition(), newParent, null);
        return CloneUtils.cloneChildren(this, node);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitSwitchDefault(this);
    }

    @Override
    public GLSLType getType() {
        return null;
    }
}

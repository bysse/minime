package com.tazadum.glsl.language.ast.conditional;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.util.SourcePosition;

/**
 * The type of selector in a switch statement must be a scalar integer.
 * The type of each case label must be a scalar integer.
 * Created by Erik on 2018-10-12
 */
public class SwitchNode extends ParentNode {
    public SwitchNode(SourcePosition position, Node selector) {
        this(position, null, selector);
    }

    public SwitchNode(SourcePosition position, ParentNode parentNode, Node selector) {
        super(position, parentNode);
        addChild(selector);
    }

    public Node getSelector() {
        return getChild(0);
    }

    public int getLabelCount() {
        return getChildCount() - 1;
    }

    public Node getLabel(int index) {
        return getChild(1 + index);
    }

    @Override
    public ParentNode clone(ParentNode newParent) {
        final SwitchNode node = new SwitchNode(getSourcePosition(), newParent, null);
        return CloneUtils.cloneChildren(this, node);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitSwitch(this);
    }

    @Override
    public GLSLType getType() {
        return null;
    }
}

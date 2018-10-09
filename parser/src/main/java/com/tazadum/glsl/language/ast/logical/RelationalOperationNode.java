package com.tazadum.glsl.language.ast.logical;

import com.tazadum.glsl.language.RelationalOperator;
import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.FixedChildParentNode;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.language.type.PredefinedType;
import com.tazadum.glsl.util.SourcePositionId;

/**
 * Created by Erik on 2016-10-10.
 */
public class RelationalOperationNode extends FixedChildParentNode {
    private RelationalOperator operator;

    public RelationalOperationNode(SourcePositionId position, RelationalOperator operator) {
        this(position, null, operator);
    }

    public RelationalOperationNode(SourcePositionId position, ParentNode parentNode, RelationalOperator operator) {
        super(position, 2, parentNode);
        this.operator = operator;
    }

    public RelationalOperator getOperator() {
        return operator;
    }

    public void setLeft(Node expression) {
        setChild(0, expression);
    }

    public void setRight(Node expression) {
        setChild(1, expression);
    }

    public Node getLeft() {
        return getChild(0);
    }

    public Node getRight() {
        return getChild(1);
    }

    @Override
    public RelationalOperationNode clone(ParentNode newParent) {
        return CloneUtils.cloneChildren(this, new RelationalOperationNode(getSourcePositionId(), newParent, operator));
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitRelationalOperation(this);
    }

    @Override
    public GLSLType getType() {
        return PredefinedType.BOOL;
    }
}

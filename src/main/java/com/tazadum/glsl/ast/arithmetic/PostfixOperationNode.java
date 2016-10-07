package com.tazadum.glsl.ast.arithmetic;

import com.tazadum.glsl.ast.FixedChildParentNode;
import com.tazadum.glsl.ast.MutatingOperation;
import com.tazadum.glsl.ast.ParentNode;
import com.tazadum.glsl.language.PostfixOperator;
import com.tazadum.glsl.util.CloneUtils;

/**
 * Created by Erik on 2016-10-07.
 */
public class PostfixOperationNode extends FixedChildParentNode implements MutatingOperation {
    private PostfixOperator operator;

    public PostfixOperationNode(PostfixOperator operator) {
        this(null, operator);
    }

    public PostfixOperationNode(ParentNode parentNode, PostfixOperator operator) {
        super(1, parentNode);
        this.operator = operator;
    }

    public PostfixOperator getOperator() {
        return operator;
    }

    @Override
    public PostfixOperationNode clone(ParentNode newParent) {
        return CloneUtils.cloneChildren(this, new PostfixOperationNode(newParent, operator));
    }
}

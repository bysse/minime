package com.tazadum.glsl.optimizer.simplification;

import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.arithmetic.NumericOperationNode;
import com.tazadum.glsl.language.model.NumericOperator;

/**
 * Created by Erik on 2018-03-30.
 */
public class OperationMatcher extends ParentMatcher {
    private NumericOperator operator;

    public OperationMatcher(NumericOperator operator, Matcher... childMatchers) {
        super(childMatchers);
        this.operator = operator;
    }

    @Override
    public boolean doMatch(Node node) {
        if (node instanceof NumericOperationNode) {
            return operator == ((NumericOperationNode) node).getOperator();
        }
        return false;
    }
}

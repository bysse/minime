package com.tazadum.glsl.simplification;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.arithmetic.NumericOperationNode;
import com.tazadum.glsl.language.NumericOperator;

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

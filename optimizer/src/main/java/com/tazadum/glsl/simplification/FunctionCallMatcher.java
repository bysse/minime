package com.tazadum.glsl.simplification;

import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.function.FunctionCallNode;

/**
 * Created by Erik on 2018-03-30.
 */
public class FunctionCallMatcher extends ParentMatcher {
    private String functionName;

    public FunctionCallMatcher(String functionName, Matcher... childMatchers) {
        super(childMatchers);
        this.functionName = functionName;
    }

    @Override
    public boolean doMatch(Node node) {
        if (node instanceof FunctionCallNode) {
            String function = ((FunctionCallNode) node).getIdentifier().original();
            return functionName.equals(function);
        }
        return false;
    }
}

package com.tazadum.glsl.optimizer.simplification.helpers;

import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.arithmetic.NumericLeafNode;
import com.tazadum.glsl.language.ast.arithmetic.NumericOperationNode;
import com.tazadum.glsl.language.ast.expression.ParenthesisNode;
import com.tazadum.glsl.language.ast.function.FunctionCallNode;
import com.tazadum.glsl.language.model.NumericOperator;
import com.tazadum.glsl.language.type.Numeric;
import com.tazadum.glsl.language.type.PredefinedType;
import com.tazadum.glsl.optimizer.simplification.CaptureGroups;
import com.tazadum.glsl.util.SourcePosition;

import java.util.function.Function;

public class Generators {
    public static Function<CaptureGroups, Node> gGroup(int group) {
        return (storage) -> storage.get(group);
    }

    public static <T extends Node> Function<CaptureGroups, Node> gGroup(int group, Class<T> type, Function<T, Node> function) {
        return (storage) -> function.apply(type.cast(storage.get(group)));
    }

    public static Function<CaptureGroups, Node> gClone(int group) {
        return (storage) -> storage.get(group).clone(null);
    }

    public static Function<CaptureGroups, Node> gParentesis(Function<CaptureGroups, Node> a) {
        return (storage) -> {
            Node node = a.apply(storage);
            return new ParenthesisNode(SourcePosition.TOP, node);
        };
    }

    public static Function<CaptureGroups, Node> gFunc(String function, Function<CaptureGroups, Node> c) {
        return (storage) -> {
            Node node = c.apply(storage);
            FunctionCallNode callNode = new FunctionCallNode(SourcePosition.TOP, function);
            callNode.addChild(node);
            return callNode;
        };
    }

    public static Function<CaptureGroups, Node> gOperation(NumericOperator operator, Function<CaptureGroups, Node> a, Function<CaptureGroups, Node> b) {
        return (storage) -> {
            Node left = a.apply(storage);
            Node right = b.apply(storage);
            return new NumericOperationNode(SourcePosition.TOP, operator, left, right);
        };
    }

    public static Function<CaptureGroups, Node> gNumeric(float value) {
        return (storage) -> new NumericLeafNode(SourcePosition.TOP, Numeric.createFloat(value, PredefinedType.FLOAT));
    }

    public static Function<CaptureGroups, Node> gNumeric(int value) {
        return (storage) -> new NumericLeafNode(SourcePosition.TOP, Numeric.createFloat(value, PredefinedType.INT));
    }

}

package com.tazadum.glsl.simplification.helpers;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.arithmetic.FloatLeafNode;
import com.tazadum.glsl.ast.arithmetic.NumericOperationNode;
import com.tazadum.glsl.language.Numeric;
import com.tazadum.glsl.language.NumericOperator;
import com.tazadum.glsl.simplification.CaptureGroups;

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

    public static Function<CaptureGroups, Node> gOperation(NumericOperator operator, Function<CaptureGroups, Node> a, Function<CaptureGroups, Node> b) {
        return (storage) -> {
            Node left = a.apply(storage);
            Node right = b.apply(storage);
            return new NumericOperationNode(operator, left, right);
        };
    }

    public static Function<CaptureGroups, Node> gNumeric(float value) {
        return (storage) -> new FloatLeafNode(new Numeric(value, 0, true));
    }

}

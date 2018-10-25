package com.tazadum.glsl.simplification.helpers;

import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.arithmetic.NumericLeafNode;
import com.tazadum.glsl.language.ast.arithmetic.NumericOperationNode;
import com.tazadum.glsl.language.model.NumericOperator;
import com.tazadum.glsl.language.type.Numeric;
import com.tazadum.glsl.language.type.PredefinedType;
import com.tazadum.glsl.simplification.CaptureGroups;
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

    public static Function<CaptureGroups, Node> gOperation(NumericOperator operator, Function<CaptureGroups, Node> a, Function<CaptureGroups, Node> b) {
        return (storage) -> {
            Node left = a.apply(storage);
            Node right = b.apply(storage);
            return new NumericOperationNode(SourcePosition.TOP, operator, left, right);
        };
    }

    public static Function<CaptureGroups, Node> gNumeric(float value) {
        return (storage) -> new NumericLeafNode(SourcePosition.TOP, new Numeric(value, 0, PredefinedType.FLOAT));
    }

}

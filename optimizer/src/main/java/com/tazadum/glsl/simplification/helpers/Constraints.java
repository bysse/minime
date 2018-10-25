package com.tazadum.glsl.simplification.helpers;

import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.traits.HasNumeric;
import com.tazadum.glsl.language.type.Numeric;
import com.tazadum.glsl.simplification.CaptureGroups;
import com.tazadum.glsl.simplification.NodeComparator;
import com.tazadum.glsl.simplification.TreeNodeComparator;

import java.util.function.Function;

public class Constraints {
    public static Function<CaptureGroups, Boolean> and(Function<CaptureGroups, Boolean>... conditions) {
        return (groups) -> {
            for (Function<CaptureGroups, Boolean> condition : conditions) {
                if (!condition.apply(groups)) {
                    return false;
                }
            }
            return true;
        };
    }

    public static Function<CaptureGroups, Boolean> or(Function<CaptureGroups, Boolean>... conditions) {
        return (groups) -> {
            for (Function<CaptureGroups, Boolean> condition : conditions) {
                if (condition.apply(groups)) {
                    return true;
                }
            }
            return false;
        };
    }

    public static Function<CaptureGroups, Boolean> cApply(int group, Function<Node, Boolean> condition) {
        return (groups) -> condition.apply(groups.get(group));
    }

    public static <T extends Node> Function<CaptureGroups, Boolean> cFunc(int group, Class<T> type, Function<T, Boolean> condition) {
        return (groups) -> {
            final Node node = groups.get(group);
            if (type.isAssignableFrom(node.getClass())) {
                return condition.apply(type.cast(node));
            }
            return false;
        };
    }

    public static Function<CaptureGroups, Boolean> nNumeric(int group, Function<Numeric, Boolean> condition) {
        return cApply(group, (node) -> {
            if (node instanceof HasNumeric) {
                return condition.apply(((HasNumeric) node).getValue());
            }
            return false;
        });
    }

    public static Function<CaptureGroups, Boolean> cSame(int groupA, int groupB, NodeComparator comparator) {
        return (groups) -> comparator.equals(groups.get(groupA), groups.get(groupB));
    }

    public static NodeComparator cSameNumeric() {
        return (a, b) -> {
            if (a instanceof HasNumeric && b instanceof HasNumeric) {
                Numeric aNumber = ((HasNumeric) a).getValue();
                Numeric bNumber = ((HasNumeric) b).getValue();
                return aNumber.equals(bNumber);
            }
            return false;
        };
    }

    public static NodeComparator cSameTree() {
        return new TreeNodeComparator();
    }

}

package com.tazadum.glsl.optimizer.simplification;

import com.tazadum.glsl.language.ast.Node;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Erik on 2018-03-30.
 */
public class CaptureGroups {
    private List<Node> nodes = new ArrayList<>();
    private Set<Integer> references = new HashSet<>();

    public CaptureGroups() {
    }

    public CaptureGroups(Node node) {
        nodes.add(node);
    }

    public CaptureGroups capture(Node node) {
        nodes.add(node);
        return this;
    }

    public Node get(int index) {
        references.add(index);
        return nodes.get(index);
    }

    public List<Node> unreferenced() {
        List<Node> list = new ArrayList<>();
        for (int i = 0; i < nodes.size(); i++) {
            if (references.contains(i)) {
                continue;
            }
            list.add(nodes.get(i));
        }
        return list;
    }

    public void merge(CaptureGroups groups) {
        nodes.addAll(groups.nodes);
    }
}

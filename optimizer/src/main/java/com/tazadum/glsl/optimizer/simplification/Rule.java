package com.tazadum.glsl.optimizer.simplification;

import com.tazadum.glsl.language.ast.Node;

import java.util.List;

/**
 * Created by Erik on 2018-03-30.
 */
public interface Rule {
    boolean matches(Node node);

    Node replacement();

    List<Node> removedNodes();
}

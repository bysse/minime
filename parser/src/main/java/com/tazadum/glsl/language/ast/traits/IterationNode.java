package com.tazadum.glsl.language.ast.traits;

import com.tazadum.glsl.language.ast.Node;

/**
 * Created by Erik on 2016-10-15.
 */
public interface IterationNode {
    Node getStatement();

    void setStatement(Node node);
}

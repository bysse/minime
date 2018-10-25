package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.language.ast.Node;

/**
 * Created by Erik on 2016-10-20.
 */
public interface OptimizationDecider {
    int score(Node node);
}

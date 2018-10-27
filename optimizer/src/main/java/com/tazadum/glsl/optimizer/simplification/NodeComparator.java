package com.tazadum.glsl.optimizer.simplification;

import com.tazadum.glsl.language.ast.Node;

public interface NodeComparator {
    boolean equals(Node a, Node b);
}

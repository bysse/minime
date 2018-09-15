package com.tazadum.glsl.simplification;

import com.tazadum.glsl.ast.Node;

public interface NodeComparator {
    boolean equals(Node a, Node b);
}
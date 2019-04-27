package com.tazadum.glsl.optimizer;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.Node;

import java.util.Collections;
import java.util.List;

public interface OptimizerVisitor extends ASTVisitor<Node> {
    Node applyOn(Node node);

    void reset();

    int getChanges();

    default List<Branch> getBranches() {
        return Collections.emptyList();
    }
}

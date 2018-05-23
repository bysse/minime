package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.ast.ASTVisitor;
import com.tazadum.glsl.ast.Node;

import java.util.Collections;
import java.util.List;

public interface OptimizerVisitor extends ASTVisitor<Node> {
    void reset();

    int getChanges();

    default List<OptimizerBranch> getBranches() {
        return Collections.emptyList();
    }
}

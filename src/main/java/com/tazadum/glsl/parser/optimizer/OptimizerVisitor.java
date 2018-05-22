package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.ast.ASTVisitor;
import com.tazadum.glsl.ast.Node;

public interface OptimizerVisitor extends ASTVisitor<Node> {
    void reset();

    int getChanges();
}

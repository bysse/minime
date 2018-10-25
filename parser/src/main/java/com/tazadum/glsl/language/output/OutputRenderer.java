package com.tazadum.glsl.language.output;


import com.tazadum.glsl.language.ast.Node;

/**
 * Shortcut for using the OutputVisitor.
 */
public class OutputRenderer {
    public String render(Node node, OutputConfig config) {
        final OutputVisitor visitor = new OutputVisitor(config);
        return node.accept(visitor).get();
    }
}

package com.tazadum.glsl.output;

import com.tazadum.glsl.ast.Node;

/**
 * Created by Erik on 2016-10-13.
 */
public class Output {
    public String render(Node node, OutputConfig config) {
        final OutputVisitor visitor = new OutputVisitor(config);
        return node.accept(visitor);
    }
}

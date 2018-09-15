package com.tazadum.glsl.output;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.parser.optimizer.OptimizationDecider;

/**
 * Created by Erik on 2016-10-20.
 */
public class OutputSizeDecider implements OptimizationDecider {
    private final Output output;
    private final OutputConfig config;

    public OutputSizeDecider() {
        output = new Output();
        config = new OutputConfig();
        config.setIdentifiers(IdentifierOutput.None);
        config.setIndentation(0);
        config.setNewlines(false);
        config.setMaxDecimals(5);
    }

    public OutputConfig getConfig() {
        return config;
    }

    @Override
    public int score(Node node) {
        final String source = output.render(node, config);
        return source.length();
    }
}

package com.tazadum.glsl.optimizer;

import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.output.IdentifierOutputMode;
import com.tazadum.glsl.language.output.OutputConfig;
import com.tazadum.glsl.language.output.OutputConfigBuilder;
import com.tazadum.glsl.language.output.OutputRenderer;

/**
 * Created by Erik on 2016-10-20.
 */
public class OutputSizeDecider implements OptimizationDecider {
    private final OutputRenderer output;
    private final OutputConfig config;

    public OutputSizeDecider(int decimals) {
        output = new OutputRenderer();
        config = new OutputConfigBuilder()
            .identifierMode(IdentifierOutputMode.None)
            .renderNewLines(false)
            .indentation(0)
            .significantDecimals(decimals)
            .build();
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

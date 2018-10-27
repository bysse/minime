package com.tazadum.glsl.optimizer.pipeline;

import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.optimizer.OptimizerContext;

public interface OptimizerPipeline {
    Node optimize(OptimizerContext optimizerContext, Node shaderNode, boolean showOutput);
}

package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.GLSLOptimizerContext;
import com.tazadum.glsl.ast.Node;

public interface OptimizerPipeline {
    Node optimize(GLSLOptimizerContext optimizerContext, Node shaderNode, boolean showOutput);
}

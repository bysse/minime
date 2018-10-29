package com.tazadum.glsl.optimizer.pipeline;

import com.tazadum.glsl.cli.OptimizerReport;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.optimizer.Branch;
import com.tazadum.glsl.optimizer.OptimizerContext;

public interface OptimizerPipeline {
    Branch optimize(OptimizerContext optimizerContext, Node shaderNode, boolean showOutput, OptimizerReport report);
}

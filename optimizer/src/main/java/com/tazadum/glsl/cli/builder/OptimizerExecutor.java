package com.tazadum.glsl.cli.builder;

import com.tazadum.glsl.language.output.IdentifierOutputMode;
import com.tazadum.glsl.language.output.OutputConfig;
import com.tazadum.glsl.language.output.OutputConfigBuilder;
import com.tazadum.glsl.optimizer.OptimizerContext;
import com.tazadum.glsl.optimizer.OptimizerType;
import com.tazadum.glsl.optimizer.TreePruner;
import com.tazadum.glsl.optimizer.pipeline.BranchingOptimizerPipeline;
import com.tazadum.glsl.parser.ParserContext;

import java.util.EnumSet;

public class OptimizerExecutor implements ProcessorExecutor<CompilerExecutor.Result> {
    private OutputConfig outputConfig;
    private CompilerExecutor.Result source;
    private EnumSet<OptimizerType> optimizers;

    private int maxIterationDepth = 25;
    private int maxBranchSize = 1024;
    private TreePruner treePruner = null;

    public static OptimizerExecutor create() {
        return new OptimizerExecutor();
    }

    private OptimizerExecutor() {
        optimizers = EnumSet.allOf(OptimizerType.class);

        outputConfig = new OutputConfigBuilder()
                .identifierMode(IdentifierOutputMode.Replaced)
                .indentation(0)
                .renderNewLines(false)
                .blacklistKeyword("out", "const")
                .significantDecimals(4)
                .build();
    }

    public OptimizerExecutor source(CompilerExecutor.Result source) {
        this.source = source;
        return this;
    }

    public OptimizerExecutor outputConfig(OutputConfig config) {
        this.outputConfig = config;
        return this;
    }

    public OptimizerExecutor withOptimizer(OptimizerType optimizer, OptimizerType... optimizers) {
        this.optimizers = EnumSet.of(optimizer, optimizers);
        return this;
    }

    @Override
    public CompilerExecutor.Result process() {
        if (source == null) {
            throw new IllegalArgumentException("No source set!");
        }

        // construct the tree pruning algorithms
        TreePruner pruner = TreePruner.bySizeDifference(maxBranchSize);
        if (treePruner != null) {
            pruner = TreePruner.or(pruner, treePruner);
        }

        // construct the pipeline
        final BranchingOptimizerPipeline pipeline = new BranchingOptimizerPipeline(pruner, outputConfig, maxIterationDepth, optimizers.toArray(new OptimizerType[0]));

        ParserContext parserContext = source.getContext();
        OptimizerContext optimizerContext = new OptimizerContext(parserContext);

        // TODO

        return null;
    }
}

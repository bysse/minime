package com.tazadum.glsl.cli.builder;

import com.tazadum.glsl.cli.OptimizerReport;
import com.tazadum.glsl.exception.SourcePositionException;
import com.tazadum.glsl.language.output.IdentifierOutputMode;
import com.tazadum.glsl.language.output.OutputConfig;
import com.tazadum.glsl.language.output.OutputConfigBuilder;
import com.tazadum.glsl.optimizer.Branch;
import com.tazadum.glsl.optimizer.OptimizerContext;
import com.tazadum.glsl.optimizer.OptimizerType;
import com.tazadum.glsl.optimizer.TreePruner;
import com.tazadum.glsl.optimizer.identifier.ContextBasedMultiIdentifierShortener;
import com.tazadum.glsl.optimizer.pipeline.BranchingOptimizerPipeline;
import com.tazadum.glsl.stage.StageException;
import com.tazadum.glsl.util.SourcePositionId;
import com.tazadum.glsl.util.SourcePositionMapper;

import java.util.EnumSet;

public class OptimizerExecutor implements ProcessorExecutor<CompilerExecutor.Result> {
    private OutputConfig outputConfig;
    private CompilerExecutor.Result source;
    private EnumSet<OptimizerType> optimizers;

    private TreePruner treePruner = null;
    private int maxIterationDepth = 25;
    private int maxBranchSize = 1024;
    private boolean keepIdentifiers = false;
    private boolean keepUniformIdentifiers = false;

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

    public OptimizerExecutor maxIterationDepth(int maxIterationDepth) {
        this.maxIterationDepth = maxIterationDepth;
        return this;
    }

    public OptimizerExecutor maxBranchSizeDiff(int maxBranchSize) {
        this.maxBranchSize = maxBranchSize;
        return this;
    }

    public OptimizerExecutor keepIdentifiers(boolean keepIdentifiers) {
        this.keepIdentifiers = keepIdentifiers;
        return this;
    }

    public OptimizerExecutor keepUniformIdentifiers(boolean keepUniformIdentifiers) {
        this.keepUniformIdentifiers = keepUniformIdentifiers;
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

        final SourcePositionMapper sourceMapper = source.getMapper();
        final OptimizerContext optimizerContext = new OptimizerContext(source.getContext());

        try {
            final OptimizerReport report = new OptimizerReport();
            report.header(sourceMapper.getDefaultId());

            report.mark();
            final Branch result = pipeline.optimize(optimizerContext, source.getNode(), false, report);

            if (!keepIdentifiers) {
                ContextBasedMultiIdentifierShortener shortener = new ContextBasedMultiIdentifierShortener(false, outputConfig, keepUniformIdentifiers);
                shortener.register(result.getContext(), result.getNode());
                shortener.apply();
            }

            report.display();

            return new CompilerExecutor.Result(
                    sourceMapper,
                    result.getNode(),
                    result.getContext()
            );
        } catch (SourcePositionException e) {
            final SourcePositionId sourcePositionId = sourceMapper.map(e.getSourcePosition());
            final String message = sourcePositionId.format() + ": " + e.getMessage();
            throw new StageException(message, e);
        }
    }
}

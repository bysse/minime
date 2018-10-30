package com.tazadum.glsl.stage;

import com.tazadum.glsl.cli.OptimizerReport;
import com.tazadum.glsl.cli.options.OptimizerOptions;
import com.tazadum.glsl.exception.SourcePositionException;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.output.OutputConfig;
import com.tazadum.glsl.optimizer.Branch;
import com.tazadum.glsl.optimizer.OptimizerContext;
import com.tazadum.glsl.optimizer.OptimizerType;
import com.tazadum.glsl.optimizer.TreePruner;
import com.tazadum.glsl.optimizer.identifier.ContextBasedMultiIdentifierShortener;
import com.tazadum.glsl.optimizer.pipeline.BranchingOptimizerPipeline;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.util.Pair;
import com.tazadum.glsl.util.SourcePositionId;
import com.tazadum.glsl.util.SourcePositionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by erikb on 2018-10-26.
 */
public class OptimizerStage implements Stage<Pair<Node, ParserContext>, Pair<Node, ParserContext>> {
    private final Logger logger = LoggerFactory.getLogger(OptimizerStage.class);
    private final OptimizerOptions options;
    private final OutputConfig outputConfig;
    private final OptimizerReport report;

    public OptimizerStage(OptimizerOptions options, OutputConfig outputConfig, OptimizerReport report) {
        this.options = options;
        this.outputConfig = outputConfig;
        this.report = report;
    }

    @Override
    public StageData<Pair<Node, ParserContext>> process(StageData<Pair<Node, ParserContext>> input) {
        // TODO: Make a smarter tree pruning algorithm
        final TreePruner pruner = TreePruner.or(
            TreePruner.byIterationDepth(options.iterationMaxDepth()),
            TreePruner.bySizeDifference(1024) // TODO: this should also some from config
        );
        final BranchingOptimizerPipeline pipeline = new BranchingOptimizerPipeline(pruner, outputConfig, getOptimizers());

        // setup the optimizer context
        final SourcePositionMapper sourcePositionMapper = input.getMapper();
        final Pair<Node, ParserContext> inputData = input.getData();
        final OptimizerContext context = new OptimizerContext(inputData.getSecond());

        try {
            logger.info("* Running the optimizer");
            Branch result = pipeline.optimize(context, inputData.getFirst(), true, report);

            Node node = result.getNode();
            ParserContext parserContext = result.getContext();

            if (!options.keepAllIdentifiers()) {
                optimizeIdentifiers(node, parserContext, options.keepUniformIdentifiers());
            }

            return StageData.from(Pair.create(node, parserContext), sourcePositionMapper);
        } catch (SourcePositionException e) {
            final SourcePositionId sourcePositionId = sourcePositionMapper.map(e.getSourcePosition());
            final String message = sourcePositionId.format() + ": " + e.getMessage();
            throw new StageException(message, e);
        }
    }

    private void optimizeIdentifiers(Node node, ParserContext parserContext, boolean keepUniformIdentifiers) {
        logger.info("* Minifying identifiers");
        ContextBasedMultiIdentifierShortener shortener = new ContextBasedMultiIdentifierShortener(false, outputConfig, keepUniformIdentifiers);
        shortener.register(parserContext, node);
        shortener.apply();

        // TODO: iterate
    }

    private OptimizerType[] getOptimizers() {
        return OptimizerType.values();
    }
}

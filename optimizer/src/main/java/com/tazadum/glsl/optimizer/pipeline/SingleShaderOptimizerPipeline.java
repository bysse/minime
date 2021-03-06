package com.tazadum.glsl.optimizer.pipeline;

import com.tazadum.glsl.cli.OptimizerReport;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.output.OutputConfig;
import com.tazadum.glsl.language.output.OutputRenderer;
import com.tazadum.glsl.optimizer.*;
import com.tazadum.glsl.parser.ParserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SingleShaderOptimizerPipeline implements OptimizerPipeline {
    private final Logger logger = LoggerFactory.getLogger(SingleShaderOptimizerPipeline.class);
    private final OutputConfig outputConfig;
    private final OutputRenderer output;
    private final List<Optimizer> optimizers;

    public SingleShaderOptimizerPipeline(OutputConfig outputConfig, OptimizerType... types) {
        assert outputConfig != null;
        this.outputConfig = outputConfig;
        this.output = new OutputRenderer();
        this.optimizers = Stream.of(types)
                .map(OptimizerType::instantiate)
                .collect(Collectors.toList());
    }

    public SingleShaderOptimizerPipeline(OutputConfig outputConfig, EnumSet<OptimizerType> types) {
        assert outputConfig != null;
        this.outputConfig = outputConfig;
        this.output = new OutputRenderer();
        this.optimizers = types.stream()
                .map(OptimizerType::instantiate)
                .collect(Collectors.toList());
    }

    @Override
    public Branch optimize(OptimizerContext optimizerContext, Node shaderNode, boolean showOutput, OptimizerReport report) {
        final OutputSizeDecider decider = new OutputSizeDecider(outputConfig.getFormatter().getSignificantDigits());
        final ParserContext parserContext = optimizerContext.parserContext();
        final BranchRegistry branchRegistry = optimizerContext.branchRegistry();

        Branch branch = new Branch(parserContext, shaderNode);

        int iterationChanges, iteration = 0;
        do {
            iterationChanges = 0;

            if (showOutput || logger.isDebugEnabled()) {
                final String source = output.render(branch.getNode(), outputConfig);
                final int size = source.length();
                logger.info(String.format("Iteration %d: %d bytes", iteration++, size));
            }


            for (Optimizer optimizer : optimizers) {
                final Optimizer.OptimizerResult result = optimizer.run(branchRegistry, decider, branch);
                int changes = result.getChanges();
                if (changes > 0) {
                    branch = result.getInputBranch();
                    iterationChanges += changes;

                    if (showOutput || logger.isDebugEnabled()) {
                        logger.info(String.format("  - %s: %d changes", optimizer.name(), changes));
                    }
                }
            }

        } while (iterationChanges > 0);

        return branch;
    }
}

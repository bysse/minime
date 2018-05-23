package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.GLSLOptimizerContext;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.output.Output;
import com.tazadum.glsl.output.OutputConfig;
import com.tazadum.glsl.output.OutputSizeDecider;
import com.tazadum.glsl.parser.ParserContext;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SingleShaderOptimizerPipeline implements OptimizerPipeline {
    private final OutputConfig outputConfig;
    private final Output output;
    private final List<Optimizer> optimizers;

    public SingleShaderOptimizerPipeline(OutputConfig outputConfig, OptimizerType... types) {
        assert outputConfig != null;
        this.outputConfig = outputConfig;
        this.output = new Output();
        this.optimizers = Stream.of(types)
                .map(OptimizerType::instantiate)
                .collect(Collectors.toList());
    }

    public SingleShaderOptimizerPipeline(OutputConfig outputConfig, EnumSet<OptimizerType> types) {
        assert outputConfig != null;
        this.outputConfig = outputConfig;
        this.output = new Output();
        this.optimizers = types.stream()
                .map(OptimizerType::instantiate)
                .collect(Collectors.toList());
    }

    @Override
    public Node optimize(GLSLOptimizerContext optimizerContext, Node shaderNode, boolean showOutput) {
        final OutputSizeDecider decider = new OutputSizeDecider();
        final ParserContext parserContext = optimizerContext.parserContext();

        Node node = shaderNode;

        int iterationChanges, iteration = 0;
        do {
            iterationChanges = 0;

            if (showOutput) {
                final int size = output.render(node, outputConfig).length();
                System.out.println(String.format("Iteration #%d: %d bytes", iteration++, size));
            }

            for (Optimizer optimizer : optimizers) {
                final Optimizer.OptimizerResult result = optimizer.run(parserContext, decider, node);
                int changes = result.getChanges();
                if (changes > 0 ) {
                    node = result.getBranches().get(0).getNode();
                    iterationChanges += changes;

                    if (showOutput) {
                        System.out.println(String.format("  - %d %s", changes, optimizer.name()));
                    }
                }
            }

        } while (iterationChanges > 0);
        return node;
    }
}

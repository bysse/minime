package com.tazadum.glsl.cli.options;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import org.slf4j.Logger;

import java.nio.file.Path;

/**
 * Created by erikb on 2018-10-24.
 */
public class OptimizerOptions implements CLIOptions {
    private static final String KEEP_IDENTIFIERS = "fkeep-identifiers";
    private static final String KEEP_UNIFORMS = "fkeep-uniforms";
    private static final String OPTIMIZE_SMALL = "Os";

    private OptionSpec<Integer> maxIterationSpec;
    private OptionSpec<Integer> maxSizeSpec;

    private boolean keepAllIdentifiers;
    private boolean keepUniformIdentifiers;
    private boolean optimizeSmall;

    private int maxIterations = 25;
    private int branchMaxSize = 1024;


    public OptimizerOptions() {
    }

    @Override
    public void configure(OptionParser parser) {
        parser.accepts(KEEP_IDENTIFIERS, "Don't change any identifiers in the output.");
        parser.accepts(KEEP_UNIFORMS, "Don't change the identifiers on the uniforms.");
        parser.accepts(OPTIMIZE_SMALL, "Set standard settings for small output");

        maxIterationSpec = parser.accepts("max-iterations", "Set the maximum number of iterations to run.")
                .withRequiredArg().describedAs("INT").ofType(Integer.class).defaultsTo(maxIterations);

        maxSizeSpec = parser.accepts("max-size", "The maximum size difference towards the smallest branch until a branch gets pruned")
                .withRequiredArg().describedAs("INT").ofType(Integer.class).defaultsTo(branchMaxSize);
    }

    @Override
    public boolean handle(OptionSet optionSet, Logger logger) {
        this.keepAllIdentifiers = optionSet.has(KEEP_IDENTIFIERS);
        this.keepUniformIdentifiers = optionSet.has(KEEP_UNIFORMS);
        this.optimizeSmall = optionSet.has(OPTIMIZE_SMALL);
        this.maxIterations = maxIterationSpec.value(optionSet);
        this.branchMaxSize = maxSizeSpec.value(optionSet);

        return true;
    }

    @Override
    public Path generateOutput(Path inputPath) {
        String name = inputPath.toFile().getName();
        return inputPath.toAbsolutePath().getParent().resolve(name + ".min.glsl");
    }

    public boolean keepAllIdentifiers() {
        return keepAllIdentifiers;
    }

    public boolean keepUniformIdentifiers() {
        return keepUniformIdentifiers;
    }

    public int iterationMaxDepth() {
        return maxIterations;
    }

    public boolean isOptimizeSmall() {
        return optimizeSmall;
    }

    public int branchMaxSize() {
        return branchMaxSize;
    }

    public void setKeepAllIdentifiers(boolean keepAllIdentifiers) {
        this.keepAllIdentifiers = keepAllIdentifiers;
    }

    public void setKeepUniformIdentifiers(boolean keepUniformIdentifiers) {
        this.keepUniformIdentifiers = keepUniformIdentifiers;
    }

    public void setOptimizeSmall(boolean optimizeSmall) {
        this.optimizeSmall = optimizeSmall;
    }

    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    public void setBranchMaxSize(int branchMaxSize) {
        this.branchMaxSize = branchMaxSize;
    }
}

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

    private OptionSpec<Integer> maxDepthSpec;

    private boolean keepAllIdentifiers;
    private boolean keepUniformIdentifiers;
    private int maxDepth;
    private boolean optimizeSmall;


    public OptimizerOptions() {
    }

    @Override
    public void configure(OptionParser parser) {
        parser.accepts(KEEP_IDENTIFIERS, "Don't change any identifiers in the output.");
        parser.accepts(KEEP_UNIFORMS, "Don't change the identifiers on the uniforms.");
        parser.accepts(OPTIMIZE_SMALL, "Set standard settings for small output");

        maxDepthSpec = parser.accepts("max-depth", "Change the max depth of the optimizer search tree.")
            .withRequiredArg().describedAs("INT").ofType(Integer.class).defaultsTo(5);

    }

    @Override
    public boolean handle(OptionSet optionSet, Logger logger) {
        this.keepAllIdentifiers = optionSet.has(KEEP_IDENTIFIERS);
        this.keepUniformIdentifiers = optionSet.has(KEEP_UNIFORMS);
        this.maxDepth = maxDepthSpec.value(optionSet);
        this.optimizeSmall = optionSet.has(OPTIMIZE_SMALL);

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
        return maxDepth;
    }

    public boolean isOptimizeSmall() {
        return optimizeSmall;
    }
}

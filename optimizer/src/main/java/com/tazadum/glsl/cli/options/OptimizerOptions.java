package com.tazadum.glsl.cli.options;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.regex.Pattern;

/**
 * Created by erikb on 2018-10-24.
 */
public class OptimizerOptions implements CLIOptions {
    private static final Pattern keyPattern = Pattern.compile("[a-zA-Z]+");

    private OptionSpec<Boolean> newlineSpec;

    public OptimizerOptions() {
    }

    @Override
    public void configure(OptionParser parser) {
        //newlineSpec = parser.accepts("fnew-line", "If newlines are of be rendered in the output")
        //    .withRequiredArg().ofType(Boolean.class).describedAs("value").defaultsTo(newlineDefault);
    }

    @Override
    public boolean handle(OptionSet optionSet, Logger logger) {
        // handle new lines
        //newLines = newlineSpec.value(optionSet);

        return true;
    }

    @Override
    public Path generateOutput(Path inputPath) {
        String name = inputPath.toFile().getName();
        return inputPath.toAbsolutePath().getParent().resolve(name + ".min.glsl");
    }
}

package com.tazadum.glsl.cli.options;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.slf4j.Logger;

import java.nio.file.Path;


/**
 * Created by erikb on 2018-10-24.
 */
public interface CLIOptions {
    void configure(OptionParser parser);

    boolean handle(OptionSet optionSet, Logger logger);

    Path generateOutput(Path inputPath);
}

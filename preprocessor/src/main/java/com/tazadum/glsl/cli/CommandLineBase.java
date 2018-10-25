package com.tazadum.glsl.cli;

import com.tazadum.glsl.cli.options.CLIOptions;
import com.tazadum.glsl.cli.options.Formatter;
import com.tazadum.slf4j.LoggerConfig;
import com.tazadum.slf4j.TLogConfiguration;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

/**
 * Created by erikb on 2018-10-24.
 */
public class CommandLineBase {
    static {
        TLogConfiguration.get().useGlobalConfiguration();
    }

    private final Logger logger = LoggerFactory.getLogger(CommandLineBase.class);
    private final InputOutput NO_RESULT = null;

    private final OptionParser parser;
    private final CLIOptions options;
    private final String main;
    private final LoggerConfig loggerConfig;

    public CommandLineBase(CLIOptions options, String main) {
        this.loggerConfig = TLogConfiguration.get().getConfig();

        this.loggerConfig.setTraceLabel("");
        this.loggerConfig.setDebugLabel("");
        this.loggerConfig.setInfoLabel("");

        this.options = options;
        this.main = main;
        this.parser = new OptionParser(false);
        this.parser.formatHelpWith(new Formatter(120, 5));

        parser.accepts("o", "Name of the output file.").withRequiredArg().describedAs("FILE");

        parser.accepts("h", "Shows this help screen.");
        parser.accepts("v", "Increase output verbosity.");
        parser.accepts("vv", "Increase output verbosity even more.");

        options.configure(parser);
    }

    public InputOutput process(String[] args) {
        try {
            OptionSet optionSet = parser.parse(args);

            if (optionSet.has("v")) {
                loggerConfig.setLogLevel(Level.DEBUG);
            }
            if (optionSet.has("vv")) {
                loggerConfig.setLogLevel(Level.TRACE);
            }

            if (optionSet.has("h")) {
                showHelp();
                return NO_RESULT;
            }

            if (!options.handle(optionSet, logger)) {
                return NO_RESULT;
            }

            final List<?> arguments = optionSet.nonOptionArguments();
            if (arguments.isEmpty()) {
                logger.error("No input file specified!");
                return NO_RESULT;
            }

            if (arguments.size() > 1) {
                logger.error("Multiple input files specified! Only one is supported.");
                return NO_RESULT;
            }

            final Path inputPath = Paths.get(Objects.toString(arguments.get(0)));
            if (!Files.exists(inputPath)) {
                logger.error("File does not exist : {}", inputPath.toAbsolutePath());
                return NO_RESULT;
            }

            if (!Files.isReadable(inputPath)) {
                logger.error("Can't read file : {}", inputPath.toAbsolutePath());
                return NO_RESULT;
            }

            if (optionSet.has("o")) {
                Path outputPath = Paths.get(Objects.toString(optionSet.valuesOf("o")));
                return new InputOutput(inputPath, outputPath);
            }

            return new InputOutput(inputPath, options.generateOutput(inputPath));
        } catch (Exception e) {
            logger.error("Error parsing command line", e);
            showHelp();
            return NO_RESULT;
        }
    }

    private void showHelp() {
        try {
            PrintStream out = System.out;

            out.format("Syntax: %s <options> <source file>\n\n", main);

            parser.printHelpOn(out);
        } catch (IOException e) {
            logger.error("Error while printing CLI help page", e);
        }
    }

    public static class InputOutput {
        private Path input;
        private Path output;

        public InputOutput(Path input, Path output) {
            this.input = input;
            this.output = output;
        }

        public Path getInput() {
            return input;
        }

        public Path getOutput() {
            return output;
        }
    }
}

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

    public static final int RET_OK = 0;
    public static final int RET_SYNTAX = 1;
    public static final int RET_EXCEPTION = 2;

    private final Logger logger = LoggerFactory.getLogger(CommandLineBase.class);
    private final InputOutput NO_RESULT = null;

    private final OptionParser parser;
    private final CLIOptions[] options;
    private final String main;
    private final LoggerConfig loggerConfig;
    private String header;

    public CommandLineBase(String main, String header, CLIOptions... options) {
        this.header = header;
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

        for (CLIOptions option : options) {
            option.configure(parser);
        }
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
                showHelp(true);
                return NO_RESULT;
            }

            for (CLIOptions option : options) {
                if (!option.handle(optionSet, logger)) {
                    return NO_RESULT;
                }
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

            // get the last option module to generate output
            final Path outputPath = options[options.length - 1].generateOutput(inputPath);
            return new InputOutput(inputPath, outputPath);
        } catch (Exception e) {
            logger.error("Error parsing command line", e);
            showHelp(false);
            return NO_RESULT;
        }
    }

    protected void showHelp(boolean showHeader) {
        try {
            PrintStream out = System.out;
            if (showHeader) {
                out.print(header);
                out.print("\n");
            }
            out.format("\nSyntax: %s <options> <source file>\n\n", main);

            parser.printHelpOn(out);
        } catch (IOException e) {
            logger.error("Error while printing CLI help page", e);
        }
    }

    public static class InputOutput {
        private Path input;
        private Path output;

        InputOutput(Path input, Path output) {
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

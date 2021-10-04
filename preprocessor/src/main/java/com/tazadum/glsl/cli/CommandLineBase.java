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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by erikb on 2018-10-24.
 */
public class CommandLineBase {
    static {
        TLogConfiguration.get().useGlobalConfiguration();
    }

    static final int RET_OK = 0;
    static final int RET_SYNTAX = 1;
    static final int RET_EXCEPTION = 2;
    static final InputOutput NO_RESULT = null;

    private final Logger logger = LoggerFactory.getLogger(CommandLineBase.class);

    private final OptionParser parser;
    private final CLIOptions[] options;
    private final String main;
    private final LoggerConfig loggerConfig;
    private final String header;
    private final boolean multipleInputs;
    private final List<InputOutput> inputOutputs;

    private boolean singleOutput = false;
    private boolean alreadyPrinted = false;

    CommandLineBase(String main, String header, boolean multipleInputs, CLIOptions... options) {
        this.header = header;
        this.multipleInputs = multipleInputs;
        this.loggerConfig = TLogConfiguration.get().getConfig();

        this.loggerConfig.setTraceLabel("");
        this.loggerConfig.setDebugLabel("");
        this.loggerConfig.setInfoLabel("");

        this.inputOutputs = new ArrayList<>();

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

            if (!multipleInputs && arguments.size() > 1) {
                logger.error("Multiple input files specified! Only one is supported.");
                return NO_RESULT;
            }

            singleOutput = multipleInputs && optionSet.has("o");

            for (Object argument : arguments) {
                final Path inputPath = Paths.get(Objects.toString(argument));
                if (!Files.exists(inputPath)) {
                    logger.error("File does not exist : {}", inputPath.toAbsolutePath());
                    return NO_RESULT;
                }

                if (!Files.isReadable(inputPath)) {
                    logger.error("Can't read file : {}", inputPath.toAbsolutePath());
                    return NO_RESULT;
                }

                if (optionSet.has("o")) {
                    final String output = Objects.toString(optionSet.valueOf("o"));

                    Path outputPath = Paths.get(output);
                    if (!outputPath.isAbsolute()) {
                        outputPath = outputPath.toAbsolutePath();
                    }

                    if (Files.isDirectory(outputPath)) {
                        // if the outputPath is a directory, store all files in there
                        outputPath = outputPath.resolve(inputPath.getFileName());
                        singleOutput = false;
                    }

                    inputOutputs.add(new InputOutput(inputPath, outputPath));
                } else {
                    // get the last option module to generate output
                    final Path outputPath = options[options.length - 1].generateOutput(inputPath);
                    inputOutputs.add(new InputOutput(inputPath, outputPath));
                }
            }

            return inputOutputs.get(0);
        } catch (Exception e) {
            logger.error("Error parsing command line", e);
            showHelp(false);
            return NO_RESULT;
        }
    }

    boolean isSingleOutput() {
        return singleOutput;
    }

    List<InputOutput> getInputOutputs() {
        return inputOutputs;
    }

    void showHelp(boolean showHeader) {
        if (alreadyPrinted) {
            return;
        }
        alreadyPrinted = true;
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

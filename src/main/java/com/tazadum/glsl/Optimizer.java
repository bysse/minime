package com.tazadum.glsl;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Erik on 2016-10-24.
 */
public class Optimizer {
    private static final String OUTPUT_FILE = "o";
    private static final String OUTPUT_FORMAT = "f";
    private static final String OUTPUT_SHADER_TOY = "t";
    private static final String OUTPUT_STATISTICS = "s";
    private static final String OUTPUT_PREFERENCE = "p";

    public static void displayHelp() {
        System.out.println("Syntax: ");
    }

    public static void main(String[] args) throws FileNotFoundException {
        final OptionParser parser = new OptionParser("o:f:tsp:");
        final OptionSet options = parser.parse(args);

        if (options.nonOptionArguments().isEmpty()) {
            System.err.println("Error: Missing input shader!");
            displayHelp();
            System.exit(1);
        }

        OutputWriter outputFunction = new OutputWriter.StdOutOutputWriter();
        if (options.has(OUTPUT_FILE)) {
            final String filename = String.valueOf(options.valueOf(OUTPUT_FILE));
            outputFunction = new OutputWriter.FileOutputWriter(filename);
        }

        OutputProfile profile = OutputProfile.GLSL;
        if (options.has(OUTPUT_FORMAT)) {
            profile = OutputProfile.valueOf(String.valueOf(options.valueOf(OUTPUT_FORMAT)));
        }

        try {
            final GLSLOptimizer optimizer = new GLSLOptimizer(outputFunction, profile);
            if (options.has(OUTPUT_SHADER_TOY)) {
                optimizer.addShaderToySupport();
            }
            if (options.has(OUTPUT_STATISTICS)) {
                optimizer.showStatistics();
            }
            if (options.has(OUTPUT_PREFERENCE)) {
                final Set<Preference> preferences = options.valuesOf(OUTPUT_PREFERENCE).stream()
                    .map(String::valueOf)
                    .map(Preference::valueOf)
                    .collect(Collectors.toSet());
                optimizer.setPreferences(preferences);
            }

            List<String> shaderList = options.nonOptionArguments().stream()
                    .map(String::valueOf)
                    .collect(Collectors.toList());

            optimizer.execute(shaderList);
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());

            throw new RuntimeException(e);
        }
    }
}

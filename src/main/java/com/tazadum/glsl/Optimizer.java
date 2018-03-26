package com.tazadum.glsl;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

import java.io.IOException;
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

    public static void main(String[] args) throws IOException {
        final OptionParser parser = new OptionParser();
        parser.accepts(OUTPUT_FILE, "Output file.").withRequiredArg();
        parser.accepts(OUTPUT_FORMAT, "Format of output [GLSL, C].").withRequiredArg();
        parser.accepts(OUTPUT_SHADER_TOY, "Output ShaderToy compatible shaders.");
        parser.accepts(OUTPUT_STATISTICS, "Show statistics during optimization.");
        parser.accepts(OUTPUT_PREFERENCE, "Set preference flag [NO_MACRO, LINE_BREAKS, NO_TYPE_CONVERSION, PASS_THROUGH, NO_RENAMING]").withRequiredArg();
        final OptionSet options = parser.parse(args);

        if (options.nonOptionArguments().isEmpty()) {
            System.err.println("Error: Missing input shader!");
            parser.printHelpOn(System.err);
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

            List<GLSLSource> shaderList = options.nonOptionArguments().stream()
                    .map(obj -> new GLSLSource(String.valueOf(obj), null))
                    .collect(Collectors.toList());

            optimizer.process(shaderList);
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());

            throw new RuntimeException(e);
        }
    }
}

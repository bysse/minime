package com.tazadum.glsl;

import com.tazadum.glsl.input.GLSLSource;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Erik on 2016-10-24.
 */
public class Optimizer {
    private static final String OUTPUT_FILE = "o";
    private static final String OUTPUT_FORMAT = "f";

    public static void main(String[] args) throws IOException {
        final OptionParser parser = new OptionParser();
        parser.accepts(OUTPUT_FILE, "Output file.").withRequiredArg();
        parser.accepts(OUTPUT_FORMAT, "Format of output [GLSL, C].").withRequiredArg();


        final Map<Option, Boolean> optionMap = new HashMap<>();
        for (Option flag : Option.values()) {
            parser.accepts(flag.getSymbol(), flag.getDescription());
            optionMap.put(flag, flag.getDefault());
        }

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

        // resolve all flags
        for (Option flag : Option.values()) {
            if (options.has(flag.getSymbol())) {
                optionMap.put(flag, !flag.getDefault());
            }
        }

        try {
            final GLSLOptimizer optimizer = new GLSLOptimizer(outputFunction, profile);
            optimizer.setOptions(optionMap);

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

package com.tazadum.glsl;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by Erik on 2016-10-24.
 */
public class Optimizer {
    private static final String OUTPUT_FILE = "o";
    private static final String OUTPUT_FORMAT = "f";

    public static void displayHelp() {
        System.out.println("Syntax: ");
    }

    public static void main(String[] args) throws FileNotFoundException {
        final OptionParser parser = new OptionParser("o:f:t");
        final OptionSet options = parser.parse(args);

        if (options.nonOptionArguments().isEmpty()) {
            System.err.println("Error: Missing input shader!");
            displayHelp();
            System.exit(1);
        }

        OutputStreamProvider outputFunction = () -> System.out;
        if (options.has(OUTPUT_FILE)) {
            final String filename = String.valueOf(options.valueOf(OUTPUT_FILE));
            final FileOutputStream fileOutputStream = new FileOutputStream(filename);
            outputFunction = () -> fileOutputStream;
        }

        OutputProfile profile = OutputProfile.GLSL;
        if (options.has(OUTPUT_FORMAT)) {
            profile = OutputProfile.valueOf(String.valueOf(options.valueOf(OUTPUT_FORMAT)));
        }

        try {
            final GLSLOptimizer optimizer = new GLSLOptimizer(outputFunction, profile);
            if (options.has("t")) {
                optimizer.addShaderToySupport();
            }

            final String shader = String.valueOf(options.nonOptionArguments().get(0));
            optimizer.execute(shader);
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
            System.exit(2);
        }
    }
}

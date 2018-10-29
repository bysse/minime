package com.tazadum.glsl.cli.options;

import com.tazadum.glsl.preprocessor.language.GLSLVersion;
import com.tazadum.glsl.util.Pair;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by erikb on 2018-10-24.
 */
public class PreprocessorOptions implements CLIOptions {
    private static final Pattern keyPattern = Pattern.compile("[a-zA-Z][a-zA-Z0-9_-]*");
    private static final Pattern keyValuePattern = Pattern.compile("([a-zA-Z][a-zA-Z0-9_-]*)=([a-zA-Z0-9_-]+)");

    private OptionSpec<String> versionSpec;
    private OptionSpec<String> macroSpec;

    private GLSLVersion glslVersion;
    private List<Pair<String, String>> objectLike = new ArrayList<>();
    private boolean outputIntermediateResult;

    @Override
    public void configure(OptionParser parser) {
        versionSpec = parser.accepts("fglsl-version", "Sets the OpenGL version used in preprocessing.")
            .withRequiredArg().describedAs("int")
            .defaultsTo(Objects.toString(GLSLVersion.OpenGL46.getVersionCode()));

        macroSpec = parser.accepts("D", "Define an object-like macro in the preprocessor.")
            .withRequiredArg().describedAs("macro");

        parser.accepts("E", "Output the results of the preprocessor.");
    }

    @Override
    public boolean handle(OptionSet optionSet, Logger logger) {
        try {
            int glVersion = Integer.parseInt(versionSpec.value(optionSet));

            for (GLSLVersion version : GLSLVersion.values()) {
                if (version.getVersionCode() == glVersion) {
                    glslVersion = version;
                    logger.trace("Setting GLSL Version to {}", glslVersion.getVersionName());
                    break;
                }
            }

            if (glslVersion == null) {
                logger.error("Unrecognized GL version '{}'", glVersion);
                printVersionExpectations(logger);
                return false;
            }
        } catch (NumberFormatException e) {
            logger.error("Invalid version code");
            printVersionExpectations(logger);
            return false;
        }

        for (String value : macroSpec.values(optionSet)) {
            value = value.trim();
            Matcher matcher = keyValuePattern.matcher(value);
            if (matcher.matches()) {
                objectLike.add(Pair.create(matcher.group(1), matcher.group(2)));
                continue;
            }

            matcher = keyPattern.matcher(value);
            if (!matcher.matches()) {
                logger.error("Invalid object-like macro format, expected name or name=value : {}", value);
                return false;
            }
            objectLike.add(Pair.create(value, "1"));
        }

        outputIntermediateResult = optionSet.has("E");

        return true;
    }

    @Override
    public Path generateOutput(Path inputPath) {
        return Paths.get(inputPath.toAbsolutePath().toString() + ".i");
    }

    private void printVersionExpectations(Logger logger) {
        logger.error("Expected one of:");

        for (GLSLVersion version : GLSLVersion.values()) {
            logger.error(String.format("  %3d: %s", version.getVersionCode(), version.getVersionName()));
        }
    }

    public GLSLVersion getGLSLVersion() {
        return glslVersion;
    }

    public List<Pair<String, String>> getObjectLike() {
        return objectLike;
    }

    public boolean outputIntermediateResult() {
        return outputIntermediateResult;
    }
}

package com.tazadum.glsl.cli.options;

import com.tazadum.glsl.parser.ShaderType;
import com.tazadum.glsl.preprocessor.language.GLSLProfile;
import com.tazadum.glsl.preprocessor.model.HasToken;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by erikb on 2018-10-24.
 */
public class CompilerOptions implements CLIOptions {
    private static final Pattern keyPattern = Pattern.compile("[a-zA-Z]+");
    private final int indentationDefault;
    private final boolean newlineDefault;

    private OptionSpec<String> typeSpec;
    private OptionSpec<String> profileSpec;
    private OptionSpec<String> formatSpec;
    private OptionSpec<String> blacklistSpec;
    private OptionSpec<Integer> indentationSpec;
    private OptionSpec<Boolean> newlineSpec;
    private OptionSpec<String> shaderIdSpec;

    private ShaderType shaderType;
    private GLSLProfile profile;
    private String shaderId;
    private Set<String> keywords;
    private int indentation;
    private boolean newLines;
    private OutputFormat outputFormat;

    public CompilerOptions(int indentationDefault, boolean newlineDefault) {
        this.indentationDefault = indentationDefault;
        this.newlineDefault = newlineDefault;
    }

    @Override
    public void configure(OptionParser parser) {
        typeSpec = parser.accepts("type", "Set the GLSL Shader type.")
                .withRequiredArg().describedAs("type")
                .defaultsTo(ShaderType.FRAGMENT.name().toLowerCase());

        profileSpec = parser.accepts("profile", "Set the OpenGL profile.")
                .withRequiredArg().describedAs("profile")
                .defaultsTo(GLSLProfile.COMPATIBILITY.token());

        formatSpec = parser.accepts("format", "Set the output format")
                .withRequiredArg().describedAs("fmt")
                .defaultsTo(OutputFormat.PLAIN.token());

        shaderIdSpec = parser.accepts("id", "Set the shader id, used for C-header generation.")
                .withRequiredArg().describedAs("id");

        blacklistSpec = parser.accepts("no-render", "Black lists a keyword that will be omitted from rendering.")
                .withRequiredArg().describedAs("keyword");

        indentationSpec = parser.accepts("fidentation", "The number of spaces to use for output indentation")
                .withRequiredArg().ofType(Integer.class).describedAs("int").defaultsTo(indentationDefault);

        newlineSpec = parser.accepts("fnew-line", "Control if newline characters are rendered in the output")
                .withRequiredArg().ofType(Boolean.class).describedAs("value").defaultsTo(newlineDefault);
    }

    @Override
    public boolean handle(OptionSet optionSet, Logger logger) {
        // handle the shader type
        final String typeArg = typeSpec.value(optionSet).toLowerCase();
        int typeArgInt;
        try {
            typeArgInt = Integer.parseInt(typeArg);
        } catch (NumberFormatException e) {
            typeArgInt = -1;
        }

        shaderType = null;
        for (ShaderType type : ShaderType.values()) {
            if (typeArgInt == type.tokenId() ||
                    type.token().equals(typeArg) ||
                    type.name().toLowerCase().equals(typeArg)) {
                shaderType = type;
                break;
            }
        }

        if (shaderType == null) {
            logger.error("Unrecognized shader type '{}'", typeArg);
            printShaderTypeExpectations(logger);
            return false;
        }

        // handle the profile
        final String profileArg = profileSpec.value(optionSet).toLowerCase();
        profile = HasToken.fromString(profileArg, GLSLProfile.values());

        if (profile == null) {
            logger.error("Unrecognized GL profile '{}'", profileArg);
            printProfileExpectations(logger);
            return false;
        }

        // handle shader id
        shaderId = shaderIdSpec.value(optionSet);

        // handle output format
        outputFormat = HasToken.fromString(formatSpec.value(optionSet), OutputFormat.values());
        if (outputFormat == null) {
            logger.error("Unrecognized output format '{}'", formatSpec.value(optionSet));
            printFormatExpectations(logger);
            return false;
        }

        // handle keyword black list
        keywords = new HashSet<>();
        for (String value : blacklistSpec.values(optionSet)) {
            value = value.trim();
            Matcher matcher = keyPattern.matcher(value);
            if (!matcher.matches()) {
                logger.error("Invalid keyword format: {}", value);
                return false;
            }
            keywords.add(value);
        }

        // handle indentation
        indentation = indentationSpec.value(optionSet);
        if (indentation < 0 || indentation > 8) {
            logger.error("Indentation must be inside the range [0,8]");
            return false;
        }

        // handle new lines
        newLines = newlineSpec.value(optionSet);

        return true;
    }

    @Override
    public Path generateOutput(Path inputPath) {
        String name = inputPath.toFile().getName();
        switch (outputFormat) {
            case SHADERTOY:
            case PLAIN:
                return inputPath.toAbsolutePath().getParent().resolve(name + ".min.glsl");
            case C_HEADER:
                return inputPath.toAbsolutePath().getParent().resolve(name + ".h");
            default:
                throw new UnsupportedOperationException("Unsupported format option : " + outputFormat);
        }
    }

    private void printFormatExpectations(Logger logger) {
        logger.error("Expected one of:");

        for (OutputFormat format : OutputFormat.values()) {
            logger.error(String.format("  %s", format.token()));
        }
    }

    private void printShaderTypeExpectations(Logger logger) {
        logger.error("Expected one of:");

        for (ShaderType type : ShaderType.values()) {
            logger.error(String.format("  %2d: [%s] %s", type.tokenId(), type.token(), type.name().toLowerCase()));
        }
    }

    private void printProfileExpectations(Logger logger) {
        logger.error("Expected one of:");

        for (GLSLProfile type : GLSLProfile.values()) {
            logger.error(String.format("  %s", type.token()));
        }
    }

    public ShaderType getShaderType() {
        return shaderType;
    }

    public GLSLProfile getProfile() {
        return profile;
    }

    public String getShaderId(Path inputPath) {
        if (shaderId == null) {
            return inputPath.toFile().getName();
        }
        return shaderId;
    }

    public Set<String> getKeywords() {
        return keywords;
    }

    public int getIndentation() {
        return indentation;
    }

    public boolean isNewLines() {
        return newLines;
    }

    public OutputFormat getOutputFormat() {
        return outputFormat;
    }
}

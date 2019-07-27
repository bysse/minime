package com.tazadum.glsl.cli.builder;

import com.tazadum.glsl.cli.options.OutputFormat;
import com.tazadum.glsl.language.output.IdentifierOutputMode;
import com.tazadum.glsl.language.output.OutputConfig;
import com.tazadum.glsl.language.output.OutputConfigBuilder;
import com.tazadum.glsl.preprocessor.language.GLSLVersion;
import com.tazadum.glsl.stage.HeaderRenderStage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class OutputExecutor implements ProcessorExecutor<String> {
    private static Logger logger = LoggerFactory.getLogger(OutputExecutor.class);

    private final OutputFormat outputFormat;
    private GLSLVersion version = GLSLVersion.OpenGL45;
    private String shaderId;

    private IdentifierOutputMode mode = IdentifierOutputMode.Original;
    private int indentation = 4;
    private int maxDecimals = 5;
    private boolean renderNewLines = true;

    private Set<String> blacklistedKeywords;
    private OutputConfig configuration;
    private String header="";
    private CompilerExecutor.Result result;


    public static OutputExecutor create(OutputFormat outputFormat) {
        return new OutputExecutor(outputFormat);
    }

    private OutputExecutor(OutputFormat outputFormat) {
        this.outputFormat = outputFormat;
        this.blacklistedKeywords = new HashSet<>();
    }

    public OutputExecutor source(CompilerExecutor.Result result) {
        this.result = result;
        return this;
    }

    public OutputExecutor version(GLSLVersion version) {
        this.version = version;
        return this;
    }

    public OutputExecutor shaderId(String shaderId) {
        this.shaderId = shaderId;
        if (outputFormat != OutputFormat.C_HEADER) {
            logger.warn("Setting shader id for OutputFormats other than C_HEADER doesn't do anything!");
        }
        return this;
    }

    public OutputExecutor identifierMode(IdentifierOutputMode mode) {
        this.mode = mode;
        return this;
    }

    public OutputExecutor indentation(int indentation) {
        this.indentation = indentation;
        return this;
    }

    public OutputExecutor significantDecimals(int decimals) {
        this.maxDecimals = decimals;
        return this;
    }

    public OutputExecutor renderNewLines(boolean enabled) {
        this.renderNewLines = enabled;
        return this;
    }

    public OutputExecutor blackListKeyword(String... keywords) {
        Collections.addAll(blacklistedKeywords, keywords);
        return this;
    }

    public OutputExecutor outputConfig(OutputConfig config) {
        this.configuration = config;
        return this;
    }

    public OutputExecutor withHeader(String header) {
        this.header = header;
        return this;
    }

    @Override
    public String process() {
        if (result == null) {
            throw new IllegalArgumentException("No source for the output");
        }

        if (configuration == null) {
            configuration = new OutputConfigBuilder()
                    .identifierMode(mode)
                    .indentation(indentation)
                    .renderNewLines(renderNewLines)
                    .showOriginalIdentifiers(true)
                    .blacklistKeyword(blacklistedKeywords)
                    .shaderToy(outputFormat == OutputFormat.SHADERTOY)
                    .significantDecimals(maxDecimals)
                    .build();
        }

        switch (outputFormat) {
            case C_HEADER:
                return renderHeader(configuration);
            case PLAIN:
            case SHADERTOY:
                throw new UnsupportedOperationException("Not implemented");
        }

        return null;
    }


    private String renderHeader(OutputConfig configuration) {
        if (shaderId == null) {
            throw new IllegalStateException("No shader id provided!");
        }

        OutputConfig headerConfiguration = configuration.edit()
                .indentation(4)
                .renderNewLines(true)
                .build();

        HeaderRenderStage renderStage = new HeaderRenderStage(shaderId, header, headerConfiguration)
                .setVersionSupplier(() -> version);

        return renderStage.process(result).getData();
    }
}

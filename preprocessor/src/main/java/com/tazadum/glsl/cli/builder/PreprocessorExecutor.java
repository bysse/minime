package com.tazadum.glsl.cli.builder;

import com.tazadum.glsl.preprocessor.DefaultPreprocessor;
import com.tazadum.glsl.preprocessor.Preprocessor;
import com.tazadum.glsl.preprocessor.language.GLSLVersion;
import com.tazadum.glsl.stage.StageException;
import com.tazadum.glsl.util.io.FileSource;
import com.tazadum.glsl.util.io.Source;
import com.tazadum.glsl.util.io.SourceResolver;
import com.tazadum.glsl.util.io.StringSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PreprocessorExecutor implements ProcessorExecutor<Preprocessor.Result> {
    private Logger logger = LoggerFactory.getLogger(PreprocessorExecutor.class);
    private List<SourceResolver> resolvers;
    private GLSLVersion glslVersion;
    private Map<String, String> macros;
    private Source source;

    public static PreprocessorExecutor create() {
        return new PreprocessorExecutor();
    }

    public PreprocessorExecutor() {
        this.glslVersion = GLSLVersion.OpenGL45;
        this.macros = new HashMap<>();
        this.resolvers = new ArrayList<>();
    }

    public PreprocessorExecutor version(GLSLVersion version) {
        glslVersion = version;
        return this;
    }

    public PreprocessorExecutor define(String macro, String value) {
        macros.put(macro, value);
        return this;
    }

    public PreprocessorExecutor sourceResolver(SourceResolver resolver) {
        resolvers.add(resolver);
        return this;
    }

    private void validateSource() {
        if (source != null) {
            throw new IllegalStateException("Preprocessor source already set!");
        }
    }

    public PreprocessorExecutor source(Path path) {
        validateSource();
        try {
            source = new FileSource(path);
        } catch (FileNotFoundException e) {
            throw new UncheckedIOException(e);
        }
        return this;
    }

    public PreprocessorExecutor source(String id, String content) {
        validateSource();
        source = new StringSource(id, content);
        return this;
    }

    public PreprocessorExecutor source(Source source) {
        validateSource();
        this.source = source;
        return this;
    }

    public PreprocessorExecutor source(String id) {
        validateSource();

        for (SourceResolver resolver : resolvers) {
            try {
                Source source = resolver.resolve(id);
                if (source != null) {
                    this.source = source;
                    break;
                }
            } catch (IOException e) {
                logger.info("Failed to resolve " + id);
            }
        }

        if (source == null) {
            throw new IllegalArgumentException("No SourceResolver could resolve a source with id " + id);
        }

        return this;
    }

    @Override
    public Preprocessor.Result process() {
        if (source == null) {
            throw new IllegalStateException("No source set!");
        }

        DefaultPreprocessor preprocessor = new DefaultPreprocessor(glslVersion, resolvers);

        for (Map.Entry<String, String> entry : macros.entrySet()) {
            preprocessor.define(entry.getKey(), entry.getValue());
        }

        try {
            return preprocessor.process(source);
        } catch (IOException e) {
            throw new StageException(e.getMessage(), e);
        }
    }
}

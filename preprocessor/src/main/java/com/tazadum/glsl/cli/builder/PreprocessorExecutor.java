package com.tazadum.glsl.cli.builder;

import com.tazadum.glsl.preprocessor.DefaultPreprocessor;
import com.tazadum.glsl.preprocessor.Preprocessor;
import com.tazadum.glsl.preprocessor.language.GLSLVersion;
import com.tazadum.glsl.stage.StageException;
import com.tazadum.glsl.util.io.FileSource;
import com.tazadum.glsl.util.io.Source;
import com.tazadum.glsl.util.io.StringSource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class PreprocessorExecutor implements ProcessorExecutor<Preprocessor.Result> {
    private GLSLVersion glslVersion;
    private Map<String, String> macros;
    private Source source;

    public static PreprocessorExecutor create() {
        return new PreprocessorExecutor();
    }

    public PreprocessorExecutor() {
        this.glslVersion = GLSLVersion.OpenGL45;
        this.macros = new HashMap<>();
    }

    public PreprocessorExecutor version(GLSLVersion version) {
        glslVersion = version;
        return this;
    }

    public PreprocessorExecutor define(String macro, String value) {
        macros.put(macro, value);
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

    @Override
    public Preprocessor.Result process() {
        if (source == null) {
            throw new IllegalStateException("No source set!");
        }

        DefaultPreprocessor preprocessor = new DefaultPreprocessor(glslVersion);

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

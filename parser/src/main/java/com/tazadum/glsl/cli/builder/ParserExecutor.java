package com.tazadum.glsl.cli.builder;

import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.ShaderType;
import com.tazadum.glsl.preprocessor.Preprocessor;
import com.tazadum.glsl.preprocessor.language.GLSLProfile;
import com.tazadum.glsl.stage.CompilerStage;
import com.tazadum.glsl.stage.StageData;
import com.tazadum.glsl.util.Pair;
import com.tazadum.glsl.util.SourcePositionMapper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ParserExecutor implements ProcessorExecutor<ParserResult> {
    private final ShaderType shaderType;
    private final GLSLProfile profile;
    private String source;
    private SourcePositionMapper mapper;

    public static ParserExecutor create() {
        return new ParserExecutor(ShaderType.FRAGMENT, GLSLProfile.COMPATIBILITY);
    }

    public static ParserExecutor create(ShaderType shaderType, GLSLProfile profile) {
        return new ParserExecutor(shaderType, profile);
    }

    private ParserExecutor(ShaderType shaderType, GLSLProfile profile) {
        this.shaderType = shaderType;
        this.profile = profile;
    }

    public ParserExecutor source(Path path) {
        validateSource();
        try {
            source = Files.readString(path);
            mapper = new SourcePositionMapper();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return this;
    }

    public ParserExecutor source(String source) {
        validateSource();
        this.source = source;
        this.mapper = new SourcePositionMapper();
        return this;
    }

    public ParserExecutor source(Preprocessor.Result result) {
        this.source = result.getSource();
        this.mapper = result.getMapper();
        return this;
    }

    private void validateSource() {
        if (source != null) {
            throw new IllegalStateException("Compiler source already set!");
        }
    }

    @Override
    public ParserResult process() {
        final CompilerStage stage = new CompilerStage(shaderType, profile);
        final StageData<Pair<Node, ParserContext>> result = stage.process(StageData.from(source, mapper));
        final Pair<Node, ParserContext> data = result.getData();
        return new ParserResult(result.getMapper(), data.getFirst(), data.getSecond());
    }
}

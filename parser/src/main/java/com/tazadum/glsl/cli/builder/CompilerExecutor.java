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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class CompilerExecutor implements ProcessorExecutor<CompilerExecutor.Result> {
    private final ShaderType shaderType;
    private final GLSLProfile profile;
    private String source;
    private SourcePositionMapper mapper;

    public static CompilerExecutor create() {
        return new CompilerExecutor(ShaderType.FRAGMENT, GLSLProfile.COMPATIBILITY);
    }

    public static CompilerExecutor create(ShaderType shaderType, GLSLProfile profile) {
        return new CompilerExecutor(shaderType, profile);
    }

    private CompilerExecutor(ShaderType shaderType, GLSLProfile profile) {
        this.shaderType = shaderType;
        this.profile = profile;
    }

    public CompilerExecutor source(Path path) {
        validateSource();
        try {
            source = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
            mapper = new SourcePositionMapper();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return this;
    }

    public CompilerExecutor source(String source) {
        validateSource();
        this.source = source;
        this.mapper = new SourcePositionMapper();
        return this;
    }

    public CompilerExecutor source(Preprocessor.Result result) {
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
    public Result process() {
        final CompilerStage stage = new CompilerStage(shaderType, profile);
        final StageData<Pair<Node, ParserContext>> result = stage.process(StageData.from(source, mapper));
        final Pair<Node, ParserContext> data = result.getData();
        return new Result(result.getMapper(), data.getFirst(), data.getSecond());
    }

    public static class Result implements StageData<Pair<Node, ParserContext>> {
        private final SourcePositionMapper mapper;
        private Node node;
        private ParserContext context;

        Result(SourcePositionMapper mapper, Node node, ParserContext context) {
            this.mapper = mapper;
            this.node = node;
            this.context = context;
        }

        public Node getNode() {
            return node;
        }

        public ParserContext getContext() {
            return context;
        }

        @Override
        public Pair<Node, ParserContext> getData() {
            return Pair.create(node, context);
        }

        @Override
        public SourcePositionMapper getMapper() {
            return mapper;
        }
    }
}

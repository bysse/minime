package com.tazadum.glsl.cli.builder;

import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.stage.StageData;
import com.tazadum.glsl.util.Pair;
import com.tazadum.glsl.util.SourcePositionMapper;

public class ParserResult implements StageData<Pair<Node, ParserContext>> {
    private final SourcePositionMapper mapper;
    private Node node;
    private ParserContext context;

    public ParserResult(SourcePositionMapper mapper, Node node, ParserContext context) {
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

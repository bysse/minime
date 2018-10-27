package com.tazadum.glsl.stage;

import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.util.Pair;

/**
 * Created by erikb on 2018-10-26.
 */
public class OptimizerStage implements Stage<Pair<Node, ParserContext>, Pair<Node, ParserContext>> {
    @Override
    public StageData<Pair<Node, ParserContext>> process(StageData<Pair<Node, ParserContext>> input) {
        return null;
    }
}

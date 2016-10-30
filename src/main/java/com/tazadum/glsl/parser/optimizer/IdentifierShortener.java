package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.output.OutputConfig;
import com.tazadum.glsl.parser.ParserContext;

/**
 * Created by Erik on 2016-10-30.
 */
public interface IdentifierShortener {
    void updateIdentifiers(ParserContext parserContext, Node node, OutputConfig outputConfig);

    String updateTokens(String outputShader);
}

package com.tazadum.glsl.stage;

import com.tazadum.glsl.cli.options.OutputFormat;
import com.tazadum.glsl.exception.SourcePositionException;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.output.OutputConfig;
import com.tazadum.glsl.language.output.OutputVisitor;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by erikb on 2018-10-24.
 */
public class RenderStage implements Stage<Pair<Node, ParserContext>, String> {
    private final Logger logger = LoggerFactory.getLogger(RenderStage.class);
    private final OutputConfig outputConfig;

    public RenderStage(OutputConfig outputConfig, OutputFormat outputFormat) {
        if (outputFormat == OutputFormat.SHADERTOY) {
            this.outputConfig = outputConfig.edit().shaderToy(true).build();
        } else {
            this.outputConfig = outputConfig;
        }
    }

    @Override
    public StageData<String> process(StageData<Pair<Node, ParserContext>> input) {
        try {
            logger.trace("* Rendering the AST to a source string");

            final Node node = input.getData().getFirst();
            final String source = node.accept(new OutputVisitor(outputConfig)).get();

            return StageData.from(source, null);
        } catch (SourcePositionException e) {
            final String message = e.getSourcePosition().format() + ": " + e.getMessage();
            throw new StageException(message, e);
        }
    }
}

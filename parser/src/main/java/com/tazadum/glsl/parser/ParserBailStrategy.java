package com.tazadum.glsl.parser;

import com.tazadum.glsl.preprocessor.PreprocessorException;
import com.tazadum.glsl.util.SourcePosition;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.IntervalSet;

/**
 * Created by erikb on 2018-10-25.
 */
public class ParserBailStrategy extends DefaultErrorStrategy {
    public void recover(Parser recognizer, RecognitionException e) {
        for (ParserRuleContext context = recognizer.getContext(); context != null; context = context.getParent()) {
            context.exception = e;
        }

        final SourcePosition position = SourcePosition.create(e.getOffendingToken());
        throw new PreprocessorException(position, e.getMessage(), e);
    }

    public Token recoverInline(Parser recognizer) throws RecognitionException {
        InputMismatchException e = new InputMismatchException(recognizer);

        for (ParserRuleContext context = recognizer.getContext(); context != null; context = context.getParent()) {
            context.exception = e;
        }

        String message = buildMessage(recognizer, "Input mismatch.", e.getExpectedTokens());
        final SourcePosition position = SourcePosition.create(e.getOffendingToken());
        throw new PreprocessorException(position, message, e);
    }

    private String buildMessage(Parser parser, String base, IntervalSet expectedTokens) {
        if (expectedTokens != null) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < expectedTokens.size(); i++) {
                if (i > 0) {
                    builder.append(", ");
                }
                String literalName = getName(parser, expectedTokens.get(i));
                builder.append("'").append(literalName).append("'");
            }
            return base + " Expected " + builder;
        }
        return base;
    }

    private String getName(Parser parser, int token) {
        return parser.getVocabulary().getDisplayName(token);
    }
}

package com.tazadum.glsl.preprocessor;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.IntervalSet;

/**
 * Created by erikb on 2021-10-05.
 */
public class PreprocessorBailStrategy extends DefaultErrorStrategy {
    private ParserRuleContext getParent(ParserRuleContext context, int depth) {
        for (int i = 0; i < depth; i++) {
            if (context.getParent() == null) {
                break;
            }
            context = context.getParent();
        }
        return context;
    }

    protected void reportInputMismatch(Parser recognizer,
                                       InputMismatchException e) {
        ParserRuleContext context = getParent(recognizer.getContext(), 5);
        String msg = "Mismatched input after \"" + context.getText() + "\". Found " + getTokenErrorDisplay(e.getOffendingToken()) +
                " but expected " + e.getExpectedTokens().toString(recognizer.getVocabulary());
        recognizer.notifyErrorListeners(e.getOffendingToken(), msg, e);
    }

    protected void reportNoViableAlternative(Parser recognizer,
                                             NoViableAltException e)
    {
        TokenStream tokens = recognizer.getInputStream();
        String input;
        if ( tokens!=null ) {
            if ( e.getStartToken().getType()==Token.EOF ) input = "<EOF>";
            else input = tokens.getText(e.getStartToken(), e.getOffendingToken());
        }
        else {
            input = "<unknown input>";
        }
        String msg = "No viable alternative at input "+escapeWSAndQuote(input);
        recognizer.notifyErrorListeners(e.getOffendingToken(), msg, e);
    }

    protected void reportFailedPredicate(Parser recognizer,
                                         FailedPredicateException e)
    {
        String ruleName = recognizer.getRuleNames()[recognizer.getContext().getRuleIndex()];
        String msg = "Rule "+ruleName+" "+e.getMessage();
        recognizer.notifyErrorListeners(e.getOffendingToken(), msg, e);
    }

    protected void reportUnwantedToken(Parser recognizer) {
        if (inErrorRecoveryMode(recognizer)) {
            return;
        }

        beginErrorCondition(recognizer);

        Token t = recognizer.getCurrentToken();
        String tokenName = getTokenErrorDisplay(t);
        IntervalSet expecting = getExpectedTokens(recognizer);
        String msg = "Extraneous input "+tokenName+" expecting "+
                expecting.toString(recognizer.getVocabulary());
        recognizer.notifyErrorListeners(t, msg, null);
    }

    @Override
    public void reportError(Parser recognizer, RecognitionException e) {
        // if we've already reported an error and have not matched a token
        // yet successfully, don't report any errors.
        if (inErrorRecoveryMode(recognizer)) {
            return;
        }
        beginErrorCondition(recognizer);
        if (e instanceof NoViableAltException) {
            reportNoViableAlternative(recognizer, (NoViableAltException) e);
        } else if (e instanceof InputMismatchException) {
            reportInputMismatch(recognizer, (InputMismatchException) e);
        } else if (e instanceof FailedPredicateException) {
            reportFailedPredicate(recognizer, (FailedPredicateException) e);
        } else {
            System.err.println("Unknown recognition error type: " + e.getClass().getName());
            recognizer.notifyErrorListeners(e.getOffendingToken(), e.getMessage(), e);
        }
    }
}

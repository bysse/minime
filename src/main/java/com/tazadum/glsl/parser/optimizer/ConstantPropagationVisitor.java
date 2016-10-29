package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.ast.ReplacingASTVisitor;
import com.tazadum.glsl.parser.ParserContext;

/**
 * Created by Erik on 2016-10-29.
 */
public class ConstantPropagationVisitor extends ReplacingASTVisitor {
    private int changes = 0;

    public ConstantPropagationVisitor(ParserContext parserContext) {
        super(parserContext, false);
    }

    public void reset() {
        this.changes = 0;
    }

    public int getChanges() {
        return changes;
    }
}

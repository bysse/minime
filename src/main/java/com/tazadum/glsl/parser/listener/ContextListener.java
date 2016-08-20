package com.tazadum.glsl.parser.listener;

import com.tazadum.glsl.ast.Context;
import com.tazadum.glsl.language.GLSLParser;
import com.tazadum.glsl.parser.ParserContext;

/**
 * @author erikb
 * @since 2016-07-31
 */
public class ContextListener extends WalkableListener implements HasResult<Context> {
    private final ParserContext parserContext;

    private Context context;

    public ContextListener(ParserContext parserContext) {
        this.parserContext = parserContext;
        this.context = new Context();
    }

    @Override
    public void resetState() {
        context = new Context();
    }

    @Override
    public Context getResult() {
        return context;
    }

    @Override
    public void enterFunction_definition(GLSLParser.Function_definitionContext ctx) {
        parserContext.enterContext();
        super.enterFunction_definition(ctx);
    }

    @Override
    public void exitFunction_definition(GLSLParser.Function_definitionContext ctx) {
        super.exitFunction_definition(ctx);
        parserContext.exitContext();
    }

    @Override
    public void exitVariableDeclaration(GLSLParser.VariableDeclarationContext ctx) {
        VariableDeclarationListener listener = new VariableDeclarationListener(parserContext);
        listener.walk(ctx.init_declarator_list());
        context.addChild(listener.getResult());
    }
}

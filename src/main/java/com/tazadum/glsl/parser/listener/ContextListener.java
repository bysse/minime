package com.tazadum.glsl.parser.listener;

import com.tazadum.glsl.arithmetic.FloatLeafNode;
import com.tazadum.glsl.arithmetic.IntLeafNode;
import com.tazadum.glsl.arithmetic.Numeric;
import com.tazadum.glsl.ast.*;
import com.tazadum.glsl.language.GLSLParser;
import com.tazadum.glsl.parser.GLSLContext;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.variable.ResolutionResult;
import com.tazadum.glsl.parser.variable.VariableRegistry;

/**
 * @author erikb
 * @since 2016-07-31
 */
public class ContextListener extends WalkableListener implements HasResult<Context> {
    private final ParserContext parserContext;

    private Context context;
    private Node node;

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

    @Override
    public void exitPrimary_expression(GLSLParser.Primary_expressionContext ctx) {
        if (ctx.INTCONSTANT() != null) {
            Numeric numeric = Numeric.create(ctx.INTCONSTANT().getText());
            node = new IntLeafNode(numeric);
            return;
        }
        if (ctx.FLOATCONSTANT() != null) {
            Numeric numeric = Numeric.create(ctx.FLOATCONSTANT().getText());
            node = new FloatLeafNode(numeric);
            return;
        }
        if (ctx.BOOLCONSTANT() != null) {
            node = new BooleanLeafNode(Boolean.valueOf(ctx.BOOLCONSTANT().getText()));
            return;
        }
        if (ctx.LEFT_PAREN() != null) {
            node = new ParenthesisNode(node);
            return;
        }

        // resolve variable
        final VariableRegistry variableRegistry = parserContext.getVariableRegistry();
        final GLSLContext currentContext = parserContext.currentContext();

        final String variableName = ctx.variable_identifier().IDENTIFIER().getText();

        ResolutionResult result = variableRegistry.resolve(currentContext, variableName);
        node = new VariableNode(result.getDeclaration());

        variableRegistry.usage(currentContext, variableName, node);
    }
}

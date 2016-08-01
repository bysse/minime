package com.tazadum.glsl.parser.listener;

import com.tazadum.glsl.ast.Context;
import com.tazadum.glsl.ast.VariableDeclarationListNode;
import com.tazadum.glsl.ast.VariableDeclarationNode;
import com.tazadum.glsl.exception.ParserException;
import com.tazadum.glsl.language.GLSLParser;
import com.tazadum.glsl.log.Log;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.type.FullySpecifiedType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author erikb
 * @since 2016-07-31
 */
public class VariableDeclarationListener extends WalkableListener implements HasResult<VariableDeclarationListNode> {
    private final Logger logger = LoggerFactory.getLogger(VariableDeclarationListener.class);
    private final Logger appLogger = Log.getApplicationLogger();

    private final ParserContext parserContext;

    private FullySpecifiedType fst;
    private Context arraySpecifier;
    private Context initializer;

    private VariableDeclarationListNode listNode;

    public VariableDeclarationListener(ParserContext parserContext) {
        this.parserContext = parserContext;
    }

    @Override
    public void resetState() {
        fst = null;
        arraySpecifier = null;
        initializer = null;
        listNode = null;
    }

    public VariableDeclarationListNode getResult() {
        return listNode;
    }

    @Override
    public void enterFully_specified_type(GLSLParser.Fully_specified_typeContext ctx) {
        TypeListener listener = new TypeListener(parserContext);
        listener.walk(ctx);
        fst = listener.getResult();
    }

    @Override
    public void exitSingle_declaration(GLSLParser.Single_declarationContext ctx) {
        if (ctx.INVARIANT() != null) {
            throw ParserException.notSupported("invariant");
        }

        if (ctx.IDENTIFIER() == null) {
            // early declaration of a type
            throw ParserException.notSupported("early type declaration");
        }

        instantiate(ctx.IDENTIFIER().getText());

        super.exitSingle_declaration(ctx);
    }

    @Override
    public void exitInit_declarator_list(GLSLParser.Init_declarator_listContext ctx) {
        instantiate(ctx.IDENTIFIER().getText());

        super.exitInit_declarator_list(ctx);
    }

    @Override
    public void exitArray_specifier(GLSLParser.Array_specifierContext ctx) {
        ContextListener listener = new ContextListener(parserContext);
        listener.walk(ctx);
        arraySpecifier = listener.getResult();
    }

    @Override
    public void exitInitializer(GLSLParser.InitializerContext ctx) {
        ContextListener listener = new ContextListener(parserContext);
        listener.walk(ctx);
        initializer = listener.getResult();
    }

    private void instantiate(String identifier) {
        final VariableDeclarationNode variableNode = new VariableDeclarationNode(fst, identifier, arraySpecifier, initializer);

        // register the declaration and usage of the type to enable easy look up during optimization
        parserContext.getVariableRegistry().declare(variableNode);
        parserContext.getTypeRegistry().usage(fst.getType(), variableNode);

        if (listNode == null) {
            listNode = new VariableDeclarationListNode();
        }
        listNode.setType(fst);
        listNode.addChild(variableNode);
    }
}

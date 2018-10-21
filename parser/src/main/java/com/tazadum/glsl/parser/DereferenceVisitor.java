package com.tazadum.glsl.parser;

import com.tazadum.glsl.language.ast.DefaultASTVisitor;
import com.tazadum.glsl.language.ast.function.FunctionCallNode;
import com.tazadum.glsl.language.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.language.ast.variable.ParameterDeclarationNode;
import com.tazadum.glsl.language.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.language.ast.variable.VariableNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Erik on 2016-10-22.
 */
public class DereferenceVisitor extends DefaultASTVisitor<Void> {
    private final Logger logger = LoggerFactory.getLogger(DereferenceVisitor.class);

    private ParserContextImpl parserContext;

    public DereferenceVisitor(ParserContextImpl parserContext) {
        this.parserContext = parserContext;
    }

    @Override
    public Void visitVariable(VariableNode node) {
        if (parserContext.getVariableRegistry().dereference(node)) {
            logger.debug("Removing usage for {}", node);
        }
        return null;
    }

    @Override
    public Void visitVariableDeclaration(VariableDeclarationNode node) {
        super.visitVariableDeclaration(node);
        parserContext.getVariableRegistry().dereference(node);
        return null;
    }

    @Override
    public Void visitParameterDeclaration(ParameterDeclarationNode node) {
        return visitVariableDeclaration(node);
    }

    @Override
    public Void visitFunctionPrototype(FunctionPrototypeNode node) {
        super.visitFunctionPrototype(node);
        parserContext.getFunctionRegistry().dereference(node);
        return null;
    }

    @Override
    public Void visitFunctionCall(FunctionCallNode node) {
        super.visitFunctionCall(node);
        if (parserContext.getFunctionRegistry().dereference(node)) {
            logger.debug("Removing usage for {}", node);
        }
        return null;
    }
}

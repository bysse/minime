package com.tazadum.glsl.parser.visitor;

import com.tazadum.glsl.ast.DefaultASTVisitor;
import com.tazadum.glsl.ast.variable.ParameterDeclarationNode;
import com.tazadum.glsl.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.ast.variable.VariableNode;
import com.tazadum.glsl.parser.GLSLContext;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.variable.VariableRegistry;

/**
 * Registers variable declarations and usages.
 */
public class VariableReferenceVisitor extends DefaultASTVisitor<Void> {
    private ParserContext parserContext;

    public VariableReferenceVisitor(ParserContext parserContext) {
        this.parserContext = parserContext;
    }

    @Override
    public Void visitVariable(VariableNode node) {
        super.visitVariable(node);

        VariableRegistry registry = parserContext.getVariableRegistry();
        registry.registerVariableUsage(node);

        return null;
    }

    @Override
    public Void visitVariableDeclaration(VariableDeclarationNode node) {
        super.visitVariableDeclaration(node);

        GLSLContext context = parserContext.findContext(node);
        VariableRegistry registry = parserContext.getVariableRegistry();
        registry.declareVariable(context, node);

        return null;
    }

    @Override
    public Void visitParameterDeclaration(ParameterDeclarationNode node) {
        super.visitParameterDeclaration(node);

        GLSLContext context = parserContext.findContext(node);
        VariableRegistry registry = parserContext.getVariableRegistry();
        registry.declareVariable(context, node);

        return null;
    }
}

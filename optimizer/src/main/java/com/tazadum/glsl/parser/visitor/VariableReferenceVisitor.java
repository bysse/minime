package com.tazadum.glsl.parser.visitor;

import com.tazadum.glsl.language.ast.DefaultASTVisitor;
import com.tazadum.glsl.language.ast.variable.ParameterDeclarationNode;
import com.tazadum.glsl.language.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.language.ast.variable.VariableNode;
import com.tazadum.glsl.language.context.GLSLContext;
import com.tazadum.glsl.language.variable.VariableRegistry;
import com.tazadum.glsl.parser.ParserContext;

/**
 * Registers variable declarations and usages.
 */
public class VariableReferenceVisitor extends DefaultASTVisitor<Void> {
    private ParserContext parserContext;

    public VariableReferenceVisitor(ParserContext parserContext) {
        this.parserContext = parserContext;
    }


    public Void visitVariable(VariableNode node) {
        super.visitVariable(node);

        VariableRegistry registry = parserContext.getVariableRegistry();
        registry.registerVariableUsage(node);

        return null;
    }

    public Void visitVariableDeclaration(VariableDeclarationNode node) {
        super.visitVariableDeclaration(node);

        GLSLContext context = parserContext.findContext(node);
        VariableRegistry registry = parserContext.getVariableRegistry();
        registry.declareVariable(context, node);

        return null;
    }

    public Void visitParameterDeclaration(ParameterDeclarationNode node) {
        super.visitParameterDeclaration(node);

        GLSLContext context = parserContext.findContext(node);
        VariableRegistry registry = parserContext.getVariableRegistry();
        registry.declareVariable(context, node);

        return null;
    }
}

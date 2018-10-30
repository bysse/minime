package com.tazadum.glsl.parser;

import com.tazadum.glsl.exception.SourcePositionException;
import com.tazadum.glsl.exception.VariableException;
import com.tazadum.glsl.language.ast.DefaultASTVisitor;
import com.tazadum.glsl.language.ast.Identifier;
import com.tazadum.glsl.language.ast.variable.ParameterDeclarationNode;
import com.tazadum.glsl.language.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.language.ast.variable.VariableNode;
import com.tazadum.glsl.language.context.GLSLContext;
import com.tazadum.glsl.language.variable.ResolutionResult;
import com.tazadum.glsl.language.variable.VariableRegistry;

/**
 * Registers variable declarations and usages.
 * This class doesn't touch function because they are registered in the TypeVisitor
 */
public class ReferencingVisitor extends DefaultASTVisitor<Void> {
    private ParserContext parserContext;

    public ReferencingVisitor(ParserContext parserContext) {
        this.parserContext = parserContext;
    }

    public Void visitVariable(VariableNode node) {
        super.visitVariable(node);

        VariableRegistry registry = parserContext.getVariableRegistry();

        // make sure we have a valid declaration node
        try {
            GLSLContext context = parserContext.findContext(node);
            ResolutionResult result = registry.resolve(context, node.getDeclarationNode().getIdentifier().original(), Identifier.Mode.Original);
            node.setDeclarationNode(result.getDeclaration());
        } catch (VariableException e) {
            throw new SourcePositionException(node.getSourcePosition(), e.getMessage(), e);
        }

        registry.registerVariableUsage(node);

        return null;
    }

    public Void visitVariableDeclaration(VariableDeclarationNode node) {
        super.visitVariableDeclaration(node);

        VariableRegistry registry = parserContext.getVariableRegistry();
        GLSLContext context = parserContext.findContext(node);
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

package com.tazadum.glsl.parser;

import com.tazadum.glsl.exception.SourcePositionException;
import com.tazadum.glsl.exception.VariableException;
import com.tazadum.glsl.language.ast.*;
import com.tazadum.glsl.language.ast.conditional.SwitchNode;
import com.tazadum.glsl.language.ast.function.FunctionDefinitionNode;
import com.tazadum.glsl.language.ast.iteration.DoWhileIterationNode;
import com.tazadum.glsl.language.ast.iteration.ForIterationNode;
import com.tazadum.glsl.language.ast.iteration.WhileIterationNode;
import com.tazadum.glsl.language.ast.variable.ParameterDeclarationNode;
import com.tazadum.glsl.language.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.language.ast.variable.VariableNode;
import com.tazadum.glsl.language.context.GLSLContext;
import com.tazadum.glsl.language.variable.ResolutionResult;
import com.tazadum.glsl.language.variable.VariableRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Registers variable declarations and usages.
 * This class doesn't touch function because they are registered in the TypeVisitor
 */
public class ReferencingVisitor extends DefaultASTVisitor<Void> {
    private final Logger logger = LoggerFactory.getLogger(ReferencingVisitor.class);
    private ParserContext parserContext;

    public ReferencingVisitor(ParserContext parserContext) {
        this.parserContext = parserContext;
    }

    public Void visitVariable(VariableNode node) {
        super.visitVariable(node);

        VariableRegistry registry = parserContext.getVariableRegistry();

        // make sure we have a valid declaration node
        try {
            String identifier = node.getDeclarationNode().getIdentifier().original();
            GLSLContext context = parserContext.findContext(node);

            ResolutionResult result = registry.resolve(context, identifier, Identifier.Mode.Original);
            node.setDeclarationNode(result.getDeclaration());

            logger.debug("Adding usage for variable {}:{}", node.getId(), identifier);
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

    @Override
    public Void visitWhileIteration(WhileIterationNode node) {
        visitContext(node, node);
        return super.visitWhileIteration(node);
    }

    @Override
    public Void visitForIteration(ForIterationNode node) {
        visitContext(node, node);
        return super.visitForIteration(node);
    }

    @Override
    public Void visitDoWhileIteration(DoWhileIterationNode node) {
        visitContext(node, node);
        return super.visitDoWhileIteration(node);
    }

    @Override
    public Void visitFunctionDefinition(FunctionDefinitionNode node) {
        visitContext(node, node);
        return super.visitFunctionDefinition(node);
    }

    @Override
    public Void visitSwitch(SwitchNode node) {
        visitContext(node, node);
        return super.visitSwitch(node);
    }

    @Override
    public Void visitBlockNode(ContextBlockNode node) {
        visitContext(node, node);
        return super.visitBlockNode(node);
    }

    private void visitContext(GLSLContext context, Node node) {
        if (context.getParent() == null) {
            GLSLContext parent = findContext(node.getParentNode());
            if (parent != null) {
                context.setParent(parent);
                parserContext.addContext(context);
            }
        } else {
            parserContext.addContext(context);
        }
    }

    private GLSLContext findContext(Node node) {
        if (node instanceof GLSLContext) {
            return (GLSLContext) node;
        }
        final ParentNode parentNode = node.getParentNode();
        if (parentNode != null) {
            return findContext(parentNode);
        }
        return null;
    }
}

package com.tazadum.glsl.parser;

import com.tazadum.glsl.language.ast.DefaultASTVisitor;
import com.tazadum.glsl.language.ast.conditional.SwitchNode;
import com.tazadum.glsl.language.ast.function.FunctionCallNode;
import com.tazadum.glsl.language.ast.function.FunctionDefinitionNode;
import com.tazadum.glsl.language.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.language.ast.iteration.DoWhileIterationNode;
import com.tazadum.glsl.language.ast.iteration.ForIterationNode;
import com.tazadum.glsl.language.ast.iteration.WhileIterationNode;
import com.tazadum.glsl.language.ast.struct.StructDeclarationNode;
import com.tazadum.glsl.language.ast.variable.ParameterDeclarationNode;
import com.tazadum.glsl.language.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.language.ast.variable.VariableNode;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.language.type.StructType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Erik on 2016-10-22.
 */
public class DereferencingVisitor extends DefaultASTVisitor<Void> {
    private final Logger logger = LoggerFactory.getLogger(DereferencingVisitor.class);

    private ParserContextImpl parserContext;

    public DereferencingVisitor(ParserContextImpl parserContext) {
        this.parserContext = parserContext;
    }

    @Override
    public Void visitVariable(VariableNode node) {
        int id = node.getId();
        if (parserContext.getVariableRegistry().dereference(node)) {
            final VariableDeclarationNode declarationNode = node.getDeclarationNode();

            if (declarationNode == null) {
                logger.debug("Removing usage for unresolved variable {}", id);
            } else {
                logger.debug("Removing usage for variable {}:{}", id, declarationNode.getIdentifier().original());
            }
        }
        return null;
    }

    @Override
    public Void visitStructDeclarationNode(StructDeclarationNode node) {
        parserContext.getTypeRegistry().undeclare(new StructType(node));
        return null;
    }

    @Override
    public Void visitVariableDeclaration(VariableDeclarationNode node) {
        super.visitVariableDeclaration(node);
        parserContext.getVariableRegistry().dereference(node);
        parserContext.getTypeRegistry().usagesOf(node.getType()).remove(node);

        final GLSLType type = node.getType();
        if (type instanceof StructType) {
            final StructType structType = (StructType) type;
            // TODO: check if it's an inline declaration and take action
        }
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
            logger.trace("Removing usage of function {}", node);
        }
        return null;
    }

    @Override
    public Void visitWhileIteration(WhileIterationNode node) {
        super.visitWhileIteration(node);
        parserContext.removeContext(node);
        return null;
    }

    @Override
    public Void visitForIteration(ForIterationNode node) {
        super.visitForIteration(node);
        parserContext.removeContext(node);
        return null;
    }

    @Override
    public Void visitDoWhileIteration(DoWhileIterationNode node) {
        super.visitDoWhileIteration(node);
        parserContext.removeContext(node);
        return null;
    }

    @Override
    public Void visitFunctionDefinition(FunctionDefinitionNode node) {
        super.visitFunctionDefinition(node);
        parserContext.removeContext(node);
        return null;
    }

    @Override
    public Void visitSwitch(SwitchNode node) {
        super.visitSwitch(node);
        parserContext.removeContext(node);
        return null;
    }
}

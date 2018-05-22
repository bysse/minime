package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.ReplacingASTVisitor;
import com.tazadum.glsl.ast.function.FunctionDefinitionNode;
import com.tazadum.glsl.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.ast.variable.ParameterDeclarationNode;
import com.tazadum.glsl.ast.variable.VariableDeclarationListNode;
import com.tazadum.glsl.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.language.BuiltInType;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.Usage;
import com.tazadum.glsl.parser.function.FunctionPrototypeMatcher;

/**
 * Created by Erik on 2016-10-29.
 */
public class DeadCodeEliminationVisitor extends ReplacingASTVisitor implements OptimizerVisitor {
    private final FunctionPrototypeMatcher mainMatcher;
    private final FunctionPrototypeMatcher mainImageMatcher;
    private int changes = 0;

    public DeadCodeEliminationVisitor(ParserContext parserContext) {
        super(parserContext, true);

        this.mainMatcher = new FunctionPrototypeMatcher(BuiltInType.VOID);
        this.mainImageMatcher = new FunctionPrototypeMatcher(BuiltInType.VOID, BuiltInType.VEC4, BuiltInType.VEC2);
    }

    public void reset() {
        this.changes = 0;
    }

    public int getChanges() {
        return changes;
    }

    @Override
    public Node visitVariableDeclaration(VariableDeclarationNode node) {
        super.visitVariableDeclaration(node);

        if (node instanceof ParameterDeclarationNode) {
            // we can't remove parameters, just yet
            return null;
        }

        final Usage<VariableDeclarationNode> nodeUsage = parserContext.getVariableRegistry().resolve(node);
        if (nodeUsage.getUsageNodes().isEmpty()) {
            changes++;
            return REMOVE;
        }
        return null;
    }

    @Override
    public Node visitVariableDeclarationList(VariableDeclarationListNode node) {
        super.visitVariableDeclarationList(node);
        if (node.getChildCount() == 0) {
            changes++;
            return REMOVE;
        }
        return null;
    }

    @Override
    public Node visitFunctionDefinition(FunctionDefinitionNode node) {
        super.visitFunctionDefinition(node);

        // see if the functions are special and needs to be protected
        final String functionName = node.getFunctionPrototype().getIdentifier().original();
        if ("main".equals(functionName) && mainMatcher.matches(node.getFunctionPrototype().getPrototype())) {
            return null;
        }
        if ("mainImage".equals(functionName) && mainImageMatcher.matches(node.getFunctionPrototype().getPrototype())) {
            return null;
        }

        final Usage<FunctionPrototypeNode> nodeUsage = parserContext.getFunctionRegistry().resolve(node.getFunctionPrototype());
        if (nodeUsage.getUsageNodes().isEmpty()) {
            changes++;
            return REMOVE;
        }
        return null;
    }
}

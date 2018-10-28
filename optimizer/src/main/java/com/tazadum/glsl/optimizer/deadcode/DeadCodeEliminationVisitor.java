package com.tazadum.glsl.optimizer.deadcode;

import com.tazadum.glsl.ast.ReplacingASTVisitor;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.function.FunctionDefinitionNode;
import com.tazadum.glsl.language.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.language.ast.struct.StructDeclarationNode;
import com.tazadum.glsl.language.ast.type.TypeDeclarationNode;
import com.tazadum.glsl.language.ast.variable.ParameterDeclarationNode;
import com.tazadum.glsl.language.ast.variable.VariableDeclarationListNode;
import com.tazadum.glsl.language.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.language.function.FunctionPrototypeMatcher;
import com.tazadum.glsl.language.type.PredefinedType;
import com.tazadum.glsl.language.type.TypeRegistry;
import com.tazadum.glsl.language.variable.VariableRegistry;
import com.tazadum.glsl.optimizer.OptimizerVisitor;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.Usage;

import java.util.List;

/**
 * Removes dead code or declarations from the shader.
 * Created by Erik on 2016-10-29.
 */
public class DeadCodeEliminationVisitor extends ReplacingASTVisitor implements OptimizerVisitor {
    private final FunctionPrototypeMatcher mainMatcher;
    private final FunctionPrototypeMatcher mainImageMatcher;
    private int changes = 0;

    public DeadCodeEliminationVisitor(ParserContext parserContext) {
        super(parserContext, true);

        this.mainMatcher = new FunctionPrototypeMatcher(PredefinedType.VOID);
        this.mainImageMatcher = new FunctionPrototypeMatcher(PredefinedType.VOID, PredefinedType.VEC4, PredefinedType.VEC2);
    }

    public void reset() {
        this.changes = 0;
    }

    public int getChanges() {
        return changes;
    }

    @Override
    public Node visitTypeDeclaration(TypeDeclarationNode node) {
        final TypeRegistry typeRegistry = parserContext.getTypeRegistry();
        final List<Node> usageNodes = typeRegistry.usagesOf(node.getType()).getUsageNodes();
        if (!usageNodes.isEmpty()) {
            // the type declaration is being used, abort
            return null;
        }

        changes++;
        return REMOVE;
    }

    @Override
    public Node visitVariableDeclaration(VariableDeclarationNode node) {
        if (node.getInitializer() != null) {
            node.getInitializer().accept(this);
        }

        if (node instanceof ParameterDeclarationNode) {
            // we can't remove parameters, just yet
            return null;
        }

        if (node.getIdentifier() == null) {
            // this is a type declaration

            final TypeRegistry typeRegistry = parserContext.getTypeRegistry();
            final List<Node> usageNodes = typeRegistry.usagesOf(node.getType()).getUsageNodes();
            if (!usageNodes.isEmpty()) {
                // the type declaration is being used, abort
                return null;
            }

            changes++;
            return REMOVE;
        }

        final VariableRegistry variableRegistry = parserContext.getVariableRegistry();
        final Usage<VariableDeclarationNode> nodeUsage = variableRegistry.resolve(node);

        if (!nodeUsage.getUsageNodes().isEmpty()) {
            // the variable is being used, abort
            return null;
        }

        changes++;
        return REMOVE;
    }

    @Override
    public Node visitVariableDeclarationList(VariableDeclarationListNode node) {
        if (node.getParentNode() != null && node.getParentNode() instanceof StructDeclarationNode) {
            return null;
        }

        super.visitVariableDeclarationList(node);
        if (node.getChildCount() == 0) {
            // remove empty declaration lists
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
            // remove functions that aren't used
            changes++;
            return REMOVE;
        }
        return null;
    }
}

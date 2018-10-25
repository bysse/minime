package com.tazadum.glsl.ast;

import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.function.FunctionCallNode;
import com.tazadum.glsl.language.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.language.ast.util.NodeFinder;
import com.tazadum.glsl.language.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.language.ast.variable.VariableNode;
import com.tazadum.glsl.parser.ParserContext;

public class ASTCloner {
    private final ParserContext clonedContext;
    private final Node clone;

    public ASTCloner(ParserContext parserContext, Node node) {
        this.clone = node.clone(null);

        // remap all VariableDeclarations in VariableNode
        for (VariableNode variable : NodeFinder.findAll(clone, VariableNode.class)) {
            final VariableDeclarationNode variableDeclaration = variable.getDeclarationNode();
            if (variableDeclaration.isBuiltIn()) {
                continue;
            }

            final Node declarationNode = clone.find(variableDeclaration.getId());
            final VariableDeclarationNode declaration = (VariableDeclarationNode) declarationNode;
            variable.setDeclarationNode(declaration);
        }

        // remap all
        for (FunctionCallNode functionCall : NodeFinder.findAll(clone, FunctionCallNode.class)) {
            final FunctionPrototypeNode functionDeclaration = functionCall.getDeclarationNode();
            if (functionDeclaration.getPrototype().isBuiltIn()) {
                continue;
            }

            final Node declarationNode = clone.find(functionDeclaration.getId());
            final FunctionPrototypeNode declaration = (FunctionPrototypeNode) declarationNode;
            functionCall.setDeclarationNode(declaration);
        }

        clonedContext = parserContext.remap(clone);
    }

    public Node getNode() {
        return clone;
    }

    public ParserContext getContext() {
        return clonedContext;
    }
}

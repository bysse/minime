package com.tazadum.glsl.ast;

import com.tazadum.glsl.ast.function.FunctionCallNode;
import com.tazadum.glsl.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.ast.variable.VariableNode;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.finder.NodeFinder;
import com.tazadum.glsl.parser.finder.VariableFinder;

public class ASTCloner {
    private final ParserContext clonedContext;
    private final Node clone;

    public ASTCloner(ParserContext parserContext, Node node) {
        this.clone = node.clone(null);

        // remap all VariableDeclarations in VariableNode
        for (VariableNode variable : VariableFinder.findVariables(clone)) {
            final Node declarationNode = clone.find(variable.getDeclarationNode().getId());
            final VariableDeclarationNode declaration = (VariableDeclarationNode) declarationNode;
            variable.setDeclarationNode(declaration);
        }

        // remap all
        for (FunctionCallNode functionCall : NodeFinder.findAll(clone, FunctionCallNode.class)) {
            final Node declarationNode = clone.find(functionCall.getDeclarationNode().getId());
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

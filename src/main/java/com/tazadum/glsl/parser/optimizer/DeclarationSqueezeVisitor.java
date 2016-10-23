package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.ReplacingASTVisitor;
import com.tazadum.glsl.ast.variable.VariableDeclarationListNode;
import com.tazadum.glsl.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.parser.GLSLContext;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.variable.VariableRegistry;

/**
 * Created by Erik on 2016-10-23.
 */
public class DeclarationSqueezeVisitor extends ReplacingASTVisitor {
    private final VariableRegistry variableRegistry;
    private int changes = 0;

    public DeclarationSqueezeVisitor(ParserContext parserContext) {
        super(parserContext, false);
        this.variableRegistry = parserContext.getVariableRegistry();
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
        return null;
    }

    @Override
    public Node visitVariableDeclarationList(VariableDeclarationListNode node) {
        super.visitVariableDeclarationList(node);

        final GLSLContext context = parserContext.findContext(node);

        // TODO: find any earlier declarations of this type
        // TODO: check if the usage is "safe" between this id and the previous declaration

        return null;
    }
}

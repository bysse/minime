package com.tazadum.glsl.parser;

import com.tazadum.glsl.exception.Errors;
import com.tazadum.glsl.exception.SourcePositionException;
import com.tazadum.glsl.language.ast.arithmetic.FloatLeafNode;
import com.tazadum.glsl.language.ast.arithmetic.IntLeafNode;
import com.tazadum.glsl.language.ast.logical.BooleanLeafNode;
import com.tazadum.glsl.language.ast.type.ArraySpecifierNode;
import com.tazadum.glsl.language.context.GLSLContext;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.language.type.Numeric;
import com.tazadum.glsl.language.type.PredefinedType;

import java.util.Stack;

/**
 * Created by erikb on 2018-10-16.
 */
public class ResolvingVisitor extends ContextASTVisitor<GLSLType> {
    private Stack<GLSLContext> contextStack = new Stack<>();
    private ParserContext parserContext;

    public ResolvingVisitor(ParserContext parserContext) {
        super(parserContext.globalContext());
        this.parserContext = parserContext;
    }

    // TODO: we need to register all variables and types but before that resolve all unresolved nodes and replace them
    // Step 1: Resolve and replace
    // Step 2: Visit new nodes

    @Override
    public GLSLType visitBoolean(BooleanLeafNode node) {
        return PredefinedType.BOOL;
    }

    @Override
    public GLSLType visitInt(IntLeafNode node) {
        return node.getType();
    }

    @Override
    public GLSLType visitFloat(FloatLeafNode node) {
        return node.getType();
    }

    @Override
    public GLSLType visitArraySpecifierNode(ArraySpecifierNode node) {
        final Numeric numeric = node.accept(new ConstExpressionEvaluatorVisitor());
        if (numeric.getType() == PredefinedType.INT || numeric.getType() == PredefinedType.UINT) {
            final int size = (int) numeric.getValue();

            // TODO: replace this node

        }

        throw new SourcePositionException(node.getSourcePosition(), Errors.Type.ARRAY_SPECIFIER_NOT_CONSTANT);
    }
}

package com.tazadum.glsl.parser.visitor;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.function.FunctionDeclarationNode;
import com.tazadum.glsl.ast.variable.ParameterDeclarationNode;
import com.tazadum.glsl.language.BuiltInType;
import com.tazadum.glsl.language.GLSLBaseVisitor;
import com.tazadum.glsl.language.GLSLParser;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.listener.TypeListener;
import com.tazadum.glsl.parser.type.FullySpecifiedType;

public class FunctionDeclarationVisitor extends GLSLBaseVisitor<Node> {
    private ParserContext parserContext;

    public FunctionDeclarationVisitor(ParserContext parserContext) {
        this.parserContext = parserContext;
    }

    @Override
    public Node visitFunction_prototype(GLSLParser.Function_prototypeContext ctx) {
        return super.visitFunction_prototype(ctx);
    }

    @Override
    public Node visitFunction_declarator(GLSLParser.Function_declaratorContext ctx) {
        final GLSLParser.Function_headerContext functionHeader = ctx.function_header();
        final String functionName = functionHeader.IDENTIFIER().getText();

        // parse the return type
        final TypeListener typeListener = new TypeListener(parserContext);
        typeListener.walk(functionHeader.fully_specified_type());
        final FullySpecifiedType returnType = typeListener.getResult();

        final FunctionDeclarationNode functionDeclaration = new FunctionDeclarationNode(functionName, returnType);

        // parse the parameters
        for (GLSLParser.Parameter_declarationContext parameterCtx : ctx.parameter_declaration()) {
            final ParameterDeclarationNode parameter = (ParameterDeclarationNode) parameterCtx.accept(this);

            if (parameter.getIdentifier().isEmpty() && BuiltInType.VOID == parameter.getFullySpecifiedType().getType()) {
                // don't add void as a parameter
                continue;
            }

            functionDeclaration.addChild(parameter);
        }

        // register the function
        parserContext.getFunctionRegistry().declare(functionDeclaration);

        return functionDeclaration;
    }

    @Override
    public ParameterDeclarationNode visitParameter_declaration(GLSLParser.Parameter_declarationContext ctx) {
        // the parameter might not have an identifier
        final String parameterName = ctx.IDENTIFIER() == null ? null : ctx.IDENTIFIER().getText();

        // parse the type
        final TypeListener typeListener = new TypeListener(parserContext);
        typeListener.walk(ctx);
        FullySpecifiedType type = typeListener.getResult();

        // parse the array specifier
        Node arraySpecifier = null;
        if (ctx.array_specifier() != null) {
            ContextVisitor visitor = new ContextVisitor(parserContext);
            arraySpecifier = ctx.array_specifier().accept(visitor);
        }

        return new ParameterDeclarationNode(type, parameterName, arraySpecifier);
    }
}

package com.tazadum.glsl.parser.listener;

import com.tazadum.glsl.language.*;
import com.tazadum.glsl.parser.ParserContext;

/**
 * @author erikb
 * @since 2016-07-31
 */
public class TypeListener extends GLSLBaseListener {
    private final ParserContext parserContext;

    private BuiltInType type = null;
    private TypeQualifier qualifier = null;
    private PrecisionQualifier precision = null;

    public TypeListener(ParserContext parserContext) {
        this.parserContext = parserContext;
    }

    @Override
    public void enterFully_specified_type(GLSLParser.Fully_specified_typeContext ctx) {
    }

    @Override
    public void exitFully_specified_type(GLSLParser.Fully_specified_typeContext ctx) {
    }

    @Override
    public void enterType_qualifier(GLSLParser.Type_qualifierContext ctx) {
        qualifier = HasToken.match(ctx, TypeQualifier.values());
    }

    @Override
    public void enterPrecision_qualifier(GLSLParser.Precision_qualifierContext ctx) {
        precision = HasToken.match(ctx, PrecisionQualifier.values());
    }

    @Override
    public void enterType_specifier(GLSLParser.Type_specifierContext ctx) {
    }

    @Override
    public void exitType_specifier(GLSLParser.Type_specifierContext ctx) {
    }

    @Override
    public void enterType_specifier_no_prec(GLSLParser.Type_specifier_no_precContext ctx) {
        BuiltInType builtInType = HasToken.match(ctx, BuiltInType.values());

        if (builtInType != null) {
            // we found a built in type
            type = builtInType;
        }
    }

    @Override
    public void exitType_specifier_no_prec(GLSLParser.Type_specifier_no_precContext ctx) {
        if (type != null) {
            // the type has already been determined
            return;
        }

        // TODO: resolve the type from the TypeRegistry
    }

    @Override
    public void enterStruct_specifier(GLSLParser.Struct_specifierContext ctx) {
        // TODO: parse the custom struct type
    }
}

package com.tazadum.glsl.parser.listener;

import com.tazadum.glsl.language.*;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.type.FullySpecifiedType;

/**
 * @author erikb
 * @since 2016-07-31
 */
public class TypeListener extends WalkableListener implements HasResult<FullySpecifiedType> {
    private final ParserContext parserContext;

    private GLSLType type = null;
    private TypeQualifier qualifier = null;
    private PrecisionQualifier precision = null;

    public TypeListener(ParserContext parserContext) {
        this.parserContext = parserContext;
    }

    public void resetState() {
        type = null;
        qualifier = null;
        precision = null;
    }

    public FullySpecifiedType getResult() {
        return new FullySpecifiedType(qualifier, precision, type);
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
    public void enterType_specifier_no_prec(GLSLParser.Type_specifier_no_precContext ctx) {
        BuiltInType builtInType = HasToken.match(ctx, BuiltInType.values());

        if (builtInType != null) {
            // we found a built in type
            type = builtInType;
        }

        // TODO: handle custom types
    }

    @Override
    public void exitType_specifier_no_prec(GLSLParser.Type_specifier_no_precContext ctx) {
        walkChildren(ctx);

        if (type != null) {
            // the type has already been determined
            return;
        }

        // resolve the type from the TypeRegistry
        final String typeName = ctx.IDENTIFIER().getText();
        final FullySpecifiedType fst = parserContext.getTypeRegistry().resolve(typeName);

        // TODO: verify type information

        type = fst.getType();
    }

    @Override
    public void enterStruct_specifier(GLSLParser.Struct_specifierContext ctx) {
        // TODO: parse the custom struct type

        // register type :         parserContext.getTypeRegistry().declare(fsType);
    }
}

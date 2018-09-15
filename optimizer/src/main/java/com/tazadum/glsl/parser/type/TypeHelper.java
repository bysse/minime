package com.tazadum.glsl.parser.type;

import com.tazadum.glsl.language.*;

/**
 * Created by Erik on 2016-10-07.
 */
public class TypeHelper {
    public static FullySpecifiedType parseFullySpecifiedType(GLSLParser.Fully_specified_typeContext ctx) {
        final GLSLParser.Type_specifierContext typeSpecifierCtx = ctx.type_specifier();
        final PrecisionQualifier precisionQualifier = HasToken.match(typeSpecifierCtx.precision_qualifier(), PrecisionQualifier.values());
        final TypeQualifier typeQualifier = HasToken.match(ctx.type_qualifier(), TypeQualifier.values());
        final BuiltInType builtInType = HasToken.match(typeSpecifierCtx.type_specifier_no_prec(), BuiltInType.values());

        if (builtInType == null) {
            // TODO: we found a custom type and can have an anonymous type
            throw new UnsupportedOperationException("Custom types not supported!");
        }

        return new FullySpecifiedType(typeQualifier, precisionQualifier, builtInType);
    }

    public static FullySpecifiedType parseFullySpecifiedType(GLSLParser.Parameter_declarationContext ctx) {
        final TypeQualifier typeQualifier = HasToken.match(ctx.type_qualifier(), TypeQualifier.values());
        final BuiltInType builtInType = HasToken.match(ctx.type_specifier(), BuiltInType.values());

        if (builtInType == null) {
            // TODO: we found a custom type and can have an anonymous type
            throw new UnsupportedOperationException("Custom types not supported!");
        }

        return new FullySpecifiedType(typeQualifier, null, builtInType);
    }
}

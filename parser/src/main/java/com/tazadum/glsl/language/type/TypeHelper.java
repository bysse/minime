package com.tazadum.glsl.language.type;

import com.tazadum.glsl.language.HasToken;
import com.tazadum.glsl.language.model.*;
import com.tazadum.glsl.parser.GLSLParser;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Erik on 2016-10-07.
 */
public class TypeHelper {
    public static FullySpecifiedType parseFullySpecifiedType(GLSLParser.Fully_specified_typeContext ctx) {
        final GLSLParser.Type_qualifierContext qualifierContext = ctx.type_qualifier();
        final GLSLParser.Type_specifierContext specifierContext = ctx.type_specifier();

        final PrecisionQualifier precisionQualifier = HasToken.match(specifierContext.precision_qualifier(), PrecisionQualifier.values());
        final TypeQualifier typeQualifier = HasToken.match(type_qualifierContext, TypeQualifier.values());
        final BuiltInType builtInType = HasToken.match(specifierContext.type_specifier_no_prec(), BuiltInType.values());

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


    /**
     * Translates the results of the rule 'type_qualifier' to the AST model.
     */
    public static TypeQualifierList parseTypeQualifier(GLSLParser.Type_qualifierContext qualifierContext) {
        TypeQualifierList list = (qualifierContext.type_qualifier() != null) ? parseTypeQualifier(qualifierContext.type_qualifier()) : new TypeQualifierList();

        final GLSLParser.Single_type_qualifierContext context = qualifierContext.single_type_qualifier();
        if (context.storage_qualifier() != null) {

            StorageQualifier storageQualifier = HasToken.fromString(context.storage_qualifier().getText(), StorageQualifier.values());
            if (storageQualifier == StorageQualifier.SUBROUTINE) {
                GLSLParser.Type_name_listContext typeNamesCtx = context.storage_qualifier().type_name_list();

                if (typeNamesCtx == null) {
                    list.add(new SubroutineQualifier(null));
                } else {
                    List<String> typeNames = typeNamesCtx.IDENTIFIER().stream()
                        .map(TerminalNode::getText)
                        .collect(Collectors.toList());

                    list.add(new SubroutineQualifier(typeNames));
                }
            }

            list.add(storageQualifier);
        } else if (context.memory_qualifier() != null) {
            list.add(HasToken.fromToken(context.memory_qualifier(), MemoryQualifier.values()));
        } else if (context.layout_qualifier() != null) {
            GLSLParser.Layout_qualifier_id_listContext listContext = context.layout_qualifier().layout_qualifier_id_list();


            // TODO: chaos

        } else if (context.precise_qualifier() != null) {
            list.add(HasToken.fromToken(context.precision_qualifier(), PrecisionQualifier.values()));
        } else if (context.interpolation_qualifier() != null) {
            list.add(HasToken.fromToken(context.interpolation_qualifier(), InterpolationQualifier.values()));
        } else if (context.invariant_qualifier() != null) {
            list.add(HasToken.fromToken(context.invariant_qualifier(), InvariantQualifier.values()));
        } else if (context.precise_qualifier() != null) {
            list.add(HasToken.fromToken(context.precise_qualifier(), PrecisionQualifier.values()));
        } else {
            assert false : "No TypeQualifier match!";
        }

        return list;
    }
}

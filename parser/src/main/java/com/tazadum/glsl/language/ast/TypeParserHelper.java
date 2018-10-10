package com.tazadum.glsl.language.ast;

import com.tazadum.glsl.language.HasToken;
import com.tazadum.glsl.language.model.*;
import com.tazadum.glsl.language.type.*;
import com.tazadum.glsl.parser.GLSLParser;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Erik on 2016-10-07.
 */
public class TypeParserHelper {
    public static FullySpecifiedType parseFullySpecifiedType(ContextVisitor contextVisitor, GLSLParser.Fully_specified_typeContext ctx) {
        final TypeQualifierList typeQualifier = parseTypeQualifier(contextVisitor, ctx.type_qualifier());
        final GLSLParser.Type_specifierContext specifierContext = ctx.type_specifier();

        // handle the type specifier
        GLSLType type = null;
        GLSLParser.Type_specifier_no_arrayContext typeSpecifier = specifierContext.type_specifier_no_array();
        if (typeSpecifier.IDENTIFIER() != null) {
            // this is a custom type, most likely a struct
            type = new UnresolvedType(typeSpecifier.IDENTIFIER().getText());
        } else if (typeSpecifier.struct_specifier() != null) {
            DataNode<StructType> node = DataNode.cast(StructType.class, typeSpecifier.struct_specifier().accept(contextVisitor));
            type = node.getData();
        } else {
            type = HasToken.fromToken(typeSpecifier, PredefinedType.values());
        }

        // handle the array specifier
        Node arraySpecifier = null;
        if (specifierContext.array_specifier() != null) {
            arraySpecifier = specifierContext.array_specifier().accept(contextVisitor);
            type = new ArrayType(type, arraySpecifier);
        }

        return new FullySpecifiedType(typeQualifier, type);
    }

    public static FullySpecifiedType parseFullySpecifiedType(ContextVisitor contextVisitor, GLSLParser.Parameter_declarationContext ctx) {
        final TypeQualifierList typeQualifier = parseTypeQualifier(contextVisitor, ctx.type_qualifier());

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
    public static TypeQualifierList parseTypeQualifier(ContextVisitor contextVisitor, GLSLParser.Type_qualifierContext qualifierContext) {
        TypeQualifierList list = (qualifierContext.type_qualifier() != null) ? parseTypeQualifier(contextVisitor, qualifierContext.type_qualifier()) : new TypeQualifierList();

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
            final List<LayoutQualifier.QualifierId> ids = new ArrayList<>();

            final GLSLParser.Layout_qualifier_id_listContext listContext = context.layout_qualifier().layout_qualifier_id_list();
            for (GLSLParser.Layout_qualifier_idContext ctx : listContext.layout_qualifier_id()) {
                if (ctx.constant_expression() == null) {
                    ids.add(new LayoutQualifier.QualifierId(ctx.SHARED().getText(), null));
                } else {
                    Node value = ctx.constant_expression().accept(contextVisitor);
                    ids.add(new LayoutQualifier.QualifierId(ctx.IDENTIFIER().getText(), value));
                }
            }
            list.add(new LayoutQualifier(ids));
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

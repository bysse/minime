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
        final GLSLType type = parseTypeSpecifier(contextVisitor, ctx.type_specifier());
        return new FullySpecifiedType(typeQualifier, type);
    }

    public static FullySpecifiedType parseFullySpecifiedType(ContextVisitor contextVisitor, GLSLParser.Parameter_declarationContext ctx) {
        final TypeQualifierList typeQualifier = parseTypeQualifier(contextVisitor, ctx.type_qualifier());
        GLSLType type = parseTypeSpecifier(contextVisitor, ctx.type_specifier());

        if (ctx.array_specifier() == null) {
            return new FullySpecifiedType(typeQualifier, type);
        }

        // parameters can have an additional array specifier
        final Node node = ctx.array_specifier().accept(contextVisitor);
        return new FullySpecifiedType(typeQualifier, new ArrayType(type, node));
    }

    /**
     * Translates the results of the 'type_specifier' rule to the AST model.
     */
    public static GLSLType parseTypeSpecifier(ContextVisitor contextVisitor, GLSLParser.Type_specifierContext ctx) {
        final GLSLParser.Type_specifier_no_arrayContext typeCtx = ctx.type_specifier_no_array();

        GLSLType type;
        if (typeCtx.IDENTIFIER() != null) {
            // this is a custom type, most likely a struct
            type = new UnresolvedType(typeCtx.IDENTIFIER().getText());
        } else if (typeCtx.struct_specifier() != null) {
            DataNode<StructType> node = DataNode.cast(StructType.class, typeCtx.struct_specifier().accept(contextVisitor));
            type = node.getData();
        } else {
            type = HasToken.fromToken(ctx, PredefinedType.values());
        }

        if (ctx.array_specifier() == null) {
            return type;
        }

        final Node node = ctx.array_specifier().accept(contextVisitor);
        return new ArrayType(type, node);
    }

    /**
     * Translates the results of the rule 'type_qualifier' to the AST model.
     */
    public static TypeQualifierList parseTypeQualifier(ContextVisitor contextVisitor, GLSLParser.Type_qualifierContext qualifierContext) {
        if (qualifierContext == null) {
            return null;
        }

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

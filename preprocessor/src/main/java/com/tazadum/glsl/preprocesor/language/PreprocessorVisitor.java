package com.tazadum.glsl.preprocesor.language;

import com.tazadum.glsl.preprocesor.PreprocessorException;
import com.tazadum.glsl.preprocesor.language.ast.*;
import com.tazadum.glsl.preprocesor.model.ExtensionBehavior;
import com.tazadum.glsl.preprocesor.model.GLSLProfile;
import com.tazadum.glsl.preprocesor.model.GLSLVersion;
import com.tazadum.glsl.preprocesor.model.HasToken;
import com.tazadum.glsl.preprocessor.parser.PPBaseVisitor;
import com.tazadum.glsl.preprocessor.parser.PPParser;
import com.tazadum.glsl.util.ANTLRUtils;
import com.tazadum.glsl.util.FormatUtil;
import com.tazadum.glsl.util.SourcePosition;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * Created by erikb on 2018-09-17.
 */
public class PreprocessorVisitor extends PPBaseVisitor<Node> {
    private String endOfLine;
    private SourcePosition endOfLinePosition;

    @Override
    public Node visitExtension_declaration(PPParser.Extension_declarationContext ctx) {
        checkForExtraTokens();
        final String extension = ctx.IDENTIFIER().getSymbol().getText();

        ExtensionBehavior behavior = null;
        if (ctx.REQUIRE() != null) {
            behavior = ExtensionBehavior.REQUIRE;
        } else if (ctx.ENABLE() != null) {
            behavior = ExtensionBehavior.ENABLE;
        } else if (ctx.DISABLE() != null) {
            behavior = ExtensionBehavior.DISABLE;
        } else if (ctx.WARN() != null) {
            behavior = ExtensionBehavior.WARN;
        } else {
            throw new PreprocessorException(FormatUtil.error(ctx, "Unknown extension behavior : %s", ctx.getText()));
        }
        return new ExtensionDeclarationNode(extension, behavior);
    }

    @Override
    public Node visitVersion_declaration(PPParser.Version_declarationContext ctx) {
        checkForExtraTokens();
        final String versionString = ctx.INTCONSTANT().getText();
        final GLSLVersion version = HasToken.fromString(versionString, GLSLVersion.values());

        if (version == null) {
            throw new PreprocessorException(FormatUtil.error(ctx, "Unrecognized version : %s", versionString));
        }

        GLSLProfile profile = null;
        if (ctx.CORE() != null) {
            profile = GLSLProfile.CORE;
        } else if (ctx.COMPATIBILITY() != null) {
            profile = GLSLProfile.COMPATIBILITY;
        } else if (ctx.ES() != null) {
            profile = GLSLProfile.ES;
        }

        if (profile == null && version.getVersionCode() >= GLSLVersion.OpenGL32.getVersionCode()) {
            /*
            If no profile argument is provided and the version is 150 or greater,
            the default is core.
            */
            profile = GLSLProfile.CORE;
        }

        if (profile != GLSLProfile.ES && (version == GLSLVersion.OpenGLES30 || version == GLSLVersion.OpenGLES31)) {
            /*
             * If version 300 or 310 is specified, the profile argument is not optional and must be es,
             * or a compile-time error results.
             */
            throw new PreprocessorException(FormatUtil.error(ctx, "Profile must be 'ES' for version 300 and 310"));
        }

        return new VersionDeclarationNode(version, profile);
    }

    @Override
    public Node visitLine_declaration(PPParser.Line_declarationContext ctx) {
        checkForExtraTokens();

        final int line = toInteger(ctx.INTCONSTANT(0));
        final int sourceLine = toInteger(ctx.INTCONSTANT(1));

        return new LineDeclarationNode(line, sourceLine);
    }

    @Override
    public Node visitPragma_unknown_declaration(PPParser.Pragma_unknown_declarationContext ctx) {
        if (endOfLine == null || endOfLine.isEmpty()) {
            throw new PreprocessorException(FormatUtil.error(ctx, "Empty pragma declaration"));
        }
        return new PragmaDeclarationNode(endOfLine);
    }

    @Override
    public Node visitPragma_include_declaration(PPParser.Pragma_include_declarationContext ctx) {
        checkForExtraTokens();
        final String filePath = ANTLRUtils.stringify(ctx.file_path(), " ");
        return new PragmaIncludeDeclarationNode(filePath);
    }

    @Override
    public Node visitFile_path(PPParser.File_pathContext ctx) {
        return super.visitFile_path(ctx);
    }

    @Override
    public Node visitPath_component(PPParser.Path_componentContext ctx) {
        return super.visitPath_component(ctx);
    }

    @Override
    public Node visitIf_expression(PPParser.If_expressionContext ctx) {
        return super.visitIf_expression(ctx);
    }

    @Override
    public Node visitIfdef_expression(PPParser.Ifdef_expressionContext ctx) {
        return super.visitIfdef_expression(ctx);
    }

    @Override
    public Node visitIfndef_expression(PPParser.Ifndef_expressionContext ctx) {
        return super.visitIfndef_expression(ctx);
    }

    @Override
    public Node visitElse_expression(PPParser.Else_expressionContext ctx) {
        return super.visitElse_expression(ctx);
    }

    @Override
    public Node visitElse_if_expression(PPParser.Else_if_expressionContext ctx) {
        return super.visitElse_if_expression(ctx);
    }

    @Override
    public Node visitEndif_expression(PPParser.Endif_expressionContext ctx) {
        return super.visitEndif_expression(ctx);
    }

    @Override
    public Node visitUndef_expression(PPParser.Undef_expressionContext ctx) {
        return super.visitUndef_expression(ctx);
    }

    @Override
    public Node visitParenthesis_expression(PPParser.Parenthesis_expressionContext ctx) {
        return super.visitParenthesis_expression(ctx);
    }

    @Override
    public Node visitDefined_expression(PPParser.Defined_expressionContext ctx) {
        return super.visitDefined_expression(ctx);
    }

    @Override
    public Node visitInteger_expression(PPParser.Integer_expressionContext ctx) {
        return new IntegerNode(parseInt(ctx.getText()));
    }

    @Override
    public Node visitBit_expression(PPParser.Bit_expressionContext ctx) {
        return super.visitBit_expression(ctx);
    }

    @Override
    public Node visitAdditive_expression(PPParser.Additive_expressionContext ctx) {
        return super.visitAdditive_expression(ctx);
    }

    @Override
    public Node visitShift_expression(PPParser.Shift_expressionContext ctx) {
        return super.visitShift_expression(ctx);
    }

    @Override
    public Node visitIdentifier_expression(PPParser.Identifier_expressionContext ctx) {
        return super.visitIdentifier_expression(ctx);
    }

    @Override
    public Node visitUnary_expression(PPParser.Unary_expressionContext ctx) {
        return super.visitUnary_expression(ctx);
    }

    @Override
    public Node visitMultiplicative_expression(PPParser.Multiplicative_expressionContext ctx) {
        return super.visitMultiplicative_expression(ctx);
    }

    @Override
    public Node visitOr_expression(PPParser.Or_expressionContext ctx) {
        return super.visitOr_expression(ctx);
    }

    @Override
    public Node visitAnd_expression(PPParser.And_expressionContext ctx) {
        return super.visitAnd_expression(ctx);
    }

    @Override
    public Node visitRelational_expression(PPParser.Relational_expressionContext ctx) {
        return super.visitRelational_expression(ctx);
    }

    @Override
    public Node visitMacro_declaration(PPParser.Macro_declarationContext ctx) {
        return super.visitMacro_declaration(ctx);
    }

    @Override
    public Node visitParameter_declaration(PPParser.Parameter_declarationContext ctx) {
        return super.visitParameter_declaration(ctx);
    }

    @Override
    public Node visitPreprocessor(PPParser.PreprocessorContext ctx) {
        if (ctx.declaration() == null) {
            return new NoOpDeclarationNode();
        }

        return ctx.declaration().accept(this);
    }

    @Override
    public Node visitDeclaration(PPParser.DeclarationContext ctx) {
        if (ctx.end_of_line() != null) {
            // extract the end of the line before going into the declarations
            ctx.end_of_line().accept(this);
        }

        // Instead of calling each declaration type rule here we fix the
        // double parsing in 'shouldVisitNextChild'
        return super.visitDeclaration(ctx);
    }

    @Override
    public Node visitEnd_of_line(PPParser.End_of_lineContext ctx) {
        endOfLinePosition = SourcePosition.from(ctx.getStart());

        if (ctx.getChildCount() <= 1) {
            endOfLine = ctx.getText();
        } else {
            endOfLine = ANTLRUtils.stringify(ctx, " ");
        }

        return null;
    }

    private void checkForExtraTokens() {
        if (!endOfLine.isEmpty()) {
            throw new PreprocessorException(FormatUtil.error(endOfLinePosition, "Unexpected input : " + endOfLine));
        }
    }

    /**
     * Attempts to parse a TerminalNode into an integer.
     */
    private int toInteger(TerminalNode terminalNode) {
        Node node = terminalNode.accept(this);
        if (node == null) {
            return parseInt(terminalNode.getText());
        }
        if (node instanceof IntegerNode) {
            return ((IntegerNode) node).getValue();
        }

        throw new UnsupportedOperationException("'toInteger' was called on node of type " + node.getClass().getName());
    }

    private int parseInt(String number) {
        if (number.startsWith("0")) {
            if (number.startsWith("0x") || number.startsWith("0X")) {
                // hexadecimal
                return Integer.parseInt(number, 16);
            }
            // octal
            return Integer.parseInt(number, 8);
        }
        // decimal
        return Integer.parseInt(number);
    }

    @Override
    protected boolean shouldVisitNextChild(RuleNode node, Node currentResult) {
        // avoid the specific problem of parsing end_of_line rule twice.
        return !(node instanceof PPParser.DeclarationContext) || !(currentResult instanceof Declaration);
    }
}

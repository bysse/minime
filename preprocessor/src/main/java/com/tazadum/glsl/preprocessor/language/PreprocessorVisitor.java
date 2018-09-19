package com.tazadum.glsl.preprocessor.language;

import com.tazadum.glsl.preprocessor.PreprocessorException;
import com.tazadum.glsl.preprocessor.language.ast.*;
import com.tazadum.glsl.preprocessor.language.ast.expression.*;
import com.tazadum.glsl.preprocessor.language.ast.flow.*;
import com.tazadum.glsl.preprocessor.model.*;
import com.tazadum.glsl.preprocessor.parser.PPBaseVisitor;
import com.tazadum.glsl.preprocessor.parser.PPParser;
import com.tazadum.glsl.util.ANTLRUtils;
import com.tazadum.glsl.util.FormatUtil;
import com.tazadum.glsl.util.SourcePosition;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
        final String filePath = ANTLRUtils.stringify(ctx.file_path(), "");
        return new PragmaIncludeDeclarationNode(filePath);
    }

    @Override
    public Node visitIf_expression(PPParser.If_expressionContext ctx) {
        final Expression expression = (Expression) ctx.const_expression().accept(this);
        return new IfFlowNode(expression);
    }

    @Override
    public Node visitIfdef_expression(PPParser.Ifdef_expressionContext ctx) {
        checkForExtraTokens();
        final String identifier = ctx.IDENTIFIER().getText();
        return new IfDefinedFlowNode(identifier);
    }

    @Override
    public Node visitIfndef_expression(PPParser.Ifndef_expressionContext ctx) {
        checkForExtraTokens();
        final String identifier = ctx.IDENTIFIER().getText();
        return new IfNotDefinedFlowNode(identifier);
    }

    @Override
    public Node visitElse_expression(PPParser.Else_expressionContext ctx) {
        checkForExtraTokens();
        return new ElseFlowNode();
    }

    @Override
    public Node visitElse_if_expression(PPParser.Else_if_expressionContext ctx) {
        final Expression expression = (Expression) ctx.const_expression().accept(this);
        return new ElseIfFlowNode(expression);
    }

    @Override
    public Node visitEndif_expression(PPParser.Endif_expressionContext ctx) {
        checkForExtraTokens();
        return new EndIfFlowNode();
    }

    @Override
    public Node visitUndef_expression(PPParser.Undef_expressionContext ctx) {
        checkForExtraTokens();
        final String identifier = ctx.IDENTIFIER().getText();
        return new UnDefineFlowNode(identifier);
    }

    @Override
    public Node visitParenthesis_expression(PPParser.Parenthesis_expressionContext ctx) {
        final Expression expression = (Expression) ctx.const_expression().accept(this);
        return new ParenthesisNode(expression);
    }

    @Override
    public Node visitDefined_expression(PPParser.Defined_expressionContext ctx) {
        final String identifier = ctx.IDENTIFIER().getText();
        return new DefinedNode(identifier);
    }

    @Override
    public Node visitInteger_expression(PPParser.Integer_expressionContext ctx) {
        return new IntegerNode(parseInt(ctx.getText()));
    }

    @Override
    public Node visitBinary_expression(PPParser.Binary_expressionContext ctx) {
        BinaryOperator operator;
        if (ctx.STAR() != null) {
            operator = BinaryOperator.MULTIPLY;
        } else if (ctx.SLASH() != null) {
            operator = BinaryOperator.DIVIDE;
        } else if (ctx.PERCENT() != null) {
            operator = BinaryOperator.MOD;
        } else if (ctx.PLUS() != null) {
            operator = BinaryOperator.ADD;
        } else if (ctx.DASH() != null) {
            operator = BinaryOperator.SUBTRACT;
        } else if (ctx.LEFT_SHIFT() != null) {
            operator = BinaryOperator.LEFT_SHIFT;
        } else if (ctx.RIGHT_SHIFT() != null) {
            operator = BinaryOperator.RIGHT_SHIFT;
        } else if (ctx.AMPERSAND() != null) {
            operator = BinaryOperator.BITWISE_XOR;
        } else if (ctx.CARET() != null) {
            operator = BinaryOperator.BITWISE_OR;
        } else if (ctx.VERTICAL_BAR() != null) {
            operator = BinaryOperator.BITWISE_AND;
        } else {
            throw new PreprocessorException(FormatUtil.error(ctx, "Unknown operator in expression: %s", ctx.getText()));
        }

        Expression left = (Expression) ctx.numeric_expression(0).accept(this);
        Expression right = (Expression) ctx.numeric_expression(1).accept(this);
        return new BinaryExpressionNode(left, operator, right);
    }

    @Override
    public Node visitIdentifier_expression(PPParser.Identifier_expressionContext ctx) {
        final String identifier = ctx.IDENTIFIER().getText();
        return new IdentifierNode(identifier);
    }

    @Override
    public Node visitUnary_expression(PPParser.Unary_expressionContext ctx) {
        UnaryOperator operator;
        if (ctx.PLUS() != null) {
            operator = UnaryOperator.PLUS;
        } else if (ctx.DASH() != null) {
            operator = UnaryOperator.NEGATE;
        } else if (ctx.TILDE() != null) {
            operator = UnaryOperator.BITWISE_NOT;
        } else if (ctx.BANG() != null) {
            operator = UnaryOperator.NOT;
        } else {
            throw new PreprocessorException(FormatUtil.error(ctx, "Unknown unary operator: %s", ctx.getText()));
        }

        Expression expression = (Expression) ctx.numeric_expression().accept(this);
        return new UnaryExpressionNode(operator, expression);
    }


    @Override
    public Node visitOr_expression(PPParser.Or_expressionContext ctx) {
        Expression left = (Expression) ctx.const_expression(0).accept(this);
        Expression right = (Expression) ctx.const_expression(1).accept(this);
        return new OrExpressionNode(left, right);
    }

    @Override
    public Node visitAnd_expression(PPParser.And_expressionContext ctx) {
        Expression left = (Expression) ctx.const_expression(0).accept(this);
        Expression right = (Expression) ctx.const_expression(1).accept(this);
        return new AndExpressionNode(left, right);
    }

    @Override
    public Node visitRelational_expression(PPParser.Relational_expressionContext ctx) {
        RelationalOperator operator;
        if (ctx.LT_OP() != null) {
            operator = RelationalOperator.LESS_THAN;
        } else if (ctx.LE_OP() != null) {
            operator = RelationalOperator.LESS_THAN_EQUALS;
        } else if (ctx.EQ_OP() != null) {
            operator = RelationalOperator.EQUALS;
        } else if (ctx.NE_OP() != null) {
            operator = RelationalOperator.NOT_EQUALS;
        } else if (ctx.GT_OP() != null) {
            operator = RelationalOperator.GREATER_THAN;
        } else if (ctx.GE_OP() != null) {
            operator = RelationalOperator.GREATER_THAN_EQUALS;
        } else {
            throw new PreprocessorException(FormatUtil.error(ctx, "Unknown relational operator: %s", ctx.getText()));
        }

        Expression left = (Expression) ctx.numeric_expression(0).accept(this);
        Expression right = (Expression) ctx.numeric_expression(1).accept(this);
        return new RelationalExpressionNode(left, operator, right);
    }

    @Override
    public Node visitMacro_declaration(PPParser.Macro_declarationContext ctx) {
        String identifier = ctx.IDENTIFIER().getText();
        List<String> parameters = Collections.emptyList();

        PPParser.Parameter_declarationContext parameterContext = ctx.parameter_declaration();
        if (parameterContext != null) {
            // get all the parameter names
            parameters = parameterContext.IDENTIFIER().stream()
                    .map(TerminalNode::getText)
                    .collect(Collectors.toList());
        }

        return new MacroDeclarationNode(identifier, parameters, endOfLine);
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
                return Integer.parseInt(number.substring(2), 16);
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

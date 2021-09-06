package com.tazadum.glsl.preprocessor.language;

import com.tazadum.glsl.preprocessor.LogKeeper;
import com.tazadum.glsl.preprocessor.Message;
import com.tazadum.glsl.preprocessor.PreprocessorException;
import com.tazadum.glsl.preprocessor.language.ast.*;
import com.tazadum.glsl.preprocessor.language.ast.expression.*;
import com.tazadum.glsl.preprocessor.language.ast.flow.*;
import com.tazadum.glsl.preprocessor.model.HasToken;
import com.tazadum.glsl.preprocessor.parser.PPBaseVisitor;
import com.tazadum.glsl.preprocessor.parser.PPParser;
import com.tazadum.glsl.util.ANTLRUtils;
import com.tazadum.glsl.util.SourcePositionId;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by erikb on 2018-09-17.
 */
public class PreprocessorVisitor extends PPBaseVisitor<Node> {
    private final SourcePositionId sourceId;
    private final LogKeeper logKeeper;

    private String endOfLine;
    private SourcePositionId endOfLinePosition;

    public PreprocessorVisitor(SourcePositionId sourceId, LogKeeper logKeeper) {
        this.sourceId = sourceId;
        this.logKeeper = logKeeper;
    }

    @Override
    public Node visitExtension_declaration(PPParser.Extension_declarationContext ctx) {
        final SourcePositionId sourcePosition = SourcePositionId.create(sourceId, ctx.getStart());
        checkForExtraTokens(sourcePosition);

        final String extension = ctx.IDENTIFIER().getSymbol().getText();

        ExtensionBehavior behavior;
        if (ctx.REQUIRE() != null) {
            behavior = ExtensionBehavior.REQUIRE;
        } else if (ctx.ENABLE() != null) {
            behavior = ExtensionBehavior.ENABLE;
        } else if (ctx.DISABLE() != null) {
            behavior = ExtensionBehavior.DISABLE;
        } else if (ctx.WARN() != null) {
            behavior = ExtensionBehavior.WARN;
        } else {
            throw new PreprocessorException(sourcePosition, "Unknown extension behavior : " + ctx.getText());
        }
        return new ExtensionDeclarationNode(sourcePosition, extension, behavior);
    }

    @Override
    public Node visitVersion_declaration(PPParser.Version_declarationContext ctx) {
        final SourcePositionId sourcePosition = SourcePositionId.create(sourceId, ctx.getStart());
        checkForExtraTokens(sourcePosition);
        final String versionString = ctx.INTCONSTANT().getText();
        final GLSLVersion version = HasToken.fromString(versionString, GLSLVersion.values());

        if (version == null) {
            throw new PreprocessorException(sourcePosition, "Unrecognized version : " + versionString);
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
            throw new PreprocessorException(sourcePosition, "Profile must be 'ES' for version 300 and 310");
        }

        return new VersionDeclarationNode(sourcePosition, version, profile);
    }

    @Override
    public Node visitLine_declaration(PPParser.Line_declarationContext ctx) {
        final SourcePositionId sourcePosition = SourcePositionId.create(sourceId, ctx.getStart());
        checkForExtraTokens(sourcePosition);

        final int line = toInteger(ctx.INTCONSTANT(0));
        final TerminalNode sourceLineContext = ctx.INTCONSTANT(1);
        final int sourceLine = (sourceLineContext != null) ? toInteger(sourceLineContext) : LineDeclarationNode.NO_VALUE;

        return new LineDeclarationNode(sourcePosition, line, sourceLine);
    }

    @Override
    public Node visitPragma_unknown_declaration(PPParser.Pragma_unknown_declarationContext ctx) {
        final SourcePositionId sourcePosition = SourcePositionId.create(sourceId, ctx.getStart());
        if (endOfLine == null || endOfLine.isEmpty()) {
            throw new PreprocessorException(sourcePosition, "Empty pragma declaration");
        }
        return new PragmaDeclarationNode(sourcePosition, endOfLine);
    }

    @Override
    public Node visitPragma_include_declaration(PPParser.Pragma_include_declarationContext ctx) {
        final SourcePositionId sourcePosition = SourcePositionId.create(sourceId, ctx.getStart());

        String filePath = endOfLine.trim();
        if (filePath.isEmpty() || !filePath.endsWith(")")) {
            logKeeper.addWarning(sourcePosition, Message.Warning.INCLUDE_MALFORMATTED);
            return new NoOpDeclarationNode(sourcePosition);
        }

        return new PragmaIncludeDeclarationNode(sourcePosition, filePath.substring(0, filePath.length() - 1));
    }

    @Override
    public Node visitError_declaration(PPParser.Error_declarationContext ctx) {
        final SourcePositionId sourcePosition = SourcePositionId.create(sourceId, ctx.getStart());
        final String message = endOfLine.trim();

        if (message.isEmpty()) {
            throw new PreprocessorException(sourcePosition, Message.Error.EXPECTING_ERROR_MESSAGE);
        }

        return new ErrorDeclarationNode(sourcePosition, endOfLine);
    }

    @Override
    public Node visitIf_expression(PPParser.If_expressionContext ctx) {
        final SourcePositionId sourcePosition = SourcePositionId.create(sourceId, ctx.getStart());
        final Expression expression = (Expression) ctx.const_expression().accept(this);
        return new IfFlowNode(sourcePosition, expression);
    }

    @Override
    public Node visitIfdef_expression(PPParser.Ifdef_expressionContext ctx) {
        final SourcePositionId sourcePosition = SourcePositionId.create(sourceId, ctx.getStart());
        checkForExtraTokens(sourcePosition);
        final String identifier = ctx.IDENTIFIER().getText();
        return new IfDefinedFlowNode(sourcePosition, identifier);
    }

    @Override
    public Node visitIfndef_expression(PPParser.Ifndef_expressionContext ctx) {
        final SourcePositionId sourcePosition = SourcePositionId.create(sourceId, ctx.getStart());
        checkForExtraTokens(sourcePosition);
        final String identifier = ctx.IDENTIFIER().getText();
        return new IfNotDefinedFlowNode(sourcePosition, identifier);
    }

    @Override
    public Node visitElse_expression(PPParser.Else_expressionContext ctx) {
        final SourcePositionId sourcePosition = SourcePositionId.create(sourceId, ctx.getStart());
        checkForExtraTokens(sourcePosition);
        return new ElseFlowNode(sourcePosition);
    }

    @Override
    public Node visitElse_if_expression(PPParser.Else_if_expressionContext ctx) {
        final SourcePositionId sourcePosition = SourcePositionId.create(sourceId, ctx.getStart());
        final Expression expression = (Expression) ctx.const_expression().accept(this);
        return new ElseIfFlowNode(sourcePosition, expression);
    }

    @Override
    public Node visitEndif_expression(PPParser.Endif_expressionContext ctx) {
        final SourcePositionId sourcePosition = SourcePositionId.create(sourceId, ctx.getStart());
        checkForExtraTokens(sourcePosition);
        return new EndIfFlowNode(sourcePosition);
    }

    @Override
    public Node visitUndef_expression(PPParser.Undef_expressionContext ctx) {
        final SourcePositionId sourcePosition = SourcePositionId.create(sourceId, ctx.getStart());
        checkForExtraTokens(sourcePosition);
        final String identifier = ctx.IDENTIFIER().getText();
        return new UnDefineFlowNode(sourcePosition, identifier);
    }

    @Override
    public Node visitParenthesis_expression(PPParser.Parenthesis_expressionContext ctx) {
        final SourcePositionId sourcePosition = SourcePositionId.create(sourceId, ctx.getStart());
        final Expression expression = (Expression) ctx.const_expression().accept(this);
        return new ParenthesisNode(sourcePosition, expression);
    }

    @Override
    public Node visitDefined_expression(PPParser.Defined_expressionContext ctx) {
        final SourcePositionId sourcePosition = SourcePositionId.create(sourceId, ctx.getStart());
        final String identifier = ctx.IDENTIFIER().getText();
        return new DefinedNode(sourcePosition, identifier);
    }

    @Override
    public Node visitInteger_expression(PPParser.Integer_expressionContext ctx) {
        final SourcePositionId sourcePosition = SourcePositionId.create(sourceId, ctx.getStart());
        return new IntegerNode(sourcePosition, parseInt(ctx.getText()));
    }

    @Override
    public Node visitBinary_expression(PPParser.Binary_expressionContext ctx) {
        final SourcePositionId sourcePosition = SourcePositionId.create(sourceId, ctx.getStart());

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
            throw new PreprocessorException(sourcePosition, "Unknown operator in expression : " + ctx.getText());
        }

        Expression left = (Expression) ctx.numeric_expression(0).accept(this);
        Expression right = (Expression) ctx.numeric_expression(1).accept(this);
        return new BinaryExpressionNode(sourcePosition, left, operator, right);
    }

    @Override
    public Node visitIdentifier_expression(PPParser.Identifier_expressionContext ctx) {
        final SourcePositionId sourcePosition = SourcePositionId.create(sourceId, ctx.getStart());
        final String identifier = ctx.IDENTIFIER().getText();
        return new IdentifierNode(sourcePosition, identifier);
    }

    @Override
    public Node visitUnary_expression(PPParser.Unary_expressionContext ctx) {
        final SourcePositionId sourcePosition = SourcePositionId.create(sourceId, ctx.getStart());

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
            throw new PreprocessorException(sourcePosition, "Unknown unary operator : " + ctx.getText());
        }

        Expression expression = (Expression) ctx.numeric_expression().accept(this);
        return new UnaryExpressionNode(sourcePosition, operator, expression);
    }


    @Override
    public Node visitOr_expression(PPParser.Or_expressionContext ctx) {
        final SourcePositionId sourcePosition = SourcePositionId.create(sourceId, ctx.getStart());
        Expression left = (Expression) ctx.const_expression(0).accept(this);
        Expression right = (Expression) ctx.const_expression(1).accept(this);
        return new OrExpressionNode(sourcePosition, left, right);
    }

    @Override
    public Node visitAnd_expression(PPParser.And_expressionContext ctx) {
        final SourcePositionId sourcePosition = SourcePositionId.create(sourceId, ctx.getStart());
        Expression left = (Expression) ctx.const_expression(0).accept(this);
        Expression right = (Expression) ctx.const_expression(1).accept(this);
        return new AndExpressionNode(sourcePosition, left, right);
    }

    @Override
    public Node visitRelational_expression(PPParser.Relational_expressionContext ctx) {
        final SourcePositionId sourcePosition = SourcePositionId.create(sourceId, ctx.getStart());

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
            throw new PreprocessorException(sourcePosition, "Unknown relational operator : " + ctx.getText());
        }

        Expression left = (Expression) ctx.numeric_expression(0).accept(this);
        Expression right = (Expression) ctx.numeric_expression(1).accept(this);
        return new RelationalExpressionNode(sourcePosition, left, operator, right);
    }

    @Override
    public Node visitMacro_declaration_object(PPParser.Macro_declaration_objectContext ctx) {
        final SourcePositionId sourcePosition = SourcePositionId.create(sourceId, ctx.getStart());
        final String identifier = ctx.IDENTIFIER().getText();

        return new MacroDeclarationNode(sourcePosition, identifier, null, endOfLine);
    }

    @Override
    public Node visitMacro_declaration_function(PPParser.Macro_declaration_functionContext ctx) {
        final SourcePositionId sourcePosition = SourcePositionId.create(sourceId, ctx.getStart());
        final String identifier = ctx.IDENTIFIER().getText();

        // get all the parameter names
        final List<String> parameters = ctx.parameter_declaration().IDENTIFIER().stream()
                .map(TerminalNode::getText)
                .collect(Collectors.toList());

        return new MacroDeclarationNode(sourcePosition, identifier, parameters, endOfLine);
    }

    @Override
    public Node visitPreprocessor(PPParser.PreprocessorContext ctx) {
        if (ctx.declaration() == null) {
            return new NoOpDeclarationNode(SourcePositionId.create(sourceId, ctx.getStart()));
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
        endOfLinePosition = SourcePositionId.create(sourceId, ctx.getStart());

        if (ctx.getChildCount() <= 1) {
            endOfLine = ctx.getText();
        } else {
            endOfLine = ANTLRUtils.stringify(ctx, " ");
        }

        return null;
    }

    private void checkForExtraTokens(final SourcePositionId sourcePosition) {
        if (!endOfLine.isEmpty()) {
            throw new PreprocessorException(endOfLinePosition, "Unexpected input : " + endOfLine);
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

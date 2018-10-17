package com.tazadum.glsl.language.output;


import com.tazadum.glsl.exception.BadImplementationException;
import com.tazadum.glsl.language.HasToken;
import com.tazadum.glsl.language.ast.*;
import com.tazadum.glsl.language.ast.arithmetic.*;
import com.tazadum.glsl.language.ast.conditional.*;
import com.tazadum.glsl.language.ast.expression.AssignmentNode;
import com.tazadum.glsl.language.ast.expression.ConstantExpressionNode;
import com.tazadum.glsl.language.ast.expression.ParenthesisNode;
import com.tazadum.glsl.language.ast.function.FunctionCallNode;
import com.tazadum.glsl.language.ast.function.FunctionDefinitionNode;
import com.tazadum.glsl.language.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.language.ast.iteration.DoWhileIterationNode;
import com.tazadum.glsl.language.ast.iteration.ForIterationNode;
import com.tazadum.glsl.language.ast.iteration.WhileIterationNode;
import com.tazadum.glsl.language.ast.logical.BooleanLeafNode;
import com.tazadum.glsl.language.ast.logical.LogicalOperationNode;
import com.tazadum.glsl.language.ast.logical.RelationalOperationNode;
import com.tazadum.glsl.language.ast.struct.InterfaceBlockNode;
import com.tazadum.glsl.language.ast.struct.StructDeclarationNode;
import com.tazadum.glsl.language.ast.traits.IterationNode;
import com.tazadum.glsl.language.ast.type.ArraySpecifier;
import com.tazadum.glsl.language.ast.type.TypeDeclarationNode;
import com.tazadum.glsl.language.ast.type.TypeNode;
import com.tazadum.glsl.language.ast.type.TypeQualifierDeclarationNode;
import com.tazadum.glsl.language.ast.variable.*;
import com.tazadum.glsl.language.model.ArraySpecifiers;
import com.tazadum.glsl.language.model.LayoutQualifier;
import com.tazadum.glsl.language.model.LayoutQualifierId;
import com.tazadum.glsl.language.model.SubroutineQualifier;
import com.tazadum.glsl.language.type.*;
import com.tazadum.glsl.util.Provider;

import java.util.Objects;

/**
 * Created by Erik on 2016-10-13.
 */
public class OutputVisitor implements ASTVisitor<Provider<String>> {
    private static final char SPACE = ' ';
    private static final char DOT = '.';

    private final OutputConfig config;
    private final SourceBuffer buffer;
    private String scopedIndentation;

    public OutputVisitor(OutputConfig config) {
        assert config != null;

        this.config = config;
        this.scopedIndentation = "";
        this.buffer = new SourceBuffer();
    }

    @Override
    public SourceBuffer visitBoolean(BooleanLeafNode node) {
        // TODO: Optimization: replace false with 1==0 ?
        final String value = String.valueOf(node.getValue());
        return buffer.append(value);
    }

    @Override
    public SourceBuffer visitInt(IntLeafNode node) {
        return buffer.append(config.renderNumeric(node.getValue()));
    }

    @Override
    public SourceBuffer visitFloat(FloatLeafNode node) {
        return buffer.append(config.renderNumeric(node.getValue()));
    }

    @Override
    public SourceBuffer visitParenthesis(ParenthesisNode node) {
        buffer.append('(');
        visitChildren(node);
        buffer.append(')');
        return buffer;
    }

    @Override
    public SourceBuffer visitVariable(VariableNode node) {
        Identifier identifier = node.getDeclarationNode().getIdentifier();
        return buffer.append(config.identifier(identifier));
    }

    @Override
    public SourceBuffer visitStatementList(StatementListNode node) {
        for (int i = 0; i < node.getChildCount(); i++) {
            final Node child = node.getChild(i);

            buffer.append(indentation());
            child.accept(this);

            // some statements do not require a semicolon
            if (!noSemiColon(child)) {
                buffer.appendSemicolon();
            }
            buffer.append(config.newLine());
        }
        return buffer;
    }

    @Override
    public SourceBuffer visitVariableDeclaration(VariableDeclarationNode node) {
        // Don't output type here, it should come from the VariableDeclarationList
        buffer.append(config.identifier(node.getIdentifier()));

        outputArraySpecifier(node.getArraySpecifiers());

        if (node.getInitializer() != null) {
            buffer.append('=');
            node.getInitializer().accept(this);
            buffer.append(';');
        }
        return buffer;
    }

    @Override
    public SourceBuffer visitVariableDeclarationList(VariableDeclarationListNode node) {
        outputType(node.getFullySpecifiedType());
        buffer.append(config.identifierSpacing());
        return outputChildCSV(node, 0, node.getChildCount());
    }

    @Override
    public SourceBuffer visitPrecision(PrecisionDeclarationNode node) {
        buffer.append(config.keyword("precision"));
        buffer.appendSpace();
        buffer.append(node.getQualifier());
        buffer.appendSpace();
        buffer.append(node.getPredefinedType());
        return buffer;
    }

    @Override
    public SourceBuffer visitParameterDeclaration(ParameterDeclarationNode node) {
        outputType(node.getOriginalType());

        if (node.getIdentifier() != null) {
            buffer.append(config.identifierSpacing());
            buffer.append(config.identifier(node.getIdentifier()));
        }

        outputArraySpecifier(node.getArraySpecifiers());

        // we don't even look at the initializer because parameters can't have them.

        return buffer;
    }

    @Override
    public SourceBuffer visitFieldSelection(FieldSelectionNode node) {
        node.getExpression().accept(this);
        return buffer.append(DOT).append(node.getSelection());
    }

    @Override
    public SourceBuffer visitArrayIndex(ArrayIndexNode node) {
        node.getExpression().accept(this);
        buffer.append('[');
        node.getIndex().accept(this);
        buffer.append(']');
        return buffer;
    }

    @Override
    public SourceBuffer visitRelationalOperation(RelationalOperationNode node) {
        node.getLeft().accept(this);
        buffer.append(node.getOperator());
        node.getRight().accept(this);
        return buffer;
    }

    @Override
    public SourceBuffer visitLogicalOperation(LogicalOperationNode node) {
        node.getLeft().accept(this);
        buffer.append(node.getOperator());
        node.getRight().accept(this);
        return buffer;
    }

    @Override
    public SourceBuffer visitWhileIteration(WhileIterationNode node) {
        buffer.append(config.keyword("while"));
        buffer.append("(");
        node.getCondition().accept(this);
        buffer.append(')');

        return outputBlock(node.getStatement());
    }

    @Override
    public SourceBuffer visitForIteration(ForIterationNode node) {
        buffer.append(config.keyword("for"));
        buffer.append("(");

        if (node.getInitialization() != null) {
            node.getInitialization().accept(this);
            buffer.appendSemicolon();
        }
        if (node.getCondition() != null) {
            node.getCondition().accept(this);
            buffer.appendSemicolon();
        }
        if (node.getExpression() != null) {
            node.getExpression().accept(this);
        }
        buffer.append(')');

        return outputBlock(node.getStatement());
    }

    @Override
    public SourceBuffer visitDoWhileIteration(DoWhileIterationNode node) {
        buffer.append(config.keyword("do"));
        outputBlock(node.getStatement());
        buffer.append(config.keyword("while"));
        buffer.append("(");
        node.getCondition().accept(this);
        buffer.append(')');
        return buffer;
    }

    @Override
    public SourceBuffer visitFunctionPrototype(FunctionPrototypeNode node) {
        outputType(node.getReturnType());

        buffer.append(config.identifierSpacing());
        buffer.append(config.identifier(node.getIdentifier()));

        buffer.append('(');
        outputChildCSV(node, 0, node.getChildCount());
        buffer.append(')');

        return buffer;
    }

    @Override
    public SourceBuffer visitFunctionDefinition(FunctionDefinitionNode node) {
        node.getFunctionPrototype().accept(this);
        buffer.append('{');

        Identifier identifier = node.getFunctionPrototype().getIdentifier();
        if (!identifier.original().equals(identifier.token())) {
            // Append the original function name as a comment if it's different from the original name
            buffer.append(" /* ").append(identifier.original()).append(" */").append(config.newLine());
        }

        enterScope();
        node.getStatements().accept(this);
        exitScope();

        buffer.append('}');
        return buffer;
    }

    @Override
    public SourceBuffer visitFunctionCall(FunctionCallNode node) {
        if (node.getIdentifier() != null) {
            buffer.append(config.identifier(node.getIdentifier()));
        }
        buffer.append('(');
        outputChildCSV(node, 0, node.getChildCount());
        buffer.append(')');
        return buffer;
    }

    @Override
    public SourceBuffer visitConstantExpression(ConstantExpressionNode node) {
        return visitChildren(node);
    }

    @Override
    public SourceBuffer visitAssignment(AssignmentNode node) {
        node.getLeft().accept(this);
        buffer.append(node.getOperator());
        node.getRight().accept(this);
        return buffer;
    }

    @Override
    public SourceBuffer visitTernaryCondition(TernaryConditionNode node) {
        node.getCondition().accept(this);
        buffer.append('?');
        node.getThen().accept(this);
        buffer.append(':');
        node.getElse().accept(this);
        return buffer;
    }

    @Override
    public SourceBuffer visitReturn(ReturnNode node) {
        buffer.append(config.keyword("return"));
        if (node.getExpression() != null) {
            buffer.appendSpace();
            node.getExpression().accept(this);
        }
        return buffer;
    }

    @Override
    public SourceBuffer visitDiscard(DiscardLeafNode node) {
        return buffer.append(config.keyword("discard"));
    }

    @Override
    public SourceBuffer visitContinue(ContinueLeafNode node) {
        return buffer.append(config.keyword("continue"));
    }

    @Override
    public SourceBuffer visitCondition(ConditionNode node) {
        buffer.append(config.keyword("if"));
        buffer.append('(');
        node.getCondition().accept(this);
        buffer.append(')');

        outputBlock(node.getThen());

        if (node.getElse() != null) {
            if (buffer.lastCharacter() != '}') {
                buffer.append(indentation());
            }
            buffer.append(config.keyword("else"));
            outputBlock(node.getElse());
        }

        return buffer;
    }

    @Override
    public SourceBuffer visitBreak(BreakLeafNode node) {
        return buffer.append(config.keyword("break"));
    }

    @Override
    public SourceBuffer visitPrefixOperation(PrefixOperationNode node) {
        buffer.append(node.getOperator());
        node.getExpression().accept(this);
        return buffer;
    }

    @Override
    public SourceBuffer visitPostfixOperation(PostfixOperationNode node) {
        node.getExpression().accept(this);
        return buffer.append(node.getOperator());
    }

    @Override
    public SourceBuffer visitNumericOperation(NumericOperationNode node) {
        node.getLeft().accept(this);
        buffer.append(node.getOperator());
        node.getRight().accept(this);
        return buffer;
    }

    @Override
    public SourceBuffer visitTypeDeclaration(TypeDeclarationNode node) {
        outputType(node.getFullySpecifiedType());
        return buffer;
    }

    @Override
    public SourceBuffer visitBitOperation(BitOperationNode node) {
        node.getLeft().accept(this);
        buffer.append(node.getOperator());
        node.getRight().accept(this);
        return buffer;
    }

    @Override
    public SourceBuffer visitSwitch(SwitchNode node) {
        buffer.append(config.keyword("switch"));
        buffer.append('(');
        node.getSelector().accept(this);
        buffer.append("){");
        buffer.append(config.newLine());

        for (int i = 0; i < node.getLabelCount(); i++) {
            node.getLabel(i).accept(this);
        }
        return buffer.append('}');
    }

    @Override
    public SourceBuffer visitSwitchCase(CaseNode node) {
        node.getLabel().accept(this);
        buffer.append(':');
        buffer.append(config.newLine());

        if (node.getStatements() != null) {
            node.getStatements().accept(this);
        }
        return buffer;
    }

    @Override
    public SourceBuffer visitSwitchDefault(DefaultCaseNode node) {
        buffer.append(config.keyword("default"));
        buffer.append(':');
        buffer.append(config.newLine());

        if (node.getStatements() != null) {
            node.getStatements().accept(this);
        }
        return buffer;
    }

    @Override
    public SourceBuffer visitTypeNode(TypeNode node) {
        // TODO: implement
        buffer.append("TYPE_NOT_IMPLEMENTED");
        return null;
    }

    @Override
    public SourceBuffer visitInitializerList(InitializerListNode node) {
        buffer.append('{');
        outputChildCSV(node, 0, node.getChildCount());
        buffer.append('}');
        return buffer;
    }

    @Override
    public SourceBuffer visitTypeQualifierDeclarationNode(TypeQualifierDeclarationNode node) {
        outputTypeQualifiers(node.getQualifiers());
        return buffer;
    }

    public SourceBuffer visitLayoutQualifierId(LayoutQualifierId node) {
        buffer.append(node.getIdentifier());
        if (node.getValue() != null) {
            buffer.append('=').append(node.getValue());
        }
        return buffer;
    }

    @Override
    public SourceBuffer visitStructDeclarationNode(StructDeclarationNode node) {
        buffer.append(config.keyword("struct"));

        if (node.getIdentifier() != null) {
            buffer.appendSpace();
            buffer.append(node.getIdentifier());
        }

        buffer.append('{');
        for (int i = 0; i < node.getChildCount(); i++) {
            node.getChild(i).accept(this);
            buffer.appendSemicolon();
        }
        buffer.append('}');
        return buffer;
    }

    @Override
    public SourceBuffer visitInterfaceBlockNode(InterfaceBlockNode node) {
        outputTypeQualifiers(node.getTypeQualifier());
        buffer.appendSpace();

        // the struct output needs to be inlined since the keyword 'struct' shouldn't be rendered.
        final StructDeclarationNode block = node.getInterfaceStruct();
        if (block.getIdentifier() != null) {
            buffer.appendSpace();
            buffer.append(block.getIdentifier());
        }

        buffer.append('{');
        for (int i = 0; i < block.getChildCount(); i++) {
            block.getChild(i).accept(this);
            buffer.appendSemicolon();
        }
        buffer.append('}');

        if (node.getIdentifier() != null) {
            buffer.append(config.identifier(node.getIdentifier()));
        }
        outputArraySpecifier(node.getArraySpecifier());
        return buffer;
    }

    private String indentation() {
        return scopedIndentation;
    }

    private void enterScope() {
        scopedIndentation += config.indentation();
    }

    private void exitScope() {
        int n = config.indentation().length();
        if (n > 0) {
            scopedIndentation = scopedIndentation.substring(0, scopedIndentation.length() - n);
        }
    }

    private boolean noSemiColon(Node node) {
        return node instanceof FunctionDefinitionNode ||
            node instanceof IterationNode;
    }

    private void outputType(FullySpecifiedType type) {
        if (outputTypeQualifiers(type.getQualifiers())) {
            buffer.appendSpace();
        }
        buffer.append(outputType(type.getType()));
    }

    private String outputType(GLSLType type) {
        if (type instanceof ArrayType) {
            final ArrayType arrayType = (ArrayType) type;
            final String dimension = arrayType.hasDimension() ? Objects.toString(arrayType.getDimension()) : "";
            return outputType(arrayType.baseType()) + "[" + dimension + "]";
        }

        if (type instanceof StructType) {
            throw new BadImplementationException();
        }

        return type.token();
    }

    private boolean outputTypeQualifiers(TypeQualifierList typeQualifier) {
        if (typeQualifier == null) {
            return false;
        }

        boolean first = true;
        for (TypeQualifier qualifier : typeQualifier.getQualifiers()) {
            if (!first) {
                buffer.appendSpace();
            }
            first = false;

            if (qualifier instanceof HasToken) {
                final String token = ((HasToken) qualifier).token();
                buffer.append(config.keyword(token));
                continue;
            }

            if (qualifier instanceof SubroutineQualifier) {
                final SubroutineQualifier subroutine = (SubroutineQualifier) qualifier;

                buffer.append(config.keyword("subroutine"));
                buffer.append('(');
                for (String typeName : subroutine.getTypeNames()) {
                    buffer.appendComma();
                    buffer.append(typeName);
                }
                buffer.append(')');
                continue;
            }

            if (qualifier instanceof LayoutQualifier) {
                buffer.append(config.keyword("layout"));
                buffer.append('(');
                for (LayoutQualifierId id : ((LayoutQualifier) qualifier).getIds()) {
                    buffer.appendComma();
                    buffer.append(id.getIdentifier());
                    if (id.getValue() != null) {
                        buffer.append('=').append(Objects.toString(id.getValue()));
                    }
                }
                buffer.append(')');
                continue;
            }

            throw new BadImplementationException("No output case for " + qualifier.getClass().getName());
        }

        return true;
    }

    private void outputArraySpecifier(ArraySpecifiers arraySpecifiers) {
        if (arraySpecifiers == null) {
            return;
        }

        for (ArraySpecifier specifier : arraySpecifiers.getSpecifiers()) {
            buffer.append('[');
            if (specifier.hasDimension()) {
                buffer.append(specifier.getDimension());
            }
            buffer.append(']');
        }
    }

    private SourceBuffer outputBlock(Node node) {
        if (node == null) {
            return buffer;
        }

        if (node instanceof StatementListNode) {
            final boolean singleStatement = ((StatementListNode) node).getChildCount() == 1;

            if (singleStatement) {
                // 'e' is the last character of else
                buffer.ifThen('e', SPACE);
            } else {
                buffer.append('{');
            }
            buffer.append(config.newLine());

            enterScope();
            node.accept(this);
            exitScope();

            if (!singleStatement) {
                buffer.append(indentation());
                buffer.append('}');
            }
        } else {
            enterScope();

            // 'e' is the last character of else
            buffer.ifThen('e', SPACE);
            buffer.append(config.newLine());
            buffer.append(indentation());
            node.accept(this);

            // some statements do not require a semicolon
            if (!noSemiColon(node)) {
                buffer.appendSemicolon();
            }

            exitScope();
        }
        return buffer;
    }

    private SourceBuffer outputChildCSV(ParentNode node, int startIndex, int endIndex) {
        if (node.getChildCount() < startIndex) {
            return buffer;
        }
        if (node.getChildCount() < endIndex) {
            endIndex = node.getChildCount();
        }
        for (int i = startIndex; i < endIndex; i++) {
            if (i > startIndex) {
                buffer.appendComma();
            }
            node.getChild(i).accept(this);
        }
        return buffer;
    }

    private <T extends ParentNode> SourceBuffer visitChildren(T node) {
        for (int i = 0; i < node.getChildCount(); i++) {
            Node child = node.getChild(i);
            if (child == null) {
                continue;
            }
            child.accept(this);
        }
        return buffer;
    }
}

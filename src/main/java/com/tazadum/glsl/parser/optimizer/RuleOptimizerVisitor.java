package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.ParenthesisNode;
import com.tazadum.glsl.ast.ReplacingASTVisitor;
import com.tazadum.glsl.ast.arithmetic.NumericOperationNode;
import com.tazadum.glsl.ast.arithmetic.PostfixOperationNode;
import com.tazadum.glsl.ast.arithmetic.PrefixOperationNode;
import com.tazadum.glsl.ast.arithmetic.UnaryOperationNode;
import com.tazadum.glsl.ast.conditional.ConditionNode;
import com.tazadum.glsl.ast.conditional.TernaryConditionNode;
import com.tazadum.glsl.ast.expression.AssignmentNode;
import com.tazadum.glsl.ast.expression.ConstantExpressionNode;
import com.tazadum.glsl.ast.function.FunctionCallNode;
import com.tazadum.glsl.ast.logical.LogicalOperationNode;
import com.tazadum.glsl.ast.logical.RelationalOperationNode;
import com.tazadum.glsl.ast.variable.ArrayIndexNode;
import com.tazadum.glsl.ast.variable.FieldSelectionNode;
import com.tazadum.glsl.ast.variable.VariableNode;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.simplification.Rule;
import com.tazadum.glsl.simplification.RuleRunner;

import java.util.List;

/**
 * Created by Erik on 2016-10-20.
 */
public class RuleOptimizerVisitor extends ReplacingASTVisitor {
    private final OptimizationDecider decider;
    private final RuleRunner ruleRunner;
    private List<Rule> ruleSet;
    private int changes = 0;

    public RuleOptimizerVisitor(ParserContext parserContext, OptimizationDecider decider, List<Rule> ruleSet) {
        super(parserContext, false);
        this.decider = decider;
        this.ruleSet = ruleSet;
        this.ruleRunner = new RuleRunner();
    }

    public void reset() {
        this.changes = 0;
    }

    public int getChanges() {
        return changes;
    }

    private Node processRules(Node node) {
        for (Rule rule : ruleSet) {
            final Node replacement = ruleRunner.run(parserContext, rule, node);
            if (replacement != null) {
                return replacement;
            }
        }
        return node;
    }

    @Override
    public Node visitParenthesis(ParenthesisNode node) {
        Node replacement = ruleRunner.runRuleSet(parserContext, ruleSet, node);
        if (replacement != null) {
            return replacement;
        }
        return super.visitParenthesis(node);
    }

    @Override
    public Node visitVariable(VariableNode node) {
        Node replacement = ruleRunner.runRuleSet(parserContext, ruleSet, node);
        if (replacement != null) {
            return replacement;
        }
        return super.visitVariable(node);
    }

    @Override
    public Node visitFieldSelection(FieldSelectionNode node) {
        Node replacement = ruleRunner.runRuleSet(parserContext, ruleSet, node);
        if (replacement != null) {
            return replacement;
        }
        return super.visitFieldSelection(node);
    }

    @Override
    public Node visitArrayIndex(ArrayIndexNode node) {
        Node replacement = ruleRunner.runRuleSet(parserContext, ruleSet, node);
        if (replacement != null) {
            return replacement;
        }
        return super.visitArrayIndex(node);
    }

    @Override
    public Node visitRelationalOperation(RelationalOperationNode node) {
        Node replacement = ruleRunner.runRuleSet(parserContext, ruleSet, node);
        if (replacement != null) {
            return replacement;
        }
        return super.visitRelationalOperation(node);
    }

    @Override
    public Node visitLogicalOperation(LogicalOperationNode node) {
        Node replacement = ruleRunner.runRuleSet(parserContext, ruleSet, node);
        if (replacement != null) {
            return replacement;
        }
        return super.visitLogicalOperation(node);
    }

    @Override
    public Node visitFunctionCall(FunctionCallNode node) {
        Node replacement = ruleRunner.runRuleSet(parserContext, ruleSet, node);
        if (replacement != null) {
            return replacement;
        }
        return super.visitFunctionCall(node);
    }

    @Override
    public Node visitConstantExpression(ConstantExpressionNode node) {
        Node replacement = ruleRunner.runRuleSet(parserContext, ruleSet, node);
        if (replacement != null) {
            return replacement;
        }
        return super.visitConstantExpression(node);
    }

    @Override
    public Node visitAssignment(AssignmentNode node) {
        Node replacement = ruleRunner.runRuleSet(parserContext, ruleSet, node);
        if (replacement != null) {
            return replacement;
        }
        return super.visitAssignment(node);
    }

    @Override
    public Node visitTernaryCondition(TernaryConditionNode node) {
        Node replacement = ruleRunner.runRuleSet(parserContext, ruleSet, node);
        if (replacement != null) {
            return replacement;
        }
        return super.visitTernaryCondition(node);
    }

    @Override
    public Node visitCondition(ConditionNode node) {
        Node replacement = ruleRunner.runRuleSet(parserContext, ruleSet, node);
        if (replacement != null) {
            return replacement;
        }
        return super.visitCondition(node);
    }

    @Override
    public Node visitUnaryOperation(UnaryOperationNode node) {
        Node replacement = ruleRunner.runRuleSet(parserContext, ruleSet, node);
        if (replacement != null) {
            return replacement;
        }
        return super.visitUnaryOperation(node);
    }

    @Override
    public Node visitPrefixOperation(PrefixOperationNode node) {
        Node replacement = ruleRunner.runRuleSet(parserContext, ruleSet, node);
        if (replacement != null) {
            return replacement;
        }
        return super.visitPrefixOperation(node);
    }

    @Override
    public Node visitPostfixOperation(PostfixOperationNode node) {
        Node replacement = ruleRunner.runRuleSet(parserContext, ruleSet, node);
        if (replacement != null) {
            return replacement;
        }
        return super.visitPostfixOperation(node);
    }

    @Override
    public Node visitNumericOperation(NumericOperationNode node) {
        Node replacement = ruleRunner.runRuleSet(parserContext, ruleSet, node);
        if (replacement != null) {
            return replacement;
        }
        return super.visitNumericOperation(node);
    }
}

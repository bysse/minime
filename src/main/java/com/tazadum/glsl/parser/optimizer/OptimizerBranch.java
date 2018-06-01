package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.output.IdentifierOutput;
import com.tazadum.glsl.output.Output;
import com.tazadum.glsl.output.OutputConfig;
import com.tazadum.glsl.parser.ParserContext;

import java.util.*;

public class OptimizerBranch {

    private ParserContext context;
    private Node node;

    public OptimizerBranch(ParserContext context, Node node) {
        this.context = context;
        this.node = node;
    }

    public ParserContext getContext() {
        return context;
    }

    public Node getNode() {
        return node;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OptimizerBranch that = (OptimizerBranch) o;
        return Objects.equals(context, that.context) &&
                Objects.equals(node, that.node);
    }

    @Override
    public int hashCode() {
        return Objects.hash(context, node);
    }

    /**
     * Remaps and creates a branch from a ParserContext and a Node.
     * If no remap is needed the ordinary constructor should be used.
     */
    public static OptimizerBranch remap(ParserContext context, Node node) {
        final Node clone = node.clone(null);
        final ParserContext parserContext = context.remap(clone);
        return new OptimizerBranch(parserContext, clone);
    }

    public OptimizerBranch remap() {
        return remap(context, node);
    }

    /**
     * Removes all duplicated branches from a collection of branches
     * @param branches
     * @return
     */
    public static List<OptimizerBranch> unique(Collection<OptimizerBranch> branches) {
        if (branches.isEmpty()) {
            return new ArrayList<>();
        }

        final Set<OptimizerBranch> branchSet = new HashSet<>(branches);
        if (branchSet.size() == 1) {
            return new ArrayList<>(branchSet);
        }

        List<OptimizerBranch> list = new ArrayList<>(branchSet.size());

        Set<String> sourceSet = new HashSet<>();
        OutputConfig outputConfig = new OutputConfig();
        outputConfig.setIdentifiers(IdentifierOutput.None);
        Output output = new Output();

        for (OptimizerBranch branch : branchSet) {
            String source = output.render(branch.getNode(), outputConfig);
            if (sourceSet.add(source)) {
                // only add the branch to the result list if the set operation is successful.
                list.add(branch);
            }
        }
        return list;
    }
}


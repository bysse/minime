package com.tazadum.glsl.optimizer;

import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.util.NodeUtil;
import com.tazadum.glsl.language.output.IdentifierOutputMode;
import com.tazadum.glsl.language.output.OutputConfig;
import com.tazadum.glsl.language.output.OutputConfigBuilder;
import com.tazadum.glsl.language.output.OutputRenderer;
import com.tazadum.glsl.parser.ParserContext;

import java.util.*;


/**
 * Class for representing a branch of the shader optimization tree.
 * This means that we have a unique Node tree and a unique ParserContext.
 */
public class Branch {
    private ParserContext context;
    private Node node;

    public Branch(ParserContext context, Node node) {
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
        Branch that = (Branch) o;
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
    public static Branch remap(ParserContext context, Node node) {
        final Node clone = node.clone(null);

        //System.out.println(String.format("NODE: %x -> %x", NodeUtil.treeHash(node), NodeUtil.treeHash(clone)));

        final ParserContext parserContext = context.remap(clone);
        return new Branch(parserContext, clone);
    }

    public Branch remap() {
        return remap(context, node);
    }

    /**
     * Removes all duplicated branches from a collection of branches
     *
     * @param branches
     * @return
     */
    public static List<Branch> unique(Collection<Branch> branches) {
        if (branches.isEmpty()) {
            return new ArrayList<>();
        }

        final Set<Branch> branchSet = new HashSet<>(branches);
        if (branchSet.size() == 1) {
            return new ArrayList<>(branchSet);
        }

        List<Branch> list = new ArrayList<>(branchSet.size());

        Set<String> sourceSet = new HashSet<>();

        final OutputRenderer output = new OutputRenderer();
        final OutputConfig config = new OutputConfigBuilder()
            .identifierMode(IdentifierOutputMode.None)
            .significantDecimals(5)
            .indentation(0)
            .renderNewLines(false)
            .build();

        // TODO: Must be a better way to compare node trees

        for (Branch branch : branchSet) {
            String source = output.render(branch.getNode(), config);
            if (sourceSet.add(source)) {
                // only add the branch to the result list if the set operation is successful.
                list.add(branch);
            }
        }
        return list;
    }
}


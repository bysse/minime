package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.ast.Identifier;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.output.IdentifierOutput;
import com.tazadum.glsl.output.Output;
import com.tazadum.glsl.output.OutputConfig;
import com.tazadum.glsl.parser.GLSLContext;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.Usage;
import com.tazadum.glsl.parser.variable.ResolutionResult;
import com.tazadum.glsl.util.IdGenerator;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Erik on 2016-10-20.
 */
public class ContextBasedMultiIdentifierShortener {
    private final Output output;
    private final OutputConfig outputConfig;

    private Set<String> usedIdentifiers;
    private int iteration = 0;
    private boolean deterministic;

    private List<ShaderData> shaders = new ArrayList<>();
    private Map<String, List<Identifier>> appliedIdentifiers;
    private IdGenerator idGeneratorTemplate;

    public ContextBasedMultiIdentifierShortener(boolean deterministic) {
        this.deterministic = deterministic;
        this.output = new Output();
        this.usedIdentifiers = new HashSet<>();
        this.outputConfig = new OutputConfig();
        this.outputConfig.setNewlines(false);
        this.outputConfig.setIndentation(0);
        this.outputConfig.setIdentifiers(IdentifierOutput.None);
    }

    public void register(ParserContext parserContext, Node shaderNode, OutputConfig templateOutputConfig) {
        // copy relevant parts of the output configuration
        outputConfig.setOutputConst(templateOutputConfig.isOutputConst());
        outputConfig.setMaxDecimals(templateOutputConfig.getMaxDecimals());

        // render the shader
        final String shaderContent = output.render(shaderNode, outputConfig);

        final Map<Node, Integer> nodeUsageMap = getNodeUsageCountMap(parserContext);
        final List<Node> nodeUsageList = sortByUsage(nodeUsageMap);
        shaders.add(new ShaderData(parserContext, nodeUsageMap, nodeUsageList, shaderContent));
    }

    public void apply() {
        // concatenate all of the source to find frequently used characters
        final String content = shaders.stream()
                .map(ShaderData::getShaderContent)
                .collect(Collectors.joining("\n"));

        idGeneratorTemplate = IdGenerator.create(content);

        appliedIdentifiers = new HashMap<>();
        for (ShaderData shaderData : shaders) {
            applyToShader(shaderData, idGeneratorTemplate);
        }
    }

    private void applyToShader(ShaderData shaderData, IdGenerator idTemplate) {
        final Map<GLSLContext, IdGenerator> contextGeneratorMap = new HashMap<>();
        final List<Node> nodeUsageList = shaderData.getNodeUsageList();
        final ParserContext parserContext = shaderData.getParserContext();

        for (Node node : nodeUsageList) {
            final GLSLContext context = parserContext.findContext(node);
            final IdGenerator generator = contextGeneratorMap.computeIfAbsent(context, (ctx) -> idTemplate.clone());

            while (true) {
                // generate a new identifier
                final String replacement = generator.next();
                if (isIdentifierInUse(parserContext, context, node, replacement)) {
                    // another variable or function has the same name as the proposed identifier
                    continue;
                }

                // set the identifier
                final Identifier identifier = getNodeIdentifier(node);
                identifier.replacement(replacement);
                usedIdentifiers.add(replacement);
                appliedIdentifiers.computeIfAbsent(replacement, (key) -> new ArrayList<>()).add(identifier);
                break;
            }
        }
    }

    public boolean permutateIdentifiers() {
        final IdGenerator idGenerator = idGeneratorTemplate.clone();

        // sort the identifiers by usage count
        final List<String> list = new ArrayList<>(appliedIdentifiers.keySet());

        if (deterministic) {
            // make the unit tests stable
            Collections.sort(list, (a, b) -> {
                int ret = appliedIdentifiers.get(b).size() - appliedIdentifiers.get(a).size();
                if (ret != 0) {
                    return ret;
                }
                return a.compareTo(b);
            });

        } else if (iteration > 0) {
            // sorting is stable so a shuffle could change the sorted order
            Collections.shuffle(list);
            Collections.sort(list, (a, b) -> appliedIdentifiers.get(b).size() - appliedIdentifiers.get(a).size());

            if (iteration > 50) {
                Collections.shuffle(list);
            }
        }

        for (String id : list) {
            final String replacement = idGenerator.next();
            for (Identifier identifier : appliedIdentifiers.get(id)) {
                identifier.replacement(replacement);
            }
        }

        if (deterministic) {
            return iteration++ < 2;
        } else {
            return iteration++ < 100;
        }
    }

    private boolean isIdentifierInUse(ParserContext parserContext, GLSLContext context, Node node, String identifier) {
        if (isVariableReachable(parserContext, context, identifier)) {
            return true;
        }
        if (isFunctionReachable(parserContext, identifier)) {
            return true;
        }
        if (node instanceof VariableDeclarationNode) {
            final VariableDeclarationNode declarationNode = (VariableDeclarationNode) node;
            if (parserContext.globalContext().equals(context)) {
                // if the variable is declared in global scope we need to look if the identifier
                // can be resolved in the context of any of the usage nodes
                final Usage<VariableDeclarationNode> nodeUsage = parserContext.getVariableRegistry().resolve(declarationNode);
                for (Node usage : nodeUsage.getUsageNodes()) {
                    final GLSLContext usageContext = parserContext.findContext(usage);
                    if (isVariableReachable(parserContext, usageContext, identifier)) {
                        return true;
                    }
                }
            }
        }
        if (node instanceof FunctionPrototypeNode) {
            final FunctionPrototypeNode prototypeNode = (FunctionPrototypeNode) node;
            final Usage<FunctionPrototypeNode> nodeUsage = parserContext.getFunctionRegistry().resolve(prototypeNode);
            // for each usage of the function, check if a variable with the same name is reachable
            for (Node usage : nodeUsage.getUsageNodes()) {
                final GLSLContext usageContext = parserContext.findContext(usage);
                if (isVariableReachable(parserContext, usageContext, identifier)) {
                    return true;
                }
            }
        }
        return false;
    }

    private Identifier getNodeIdentifier(Node node) {
        if (node instanceof VariableDeclarationNode) {
            return ((VariableDeclarationNode) node).getIdentifier();
        }

        if (node instanceof FunctionPrototypeNode) {
            return ((FunctionPrototypeNode) node).getIdentifier();
        }

        throw new IllegalArgumentException("Unknown node " + node.getClass().getSimpleName());
    }

    private boolean isVariableReachable(ParserContext parserContext, GLSLContext context, String identifier) {
        try {
            final ResolutionResult resolve = parserContext.getVariableRegistry().resolve(context, identifier, Identifier.Mode.Replacement);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isFunctionReachable(ParserContext parserContext, String identifier) {
        try {
            final List<FunctionPrototypeNode> nodeList = parserContext.getFunctionRegistry().resolve(identifier, Identifier.Mode.Replacement);
            return !nodeList.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    private Map<Node, Integer> getNodeUsageCountMap(ParserContext parserContext) {
        final Map<Node, Integer> nodeMap = new HashMap<>();

        for (Usage<FunctionPrototypeNode> usage : parserContext.getFunctionRegistry().getUsedFunctions()) {
            if (usage.getTarget().getPrototype().isBuiltIn()) {
                // skip built in functions
                continue;
            }
            nodeMap.put(usage.getTarget(), usage.getUsageNodes().size());
        }

        for (Usage<VariableDeclarationNode> usage : parserContext.getVariableRegistry().getAllVariables()) {
            if (usage.getTarget().isBuiltIn()) {
                // skip built in variables
                continue;
            }
            nodeMap.put(usage.getTarget(), usage.getUsageNodes().size());
        }

        return nodeMap;
    }

    private List<Node> sortByUsage(Map<Node, Integer> map) {
        final List<Node> list = new ArrayList<>(map.keySet());
        Collections.sort(list, (a, b) -> map.get(b) - map.get(a));
        return list;
    }

    private static class ShaderData {
        final ParserContext parserContext;
        final Map<Node, Integer> nodeUsageMap;
        final List<Node> nodeUsageList;
        final String shaderContent;

        public ShaderData(ParserContext parserContext, Map<Node, Integer> nodeUsageMap, List<Node> nodeUsageList, String shaderContent) {
            this.parserContext = parserContext;
            this.nodeUsageMap = nodeUsageMap;
            this.nodeUsageList = nodeUsageList;
            this.shaderContent = shaderContent;
        }

        public ParserContext getParserContext() {
            return parserContext;
        }

        public Map<Node, Integer> getNodeUsageMap() {
            return nodeUsageMap;
        }

        public List<Node> getNodeUsageList() {
            return nodeUsageList;
        }

        public String getShaderContent() {
            return shaderContent;
        }
    }
}

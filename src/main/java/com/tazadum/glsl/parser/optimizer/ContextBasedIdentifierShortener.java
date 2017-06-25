package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.ast.Identifier;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.output.Output;
import com.tazadum.glsl.output.OutputConfig;
import com.tazadum.glsl.parser.GLSLContext;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.Usage;
import com.tazadum.glsl.parser.variable.ResolutionResult;
import com.tazadum.glsl.util.IdGenerator;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Erik on 2016-10-20.
 */
public class ContextBasedIdentifierShortener implements IdentifierShortener {
    private static final String DEFINE = "#define ";
    private final Output output;

    private IdGenerator templateGenerator;
    private List<TokenReplacement> tokenReplacements;
    private Set<String> usedIdentifiers;
    private Map<String, List<Identifier>> materializedIdentifiers;
    private int iteration = 0;
    private boolean deterministic;

    public ContextBasedIdentifierShortener(boolean deterministic) {
        this.deterministic = deterministic;
        this.output = new Output();
        this.usedIdentifiers = new HashSet<>();
    }

    public void updateIdentifiers(ParserContext parserContext, Node shaderNode, OutputConfig outputConfig) {
        final String shaderContent = output.render(shaderNode, outputConfig);
        tokenReplacements = getSubStringOccurrences(shaderContent);
        templateGenerator = IdGenerator.create(shaderContent);

        final Map<GLSLContext, IdGenerator> contextGeneratorMap = new HashMap<>();

        final Map<Node, Integer> nodeUsageMap = getNodeUsageCountMap(parserContext);
        final List<Node> nodeUsageList = sortByUsage(nodeUsageMap);

        materializedIdentifiers = new HashMap<>();

        for (Node node : nodeUsageList) {
            final GLSLContext context = parserContext.findContext(node);
            final IdGenerator generator = contextGeneratorMap.computeIfAbsent(context, (ctx) -> templateGenerator.clone());

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
                materializedIdentifiers.computeIfAbsent(replacement, (key) -> new ArrayList<>()).add(identifier);
                break;
            }
        }
    }

    public boolean iterateOnIdentifiers() {
        final IdGenerator idGenerator = templateGenerator.clone();

        // sort the identifiers by usage count
        final List<String> list = new ArrayList<>(materializedIdentifiers.keySet());

        if (deterministic) {
            // make the unit tests stable
            Collections.sort(list, (a, b) -> {
                int ret = materializedIdentifiers.get(b).size() - materializedIdentifiers.get(a).size();
                if (ret != 0) {
                    return ret;
                }
                return a.compareTo(b);
            });
        } else {
            // sorting is stable so a shuffle could change the sorted order
            Collections.shuffle(list);
            Collections.sort(list, (a, b) -> materializedIdentifiers.get(b).size() - materializedIdentifiers.get(a).size());

            if (iteration > 50) {
                Collections.shuffle(list);
            }
        }

        for (String id : list) {
            final String replacement = idGenerator.next();
            for (Identifier identifier : materializedIdentifiers.get(id)) {
                identifier.replacement(replacement);
            }
        }

        if (deterministic) {
            return iteration++ < 2;
        } else {
            return iteration++ < 100;
        }
    }

    public String updateTokens(String shader) {
        final Map<String, TokenReplacement> map = new HashMap<>();

        // assign the symbols
        String nextIdentifier = null;
        for (TokenReplacement replacement : tokenReplacements) {
            if (nextIdentifier == null) {
                nextIdentifier = getNextIdentifier();
            }
            if (replacement.getCharactersSaved(nextIdentifier.length()) > 0) {
                replacement.setToken(nextIdentifier);
                map.put(replacement.getOriginal(), replacement);
                nextIdentifier = null;
            }
        }

        // replace the tokens
        int start = 0;
        final StringBuilder builder = new StringBuilder(shader);
        for (int i = 0; i < builder.length(); i++) {
            final char ch = builder.charAt(i);
            if (isAlphaNumeric(ch)) {
                continue;
            }
            // Don't bother with tokens that are 1 long
            if (i - start > 1) {
                final String token = builder.substring(start, i);
                final TokenReplacement tokenReplacement = map.get(token);
                if (tokenReplacement != null) {
                    builder.replace(start, i, tokenReplacement.getToken());
                    i += tokenReplacement.getToken().length() - (i - start) - 1;
                }
            }
            start = i + 1;
        }

        // add the definitions
        for (TokenReplacement tokenReplacement : tokenReplacements) {
            if (tokenReplacement.getToken() == null) {
                continue;
            }
            final String definition = String.format(DEFINE + "%s %s\n", tokenReplacement.getToken(), tokenReplacement.getOriginal());
            builder.insert(0, definition);
        }
        return builder.toString();
    }

    private String getNextIdentifier() {
        while (true) {
            final String identifier = templateGenerator.next();
            if (usedIdentifiers.add(identifier)) {
                return identifier;
            }
        }
    }

    private List<TokenReplacement> getSubStringOccurrences(String shaderContent) {
        final Map<String, AtomicInteger> tokenMap = new HashMap<>();

        // extract and count the number of tokens in the shader
        int start = 0;
        for (int i = 0; i < shaderContent.length(); i++) {
            final char ch = shaderContent.charAt(i);
            if (isAlphaNumeric(ch)) {
                continue;
            }
            // Don't bother with tokens that are 1 long
            if (i - start > 1) {
                final String token = shaderContent.substring(start, i);
                tokenMap.computeIfAbsent(token, (t) -> new AtomicInteger(0)).incrementAndGet();
            }
            start = i + 1;
        }

        final List<TokenReplacement> definitions = new ArrayList<>();
        for (Map.Entry<String, AtomicInteger> entry : tokenMap.entrySet()) {
            final String token = entry.getKey();
            final int count = entry.getValue().get();
            definitions.add(new TokenReplacement(count, token));
        }

        Collections.sort(definitions);
        return definitions;
    }

    private boolean isAlphaNumeric(char ch) {
        return ('0' <= ch && ch <= '9') || ('a' <= ch && ch <= 'z') || ('A' <= ch && ch <= 'Z') || '.' == ch;
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

    private static class TokenReplacement implements Comparable<TokenReplacement> {
        private final int usageCount;
        private final String original;
        private String token;

        public TokenReplacement(int usageCount, String original) {
            this.usageCount = usageCount;
            this.original = original;
        }

        public String getOriginal() {
            return original;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }

        public int getCharactersSaved(int identifierLength) {
            final int originalCost = original.length() * usageCount;
            final int definitionCost = DEFINE.length() + identifierLength + 1 + original.length();
            final int occurrencesCost = identifierLength * usageCount;
            return originalCost - (definitionCost + occurrencesCost);
        }

        @Override
        public int compareTo(TokenReplacement o) {
            return o.usageCount - usageCount;
        }
    }
}

package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.ast.Identifier;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.output.Output;
import com.tazadum.glsl.output.OutputConfig;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.Usage;
import com.tazadum.glsl.util.IdGenerator;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Erik on 2016-10-20.
 */
public class IdentifierShortener {
    private static final String DEFINE = "#define ";
    private final Output output;

    private List<IdentifierReplacement> identifierReplacements;
    private List<TokenReplacement> tokenReplacements;

    public IdentifierShortener() {
        output = new Output();
    }

    public void analyse(ParserContext parserContext, Node node, OutputConfig outputConfig) {
        final String shaderContent = output.render(node, outputConfig);

        final char[] frequency = getCharacterFrequency(shaderContent);
        final IdGenerator idGenerator = new IdGenerator(frequency);

        identifierReplacements = getUsedIdentifiers(parserContext);
        tokenReplacements = getSubStringOccurrences(shaderContent);

        // build a merged list of symbols
        final List<UsageCounter> usages = new ArrayList<>();
        usages.addAll(identifierReplacements);
        usages.addAll(tokenReplacements);

        // assign the symbols
        String nextIdentifier = null;
        for (UsageCounter usage : usages) {
            if (nextIdentifier == null) {
                nextIdentifier = idGenerator.next();
            }
            if (usage.getCharactersSaved(nextIdentifier.length()) > 0 || usage instanceof IdentifierReplacement) {
                usage.setToken(nextIdentifier);
                nextIdentifier = null;
            }
        }
    }

    public void replaceIdentifiers() {
        // commit the identifier replacements
        for (IdentifierReplacement identifierReplacement : identifierReplacements) {
            final String token = identifierReplacement.getToken();
            if (token != null) {
                identifierReplacement.identifier.replacement(token);
            }
        }
    }

    public String replaceTokens(String shader) {
        // build a lookup map
        final Map<String, TokenReplacement> map = new HashMap<>();
        for (TokenReplacement definition : tokenReplacements) {
            if (definition.getToken() == null) {
                continue;
            }
            map.put(definition.getOriginal(), definition);
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
            final String definition = String.format(DEFINE +"%s %s\n", tokenReplacement.getToken(), tokenReplacement.getOriginal());
            builder.insert(0, definition);
        }
        return builder.toString();
    }

    private char[] getCharacterFrequency(String content) {
        final int[][] frequency = new int[256][2];

        for (int i = 0; i < content.length(); i++) {
            final char ch = content.charAt(i);
            if ('a' <= ch && ch <= 'z' || 'A' <= ch && ch <= 'Z') {
                frequency[ch][0]++;
                frequency[ch][1] = ch;
            }
        }

        Arrays.sort(frequency, (a, b) -> b[0] - a[0]);

        int nonZero = 0;
        for (int i = 0; i < 256; i++) {
            if (frequency[i][0] <= 0) {
                break;
            }
            nonZero++;
        }

        final char[] chars = new char[nonZero];
        for (int i = 0; i < nonZero; i++) {
            chars[i] = (char) frequency[i][1];
        }

        return chars;
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

    private List<IdentifierReplacement> getUsedIdentifiers(ParserContext parserContext) {
        final List<IdentifierReplacement> identifierReplacements = new ArrayList<>();

        for (Usage<FunctionPrototypeNode> usage : parserContext.getFunctionRegistry().getUsedFunctions()) {
            final Identifier identifier = usage.getTarget().getIdentifier();
            if (usage.getTarget().getPrototype().isBuiltIn()) {
                // skip built in functions
                continue;
            }
            identifierReplacements.add(new IdentifierReplacement(usage.getUsageNodes().size(), identifier));
        }

        for (Usage<VariableDeclarationNode> usage : parserContext.getVariableRegistry().getUsedVariables()) {
            final Identifier identifier = usage.getTarget().getIdentifier();
            if (usage.getTarget().isBuiltIn()) {
                // skip built in variables
                continue;
            }
            identifierReplacements.add(new IdentifierReplacement(usage.getUsageNodes().size(), identifier));
        }

        Collections.sort(identifierReplacements);
        return identifierReplacements;
    }

    private static class UsageCounter implements Comparable<UsageCounter> {
        int usageCount;
        String token;

        public UsageCounter(int usageCount) {
            this.usageCount = usageCount;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }

        public int getCharactersSaved(int identifierLength) {
            return 0;
        }

        @Override
        public int compareTo(UsageCounter o) {
            return o.usageCount - usageCount;
        }
    }

    private static class IdentifierReplacement extends UsageCounter {
        private Identifier identifier;

        public IdentifierReplacement(int usageCount, Identifier identifier) {
            super(usageCount);
            this.identifier = identifier;
        }

        public int getCharactersSaved(int identifierLength) {
            return usageCount * (identifier.original().length() - identifierLength);
        }
    }

    private static class TokenReplacement extends UsageCounter {
        private final String original;

        public TokenReplacement(int usageCount, String original) {
            super(usageCount);
            this.original = original;
        }

        public String getOriginal() {
            return original;
        }

        public int getCharactersSaved(int identifierLength) {
            final int originalCost = original.length() * usageCount;
            final int definitionCost = DEFINE.length() + identifierLength + 1 + original.length();
            final int occurrencesCost = identifierLength * usageCount;
            return originalCost - (definitionCost + occurrencesCost);
        }
    }
}

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

import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by Erik on 2016-10-20.
 */
public class IdentifierShortener {
    private final Output output;

    public IdentifierShortener() {
        output = new Output();
    }

    public void run(ParserContext parserContext, Node node, OutputConfig outputConfig) {
        final String shaderContent = output.render(node, outputConfig);
        final char[] frequency = getCharacterFrequency(shaderContent);
        final IdGenerator idGenerator = new IdGenerator(frequency);

        final SortedSet<IdentifierUse> identifierUses = getUsedIdentifiers(parserContext);
        for (IdentifierUse use : identifierUses) {
            final String identifier = idGenerator.next();
            //System.out.format(" - Replacing %s with %s\n", use.identifier.original(), identifier);

            use.identifier.replacement(identifier);
        }
    }

    private char[] getCharacterFrequency(String content) {
        final int [][] frequency = new int[256][2];

        for (int i=0;i<content.length();i++) {
            final char ch = content.charAt(i);
            if ('a' <= ch && ch <= 'z' || 'A' <= ch && ch <= 'Z') {
                frequency[ch][0]++;
                frequency[ch][1] = ch;
            }
        }

        Arrays.sort(frequency, (a, b) -> b[0] - a[0]);

        int nonZero = 0;
        for (int i=0;i<256;i++) {
            if (frequency[i][0] <= 0) {
               break;
            }
            nonZero++;
        }

        final char[] chars = new char[nonZero];
        for (int i=0;i<nonZero;i++) {
            chars[i] = (char)frequency[i][1];
        }

        return chars;
    }

    private SortedSet<IdentifierUse> getUsedIdentifiers(ParserContext parserContext) {
        final SortedSet<IdentifierUse> identifierUses = new TreeSet<>();

        for (Usage<FunctionPrototypeNode> usage : parserContext.getFunctionRegistry().getUsedFunctions()) {
            final Identifier identifier = usage.getTarget().getIdentifier();

            // skip built in functions
            if (usage.getTarget().getPrototype().isBuiltIn()) {
                continue;
            }

            identifierUses.add(new IdentifierUse(usage.getUsageNodes().size(), identifier));
        }

        for (Usage<VariableDeclarationNode> usage : parserContext.getVariableRegistry().getUsedVariables()) {
            final Identifier identifier = usage.getTarget().getIdentifier();

            // skip built in variables
            if (usage.getTarget().isBuiltIn()) {
                continue;
            }

            identifierUses.add(new IdentifierUse(usage.getUsageNodes().size(), identifier));
        }

        return identifierUses;
    }


    private static class IdentifierUse implements Comparable<IdentifierUse> {
        private int usageCount;
        private Identifier identifier;

        public IdentifierUse(int usageCount, Identifier identifier) {
            this.usageCount = usageCount;
            this.identifier = identifier;
        }

        @Override
        public int compareTo(IdentifierUse o) {
            // order from big to low
            int ret = o.usageCount - usageCount;
            if (ret != 0) {
                return ret;
            }
            return o.identifier.compareTo(identifier);
        }
    }
}

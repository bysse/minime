package com.tazadum.glsl.preprocessor;

import com.tazadum.glsl.preprocessor.language.Declaration;
import com.tazadum.glsl.preprocessor.language.Node;
import com.tazadum.glsl.preprocessor.language.PreprocessorVisitor;
import com.tazadum.glsl.preprocessor.model.PreprocessorState;
import com.tazadum.glsl.preprocessor.parser.PPLexer;
import com.tazadum.glsl.preprocessor.parser.PPParser;
import com.tazadum.glsl.util.SourcePosition;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Default implementation of Preprocessor.
 */
public class DefaultPreprocessor implements Preprocessor {
    private volatile boolean used = false;

    private int lineNumber = 1;
    private PreprocessorState state = new PreprocessorState();

    public DefaultPreprocessor() {
    }

    @Override
    public void define(String macro, String value) {
        state.getMacroRegistry().define(macro, value);
    }

    @Override
    public void process(InputStream inputStream) throws IOException {
        if (used) {
            throw new IllegalStateException("The preprocessor is stateful and can only be used once!");
        }
        used = true;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder output = new StringBuilder();

            String line;

            while (null != (line = reader.readLine())) {
                int startOfDeclaration = lineNumber;

                // continue to read rows while they end with a continuation
                while (line.endsWith("\\\n")) {
                    String part = reader.readLine();
                    if (part == null) {
                        throw new PreprocessorException(SourcePosition.from(lineNumber, 0), "Bad line continuation");
                    }
                    line += part;
                    lineNumber++;
                }

                processLine(output, line, startOfDeclaration);

                lineNumber++;
            }
        }
    }

    private void processLine(StringBuilder output, String line, int startOfDeclaration) {
        // do a cheap check if this is a preprocessor declaration
        if (line.trim().startsWith("#")) {

            // TODO: perform macro substitution

            final Node node = parse(line, startOfDeclaration);
            if (node instanceof Declaration) {
                state.accept(startOfDeclaration, (Declaration) node);
            } else {
                throw new UnsupportedOperationException("Only declaration nodes are allowed in the state");
            }

            return;
        }

        if (state.isSectionEnabled()) {
            // TODO: perform macro substitution

            output.append(line);
            output.append('\n');

        } else {
            output.append("// ");
            output.append(line);
            output.append('\n');
        }
    }

    /**
     * Parses a source line and creates an AST.
     *
     * @param sourceLine         The source line to parse.
     * @param startOfDeclaration The line offset of the source line.
     * @return An AST node.
     */
    private Node parse(String sourceLine, int startOfDeclaration) {
        try {
            PPLexer lexer = new PPLexer(CharStreams.fromString(sourceLine));
            final PPParser parser = new PPParser(new CommonTokenStream(lexer));

            // TODO: make better bail strategy
            parser.setErrorHandler(new BailErrorStrategy());

            PreprocessorVisitor visitor = new PreprocessorVisitor();
            PPParser.PreprocessorContext context = parser.preprocessor();
            return context.accept(visitor);
        } catch (PreprocessorException e) {
            SourcePosition local = e.getPosition();
            SourcePosition mappedPosition = SourcePosition.from(startOfDeclaration + local.getLine(), local.getColumn());
            throw new PreprocessorException(mappedPosition, "Syntax error", e);
        }
    }
}

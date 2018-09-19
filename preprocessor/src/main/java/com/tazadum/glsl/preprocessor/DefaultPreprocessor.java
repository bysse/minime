package com.tazadum.glsl.preprocessor;

import com.tazadum.glsl.preprocessor.language.Declaration;
import com.tazadum.glsl.preprocessor.language.Node;
import com.tazadum.glsl.preprocessor.language.PreprocessorVisitor;
import com.tazadum.glsl.preprocessor.language.ast.MacroDeclarationNode;
import com.tazadum.glsl.preprocessor.model.PreprocessorState;
import com.tazadum.glsl.util.FormatUtil;
import com.tazadum.glsl.preprocessor.parser.PPLexer;
import com.tazadum.glsl.preprocessor.parser.PPParser;
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
        Node node = parse("#define " + macro + " " + value);
        state.accept(1, (MacroDeclarationNode) node);
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
                        throw new PreprocessorException(FormatUtil.error(lineNumber, "Bad line continuation"));
                    }
                    line += part;
                    lineNumber++;
                }

                process(output, line, startOfDeclaration);

                lineNumber++;
            }
        }
    }

    private void process(StringBuilder output, String line, int startOfDeclaration) {
        // do a cheap check if this is a preprocessor declaration
        if (line.trim().startsWith("#")) {
            final Node node = parse(line);
            if (node instanceof Declaration) {
                state.accept(startOfDeclaration, (Declaration) node);
            } else {
                throw new UnsupportedOperationException("Only declaration nodes are allowed in the state");
            }
        } else {

            if (state.skipLines()) {
                // TODO: do some fancy indentation extraction
                output.append("// ");
            }
            output.append(line);
            output.append('\n');
        }
    }

    private Node parse(String line) {
        try {
            PPLexer lexer = new PPLexer(CharStreams.fromString(line));
            final PPParser parser = new PPParser(new CommonTokenStream(lexer));

            parser.setErrorHandler(new BailErrorStrategy());

            PreprocessorVisitor visitor = new PreprocessorVisitor();
            PPParser.PreprocessorContext context = parser.preprocessor();
            Node node = context.accept(visitor);

            // TODO: check that the entire line has been consumed
            return node;
        } catch (Exception e) {
            throw new PreprocessorException("Syntax error", e);
        }
    }
}

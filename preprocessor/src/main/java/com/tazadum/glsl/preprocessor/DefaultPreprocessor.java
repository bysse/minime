package com.tazadum.glsl.preprocessor;

import com.tazadum.glsl.preprocessor.language.Declaration;
import com.tazadum.glsl.preprocessor.language.GLSLVersion;
import com.tazadum.glsl.preprocessor.language.Node;
import com.tazadum.glsl.preprocessor.language.PreprocessorVisitor;
import com.tazadum.glsl.preprocessor.model.MacroDefinition;
import com.tazadum.glsl.preprocessor.model.MacroRegistry;
import com.tazadum.glsl.preprocessor.model.PreprocessorState;
import com.tazadum.glsl.preprocessor.parser.PPLexer;
import com.tazadum.glsl.preprocessor.parser.PPParser;
import com.tazadum.glsl.util.SourcePosition;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Default implementation of Preprocessor.
 */
public class DefaultPreprocessor implements Preprocessor {
    private GLSLVersion languageVersion;
    private Pattern declarationPattern;
    private Pattern stringizingPattern;
    private volatile boolean used;

    private int lineNumber = 1;
    private PreprocessorState state = new PreprocessorState();

    /**
     * @param languageVersion The version of the parser that will parse the preprocessed file.
     */
    public DefaultPreprocessor(GLSLVersion languageVersion) {
        this.languageVersion = languageVersion;
        this.declarationPattern = Pattern.compile("^\\s*#\\s*([a-zA-Z9]+)\\s*");
        this.stringizingPattern = Pattern.compile("\\s+##\\s+");
        this.used = false;
    }

    @Override
    public void define(String macro, String value) {
        state.getMacroRegistry().define(macro, value);
    }

    @Override
    public String process(String source) throws IOException {
        byte[] bytes = source.getBytes(StandardCharsets.UTF_8);
        return process(new ByteArrayInputStream(bytes));
    }

    @Override
    public String process(InputStream inputStream) throws IOException {
        if (used) {
            throw new IllegalStateException("The preprocessor is stateful and can only be used once!");
        }
        used = true;

        define("__VERSION__", Objects.toString(languageVersion.getVersionCode()));

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

                // set some built-in macros
                define("__LINE__", Objects.toString(lineNumber + 1));
                define("__FILE__", Objects.toString(lineNumber + 1));

                processLine(output, line, startOfDeclaration);

                lineNumber++;
            }
            return output.toString();
        }
    }

    private void processLine(StringBuilder output, String line, int startOfDeclaration) {
        // do a cheap check if this is a preprocessor declaration
        if (line.trim().startsWith("#")) {
            // then do a bit more expensive test for which declaration we have

            final Matcher matcher = declarationPattern.matcher(line);
            if (matcher.find()) {
                final String declaration = matcher.group(1);

                // check if we need to perform macro expansion, ignore #pragma, #extension, #version
                if (!declaration.equals("pragma") &&
                    !declaration.equals("extension") &&
                    !declaration.equals("version")) {
                    line = expandMacros(line, matcher.end());
                }

                line = applyStringizing(line, matcher.end());

                final Node node = parse(line, startOfDeclaration);
                if (node instanceof Declaration) {
                    state.accept(startOfDeclaration, (Declaration) node);
                } else {
                    throw new UnsupportedOperationException("Only declaration nodes are allowed in the state");
                }

                return;
            }
        }

        if (state.isSectionEnabled()) {
            // Perform macro expansion
            line = expandMacros(line, 0);
            line = applyStringizing(line, 0);
        } else {
            output.append("// ");
        }

        output.append(line);
    }

    /**
     * Apply the stringizing operator.
     *
     * @param line       The line to operate on.
     * @param startIndex The index to start searching from.
     */
    private String applyStringizing(String line, int startIndex) {
        if (line.indexOf("##", startIndex) < 0) {
            return line;
        }

        if (startIndex == 0) {
            return stringizingPattern.matcher(line).replaceAll("");
        }

        final String prefix = line.substring(0, startIndex);
        final String value = line.substring(startIndex);
        return prefix + stringizingPattern.matcher(value).replaceAll("");
    }

    /**
     * Expand macros found on the source line.
     *
     * @param line       The line to expand in.
     * @param startIndex The starting index for expansion.
     */
    private String expandMacros(String line, int startIndex) {
        final MacroRegistry registry = state.getMacroRegistry();
        for (String macro : registry.getMacroNames()) {
            int index = line.indexOf(macro, startIndex);
            if (index < 0) {
                continue;
            }

            int length = macro.length();

            // verify that the macro is surrounded by whitespaces before doing an expansion.
            boolean startOk = (index == startIndex) || isNotIdentifier(line.charAt(index - 1));
            boolean endOk = (index + length == line.length()) || isNotIdentifier(line.charAt(index + length));

            // TODO: add check for parameters

            if (startOk && endOk) {
                // expand the macro
                MacroDefinition definition = registry.getDefinition(macro);

                String prefix = line.substring(0, index);
                String suffix = line.substring(index + length);

                line = prefix + definition.getTemplate() + suffix;
            }
        }

        return line;
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

    private boolean isWhitespace(char ch) {
        return ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r';
    }

    private boolean isAlphaNumeric(char ch) {
        return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9');
    }

    private boolean isNotIdentifier(char ch) {
        return isWhitespace(ch) && !isAlphaNumeric(ch);
    }
}

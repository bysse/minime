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
import com.tazadum.glsl.preprocessor.stage.CommentStage;
import com.tazadum.glsl.preprocessor.stage.LineContinuationStage;
import com.tazadum.glsl.util.SourcePosition;
import com.tazadum.glsl.util.SourcePositionId;
import com.tazadum.glsl.util.io.Source;
import com.tazadum.glsl.util.io.SourceReader;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Default implementation of Preprocessor.
 */
public class DefaultPreprocessor implements Preprocessor {
    private GLSLVersion languageVersion;
    private Pattern declarationPattern;
    private Pattern concatPattern;
    private volatile boolean used;

    private PreprocessorState state;
    private List<String> arguments = new ArrayList<>();

    /**
     * @param languageVersion The version of the parser that will parse the preprocessed file.
     */
    public DefaultPreprocessor(GLSLVersion languageVersion) {
        this.languageVersion = languageVersion;
        this.declarationPattern = Pattern.compile("^\\s*#\\s*([a-zA-Z9]+)\\s*");
        this.concatPattern = Pattern.compile("\\s+##\\s+");
        this.used = false;
        this.state = new PreprocessorState();
    }

    @Override
    public void define(String macro, String value) {
        state.getMacroRegistry().define(macro, value);
    }

    @Override
    public void define(String macro, String[] parameters, String template) {
        state.getMacroRegistry().define(macro, parameters, template);
    }

    @Override
    public String process(Source source) throws IOException {
        if (used) {
            throw new IllegalStateException("The preprocessor is stateful and can only be used once!");
        }
        used = true;

        // setup the sources
        final SourceReader sourceReader = new SourceReader(source);
        final LineContinuationStage continuationStage = new LineContinuationStage(sourceReader, state.getLogKeeper());
        final CommentStage commentStage = new CommentStage(continuationStage);

        define("__VERSION__", Objects.toString(languageVersion.getVersionCode()));

        StringBuilder output = new StringBuilder();

        for (; ; ) {
            int lineNumber = commentStage.getLineNumber();
            String line = commentStage.readLine();
            if (line == null) {
                break;
            }

            final SourcePositionId sourceId = commentStage.getMapper().map(SourcePosition.create(lineNumber, 0));

            // set some built-in macros
            define("__LINE__", Objects.toString(lineNumber));
            define("__FILE__", Objects.toString(sourceId.getPosition().getLine()));

            try {
                processLine(lineNumber, output, line, sourceReader, sourceId);
            } catch (PreprocessorException e) {
                // catch any exception and remap it to the correct source position
                SourcePositionId position = commentStage.getMapper().map(e.getSourcePosition().getPosition());
                throw new PreprocessorException(position, e, e.getMessage());
            }
        }

        // TODO: what about all the warnings?

        return output.toString();
    }

    private void processLine(int lineNumber, StringBuilder output, String line, SourceReader sourceReader, SourcePositionId sourceId) {
        // do a cheap check if this is a preprocessor declaration
        if (line.trim().startsWith("#")) {
            // then do a bit more expensive test for which declaration we have

            final Matcher matcher = declarationPattern.matcher(line);
            if (matcher.find()) {
                final String declaration = matcher.group(1);

                // ignore macro expansion on #pragma, #extension, #version
                if (!declaration.equals("pragma") && !declaration.equals("extension") && !declaration.equals("version")) {
                    // apply macro expansion
                    line = applyOps(lineNumber, line, matcher.end());
                }

                final Node node = parse(line, lineNumber, sourceId);

                // append a commented version of the line to the output
                output.append("// ").append(line).append('\n');

                if (node instanceof Declaration) {
                    state.accept(sourceReader, lineNumber, (Declaration) node);
                } else {
                    throw new UnsupportedOperationException("Only declaration nodes are allowed in the state");
                }

                return;
            }
        }

        if (state.isSectionEnabled()) {
            line = applyOps(lineNumber, line, 0);
        } else {
            output.append("// ");
        }

        output.append(line);
        output.append('\n');
    }

    private String applyOps(int lineNumber, String line, int startIndex) {
        String previous;
        do {
            previous = line;
            line = expandMacros(lineNumber, line, startIndex);
            line = applyConcatOp(line, startIndex);
        } while (!previous.equals(line));

        return line;
    }

    /**
     * Apply the concatenation operator.
     *
     * @param line       The line to operate on.
     * @param startIndex The index to start searching from.
     */
    private String applyConcatOp(String line, int startIndex) {
        if (line.indexOf("##", startIndex) < 0) {
            return line;
        }

        if (startIndex == 0) {
            return concatPattern.matcher(line).replaceAll("");
        }

        final String prefix = line.substring(0, startIndex);
        final String value = line.substring(startIndex);
        return prefix + concatPattern.matcher(value).replaceAll("");
    }

    /**
     * Expand macros found on the source line.
     *
     * @param lineNumber The line number that is being processed.
     * @param line       The line to expand in.
     * @param startIndex The starting index for expansion.
     */
    private String expandMacros(int lineNumber, String line, int startIndex) {
        final MacroRegistry registry = state.getMacroRegistry();
        for (String macro : registry.getMacroNames()) {
            int index = line.indexOf(macro, startIndex);
            if (index < 0) {
                continue;
            }

            int length = macro.length();

            // verify that the macro is surrounded by whitespaces before doing an expansion.
            final boolean startOk = (index == startIndex) || isNotIdentifier(line.charAt(index - 1));
            if (!startOk) {
                continue;
            }

            final MacroDefinition definition = registry.getDefinition(macro);
            String template = definition.getTemplate();

            if (definition.isFunctionLike()) {
                final String[] parameters = definition.getParameters();
                final int endIndex = getMacroParameters(arguments, line, index + length);

                if (arguments.isEmpty() && parameters.length == 0) {
                    // a function like macro without any arguments
                    line = replace(line, index, endIndex, template);
                } else {
                    // verify that argument length is the same as the one expected
                    if (arguments.size() != parameters.length) {
                        throw new PreprocessorException(SourcePositionId.create(lineNumber, index), Message.Error.MACRO_ARG_MISMATCH);
                    }

                    // replace all parameter symbols with the provided arguments
                    int i = 0;
                    for (String argument : arguments) {
                        template = replaceToken(template, parameters[i++], argument);
                    }

                    line = replace(line, index, endIndex, template);
                }
            } else if ((index + length == line.length()) || isNotIdentifier(line.charAt(index + length))) {
                // expand the object-like macro
                line = replace(line, index, index + length, template);
            }
        }

        return line;
    }

    String replaceToken(String template, String token, String replacement) {
        int index, offset = 0, tokenLength = token.length();

        while ((index = template.indexOf(token, offset)) >= 0) {
            final int length = template.length();

            boolean startOk = index == 0 || !isAlphaNumeric(template.charAt(index - 1));
            boolean endOk = index + tokenLength == length || !isAlphaNumeric(template.charAt(index + tokenLength));

            if (startOk && endOk) {
                template = replace(template, index, index + tokenLength, replacement);
                offset += replacement.length() - tokenLength;
            } else {
                offset++;
            }
        }

        return template;
    }

    private String replace(String source, int start, int end, String replacement) {
        final String prefix = source.substring(0, start);
        final String suffix = source.substring(end);
        return prefix + replacement + suffix;
    }

    int getMacroParameters(List<String> arguments, String line, int index) {
        int length = line.length();
        int depth = 0;
        boolean inString = false;
        char previous = '(';

        if (line.charAt(index) != '(') {
            return index;
        }
        index++;

        arguments.clear();

        int argStartIndex = index;
        while (index < length) {
            char ch = line.charAt(index);

            if (previous != '\\' && (ch == '"' || ch == '\'')) {
                inString = !inString;
            } else if (!inString) {
                if (ch == '(') {
                    depth++;
                } else if (ch == ')') {
                    depth--;
                    if (depth < 0) {
                        if (argStartIndex != index) {
                            arguments.add(line.substring(argStartIndex, index));
                        }
                        return index + 1;
                    }
                } else if (ch == ',' && depth == 0) {
                    arguments.add(line.substring(argStartIndex, index));
                    argStartIndex = index + 1;
                }
            }

            previous = ch;
            index++;
        }

        arguments.clear();
        return index;
    }

    /**
     * Parses a source line and creates an AST.
     *
     * @param sourceLine         The source line to parse.
     * @param startOfDeclaration The line offset of the source line.
     * @param sourceId           The source position id of the line.
     * @return An AST node.
     */

    private Node parse(String sourceLine, int startOfDeclaration, SourcePositionId sourceId) {
        try {
            PPLexer lexer = new PPLexer(CharStreams.fromString(sourceLine));
            final PPParser parser = new PPParser(new CommonTokenStream(lexer));

            // TODO: make better bail strategy
            parser.setErrorHandler(new BailErrorStrategy());

            PreprocessorVisitor visitor = new PreprocessorVisitor(sourceId);
            PPParser.PreprocessorContext context = parser.preprocessor();
            return context.accept(visitor);
        } catch (PreprocessorException e) {
            SourcePosition local = e.getSourcePosition().getPosition();
            SourcePositionId position = SourcePositionId.create(startOfDeclaration + local.getLine(), local.getColumn());
            throw new PreprocessorException(position, e, "Syntax error");
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

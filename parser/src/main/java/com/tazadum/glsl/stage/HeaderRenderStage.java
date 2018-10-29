package com.tazadum.glsl.stage;

import com.tazadum.glsl.exception.SourcePositionException;
import com.tazadum.glsl.language.ast.Identifier;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.language.context.GLSLContext;
import com.tazadum.glsl.language.model.StorageQualifier;
import com.tazadum.glsl.language.output.IdentifierOutputMode;
import com.tazadum.glsl.language.output.OutputConfig;
import com.tazadum.glsl.language.output.OutputVisitor;
import com.tazadum.glsl.language.type.TypeQualifierList;
import com.tazadum.glsl.language.variable.VariableRegistry;
import com.tazadum.glsl.language.variable.VariableRegistryContext;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by erikb on 2018-10-24.
 */
public class HeaderRenderStage implements Stage<Pair<Node, ParserContext>, String> {
    private final Logger logger = LoggerFactory.getLogger(HeaderRenderStage.class);

    private final OutputConfig outputConfig;
    private final String shaderId;
    private final String shaderHeader; // only used for shader toy shaders where the uniforms are not included in the source
    private final String indentation;

    public HeaderRenderStage(String shaderName, String shaderHeader, OutputConfig outputConfig) {
        this.shaderHeader = shaderHeader;
        this.indentation = outputConfig.indentation();

        int index = shaderName.lastIndexOf('.');
        if (index > 0) {
            shaderName = shaderName.substring(0, index);
        }
        this.shaderId = shaderName.replaceAll("\\.", "_");

        this.outputConfig = outputConfig.edit()
            .renderNewLines(true)
            .identifierMode(IdentifierOutputMode.Replaced)
            .build();
    }

    @Override
    public StageData<String> process(StageData<Pair<Node, ParserContext>> input) {
        try {
            logger.trace("Rendering the AST to a header file");

            final Node node = input.getData().getFirst();

            final String source = node.accept(new OutputVisitor(outputConfig)).get();
            final String header = generate(input.getData().getSecond(), source);

            return StageData.from(header, null);
        } catch (SourcePositionException e) {
            final String message = e.getSourcePosition().format() + ": " + e.getMessage();
            throw new StageException(message, e);
        }
    }

    public String generate(ParserContext context, String shader) {
        final String MACRO = "__" + shaderId.toUpperCase() + "__";

        StringBuilder builder = new StringBuilder();
        builder.append("#ifndef ").append(MACRO).append("\n");
        builder.append("#define ").append(MACRO).append("\n\n");

        // TODO: render a file header

        // generate identifier mapping
        builder.append("// uniform mapping\n");
        VariableRegistry registry = context.getVariableRegistry();
        for (Map.Entry<GLSLContext, VariableRegistryContext> entry : registry.getDeclarationMap().entrySet()) {
            GLSLContext glslContext = entry.getKey();
            if (glslContext.getParent() != null) {
                // skip everything that is not in global scope
                continue;
            }

            VariableRegistryContext variableContext = entry.getValue();
            for (VariableDeclarationNode declarationNode : variableContext.getVariables()) {
                TypeQualifierList qualifiers = declarationNode.getFullySpecifiedType().getQualifiers();
                if (qualifiers.contains(StorageQualifier.UNIFORM)) {
                    continue;
                }

                final Identifier identifier = declarationNode.getIdentifier();

                builder.append("#define UNIFORM_").append(identifier.original().toUpperCase());
                builder.append('_').append(shaderId.toUpperCase());
                builder.append(' ');
                builder.append('"').append(identifier.token()).append("\"\n");
            }
        }
        builder.append('\n');

        builder.append("// shader source\n");
        builder.append("const char *shader_").append(shaderId).append(" = \n");

        final Pattern commentPattern = Pattern.compile("\\s*/\\*\\s*(.+)\\s*\\*/", Pattern.DOTALL);
        final Pattern indentationPattern = Pattern.compile("^(\\s*)");

        if (shaderHeader.length() > 0) {
            builder.append(indentation).append('"').append(shaderHeader).append("\\n\"\n");
        }

        for (String line : shader.split("\n+")) {
            line = indentationPattern.matcher(line).replaceFirst("$1\"");

            final Matcher matcher = commentPattern.matcher(line);
            if (matcher.find()) {
                line = matcher.replaceFirst("\" // $1");
                builder.append("  \t").append(line).append("\n");
                continue;
            }

            builder.append(indentation).append(line);
            builder.append("\"\n");
        }

        builder.append(indentation).append(";\n\n");
        builder.append("#endif // #ifndef ").append(MACRO).append("\n\n");

        return builder.toString();
    }
}

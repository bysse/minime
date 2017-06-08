package com.tazadum.glsl.output.generator;// (C) King.com Ltd 2017

import com.tazadum.glsl.GLSLOptimizerContext;
import com.tazadum.glsl.ast.Identifier;
import com.tazadum.glsl.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.language.TypeQualifier;
import com.tazadum.glsl.output.OutputConfig;
import com.tazadum.glsl.output.OutputVisitor;
import com.tazadum.glsl.parser.GLSLContext;
import com.tazadum.glsl.parser.variable.VariableRegistry;
import com.tazadum.glsl.parser.variable.VariableRegistryContext;

import java.io.File;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HeaderFileGenerator implements FileGenerator {
    private final String id;
    private final OutputConfig outputConfig;
    private boolean multipleShaders;

    public HeaderFileGenerator(String shaderFilename, OutputConfig outputConfig, boolean multipleShaders) {
        this.outputConfig = outputConfig;
        this.multipleShaders = multipleShaders;
        String name = new File(shaderFilename).getName();
        int index = name.lastIndexOf('.');

        if (index > 0) {
            name = name.substring(0, index);
        }
        this.id = name.replaceAll("\\.", "_");
    }

    @Override
    public String generate(GLSLOptimizerContext context, String shaderGLSL) {
        outputConfig.setNewlines(true);
        outputConfig.setIndentation(3);
        outputConfig.setCommentWithOriginalIdentifiers(true);

        final OutputVisitor visitor = new OutputVisitor(outputConfig);
        String shader = context.getNode().accept(visitor);

        String def = "__" + id.toUpperCase() + "__";

        StringBuilder builder = new StringBuilder();
        builder.append("#ifndef ").append(def).append("\n");
        builder.append("#define ").append(def).append("\n\n");

        // generate identifier mapping
        builder.append("// uniform mapping\n");
        VariableRegistry registry = context.parserContext().getVariableRegistry();
        for (Map.Entry<GLSLContext, VariableRegistryContext> entry : registry.getDeclarationMap().entrySet()) {
            GLSLContext glslContext = entry.getKey();
            if (glslContext.getParent() != null) {
                // skip everything that is not in global scope
                continue;
            }

            VariableRegistryContext variableContext = entry.getValue();
            for (VariableDeclarationNode declarationNode : variableContext.getVariables()) {
                if (TypeQualifier.UNIFORM != declarationNode.getFullySpecifiedType().getQualifier()) {
                    continue;
                }
                Identifier identifier = declarationNode.getIdentifier();
                builder.append("#define UNIFORM_").append(identifier.original().toUpperCase());
                if (multipleShaders) {
                    builder.append('_').append(id.toUpperCase());
                }
                builder.append(' ');
                builder.append('"').append(identifier.token()).append("\"\n");
            }
        }
        builder.append('\n');

        // output shader source
        builder.append("const char *").append(id).append(" = \n");

        Pattern commentPattern = Pattern.compile("/\\*\\s*(.+)\\s*\\*/", Pattern.DOTALL);
        Pattern indentationPattern = Pattern.compile("^(\\s*)");

        for (String line : shader.split("\n+")) {
            line = indentationPattern.matcher(line).replaceFirst("$1\"");

            Matcher matcher = commentPattern.matcher(line);
            if (matcher.find()) {
                line = matcher.replaceFirst("\" // $1");
                builder.append("  \t").append(line).append("\n");
                continue;
            }

            builder.append("   ").append(line).append("\"\n");
        }

        builder.append("   ;\n\n");
        builder.append("#endif // #ifndef ").append(def).append("\n\n");
        return builder.toString();
    }
}

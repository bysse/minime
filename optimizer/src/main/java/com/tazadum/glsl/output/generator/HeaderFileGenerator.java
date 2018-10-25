package com.tazadum.glsl.output.generator;// (C) King.com Ltd 2017

import com.tazadum.glsl.GLSLOptimizerContext;
import com.tazadum.glsl.language.ast.Identifier;
import com.tazadum.glsl.language.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.language.context.GLSLContext;
import com.tazadum.glsl.language.model.StorageQualifier;
import com.tazadum.glsl.language.output.IdentifierOutputMode;
import com.tazadum.glsl.language.output.OutputConfig;
import com.tazadum.glsl.language.output.OutputConfigBuilder;
import com.tazadum.glsl.language.output.OutputVisitor;
import com.tazadum.glsl.language.type.TypeQualifierList;
import com.tazadum.glsl.language.variable.VariableRegistry;
import com.tazadum.glsl.language.variable.VariableRegistryContext;

import java.io.File;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HeaderFileGenerator implements FileGenerator {
    private final String id;
    private final OutputConfig outputConfig;
    private boolean multipleShaders;
    private boolean noPragmaOnce;

    public HeaderFileGenerator(String shaderFilename, boolean multipleShaders, boolean noPragmaOnce) {
        this.multipleShaders = multipleShaders;
        this.noPragmaOnce = noPragmaOnce;
        this.outputConfig = new OutputConfigBuilder()
            .renderNewLines(true)
            .indentation(3)
            .identifierMode(IdentifierOutputMode.Replaced)
            .build();
        ;

        String name = new File(shaderFilename).getName();
        int index = name.lastIndexOf('.');

        if (index > 0) {
            name = name.substring(0, index);
        }
        this.id = name.replaceAll("\\.", "_");
    }

    @Override
    public String generate(GLSLOptimizerContext context, String shaderGLSL) {
        boolean keepNewlines = true;

        final OutputVisitor visitor = new OutputVisitor(outputConfig);
        String shader = context.getNode().accept(visitor).get();

        String def = "__" + id.toUpperCase() + "__";

        StringBuilder builder = new StringBuilder();
        if (!noPragmaOnce) {
            builder.append("#ifndef ").append(def).append("\n");
            builder.append("#define ").append(def).append("\n\n");
        }

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
                TypeQualifierList qualifiers = declarationNode.getFullySpecifiedType().getQualifiers();
                if (qualifiers != null && qualifiers.contains(StorageQualifier.UNIFORM)) {
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

        builder.append("// shader source\n");
        // output shader source
        builder.append("const char *").append(id).append(" = \n");

        Pattern commentPattern = Pattern.compile("\\s*/\\*\\s*(.+)\\s*\\*/", Pattern.DOTALL);
        Pattern indentationPattern = Pattern.compile("^(\\s*)");

        if (context.getHeader().length() > 0) {
            builder.append("  \t\"").append(context.getHeader()).append("\\n\"\n");
        }

        for (String line : shader.split("\n+")) {
            line = indentationPattern.matcher(line).replaceFirst("$1\"");

            Matcher matcher = commentPattern.matcher(line);
            if (matcher.find()) {
                if (keepNewlines) {
                    line = matcher.replaceFirst("\\\\n\" // $1");
                } else {
                    line = matcher.replaceFirst("\" // $1");
                }
                builder.append("  \t").append(line).append("\n");
                continue;
            }

            builder.append("   ").append(line);
            if (keepNewlines) {
                builder.append("\\n");
            }
            builder.append("\"\n");
        }

        builder.append("   ;\n\n");
        if (!noPragmaOnce) {
            builder.append("#endif // #ifndef ").append(def).append("\n\n");
        }

        return builder.toString();
    }
}

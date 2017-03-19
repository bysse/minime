package com.tazadum.glsl.output.generator;// (C) King.com Ltd 2017

import com.tazadum.glsl.GLSLOptimizerContext;
import com.tazadum.glsl.ast.Identifier;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.language.TypeQualifier;
import com.tazadum.glsl.output.OutputConfig;
import com.tazadum.glsl.output.OutputVisitor;
import com.tazadum.glsl.parser.GLSLContext;
import com.tazadum.glsl.parser.variable.VariableRegistry;
import com.tazadum.glsl.parser.variable.VariableRegistryContext;

import java.io.File;
import java.util.Map;
import java.util.Set;

public class HeaderFileGenerator implements FileGenerator {
    private final String id;
    private final OutputConfig outputConfig;

    public HeaderFileGenerator(String shaderFilename, OutputConfig outputConfig) {
        this.outputConfig = outputConfig;
        String name = new File(shaderFilename).getName();
        int index = name.lastIndexOf('.');

        if (index > 0) {
            name = name.substring(0, index);
        }
        this.id = name.replaceAll("\\.", "_");
    }

    @Override
    public String generate(GLSLOptimizerContext optimizerContext, Node shaderNode, String shaderGLSL) {
        outputConfig.setNewlines(true);
        outputConfig.setIndentation(3);
        final OutputVisitor visitor = new OutputVisitor(outputConfig);
        String shader = shaderNode.accept(visitor);

        String def = "__" + id.toUpperCase() + "__";

        StringBuilder builder = new StringBuilder();
        builder.append("#ifndef ").append(def).append("\n");
        builder.append("#define ").append(def).append("\n\n");

        // generate identifier mapping
        builder.append("// uniform mapping\n");
        VariableRegistry registry = optimizerContext.getParserContext().getVariableRegistry();
        for (Map.Entry<GLSLContext, VariableRegistryContext> entry : registry.getDeclarationMap().entrySet()) {
            GLSLContext context = entry.getKey();
            if (context.getParent() != null) {
                // skip everything that is not in global scope
                continue;
            }

            VariableRegistryContext variableContext = entry.getValue();
            for (VariableDeclarationNode declarationNode : variableContext.getVariables()) {
                if (TypeQualifier.UNIFORM != declarationNode.getFullySpecifiedType().getQualifier()) {
                    continue;
                }
                Identifier identifier = declarationNode.getIdentifier();
                builder.append("#define UNIFORM_").append(identifier.original().toUpperCase()).append(' ');
                builder.append(identifier.token()).append('\n');
            }
        }
        builder.append('\n');

        // output shader source
        builder.append("const unsigned char *").append(id).append(" = \n");

        for (String line : shader.split("\n+")) {
            line = line.replaceFirst("^(\\s*)", "$1\"");
            builder.append("   ").append(line).append("\"\n");
        }

        builder.append("   ;\n\n");
        builder.append("#endif // #ifndef ").append(def).append("\n");
        return builder.toString();
    }
}

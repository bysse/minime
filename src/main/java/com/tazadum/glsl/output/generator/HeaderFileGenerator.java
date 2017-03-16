package com.tazadum.glsl.output.generator;// (C) King.com Ltd 2017

import com.tazadum.glsl.GLSLOptimizerContext;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.output.OutputConfig;
import com.tazadum.glsl.output.OutputVisitor;

import java.io.File;

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

package com.tazadum.glsl.output.generator;// (C) King.com Ltd 2017

import com.tazadum.glsl.GLSLOptimizerContext;
import com.tazadum.glsl.ast.Node;

import java.io.File;

public class HeaderFileGenerator implements FileGenerator {
    private final String id;

    public HeaderFileGenerator(String shaderFilename) {
        String name = new File(shaderFilename).getName();
        int index = name.lastIndexOf('.');

        if (index > 0) {
            name = name.substring(0, index);
        }
        this.id = name.replaceAll("\\.", "_");
    }

    @Override
    public String generate(GLSLOptimizerContext optimizerContext, Node shaderNode, String shaderGLSL) {
        String def = "__" + id.toUpperCase() + "__";

        StringBuilder builder = new StringBuilder();
        builder.append("#ifndef ").append(def).append("\n");
        builder.append("#define ").append(def).append("\n");
        builder.append("const unsigned char *").append(id).append(" = ");

        for (String line : shaderGLSL.split("\n+")) {
            builder.append("   \"").append(line).append("\"\n");
        }

        builder.append("   ;");
        builder.append("#endif // #ifndef ").append(def).append("\n");
        return builder.toString();
    }
}

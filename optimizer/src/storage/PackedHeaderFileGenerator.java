package com.tazadum.glsl.output.generator;// (C) King.com Ltd 2017

import com.tazadum.glsl.optimizer.OptimizerContext;
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
import java.util.regex.Pattern;

public class PackedHeaderFileGenerator implements FileGenerator {
    private final String id;
    private final OutputConfig outputConfig;
    private boolean multipleShaders;
    private static boolean functionWritten = false;

    public PackedHeaderFileGenerator(String shaderFilename, boolean multipleShaders) {
        this.outputConfig = new OutputConfigBuilder()
            .renderNewLines(true)
            .indentation(3)
            .identifierMode(IdentifierOutputMode.Replaced)
            .build();
        this.multipleShaders = multipleShaders;
        String name = new File(shaderFilename).getName();
        int index = name.lastIndexOf('.');

        if (index > 0) {
            name = name.substring(0, index);
        }
        this.id = name.replaceAll("\\.", "_");
    }

    @Override
    public String generate(OptimizerContext context, String shaderGLSL) {
        final boolean keepNewlines = true;

        final OutputVisitor visitor = new OutputVisitor(outputConfig);
        String shader = context.getNode().accept(visitor).get();

        String def = "__" + id.toUpperCase() + "__";

        StringBuilder builder = new StringBuilder();
        builder.append("#ifndef ").append(def).append("\n");
        builder.append("#define ").append(def).append("\n\n");

        if (!functionWritten) {
            writeUnpackFunction(builder);
            functionWritten = true;
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

        // remove all comments
        final Pattern commentPattern = Pattern.compile("\\s*/\\*\\s*(.+)\\s*\\*/", Pattern.DOTALL);
        String source = commentPattern.matcher(shaderGLSL).replaceAll("");


        // output shader source
        builder.append("// Size of the unpacked shader source buffer\n");
        builder.append("#define ").append(id.toUpperCase()).append("_SIZE ").append(source.length()).append("\n\n");

        builder.append("// shader source, 7 bit packed\n");
        builder.append("const char ").append(id).append("[] = {\n    ");

        int[] encoded = encode(source);
        for (int i=0;i<encoded.length;i++ ) {
            if (i > 0) {
                builder.append(", ");
                if (i % 20 == 0) {
                    builder.append("\n    ");
                }
            }
            builder.append(String.format("0x%02X", encoded[i]));
        }
        builder.append("\n    };\n\n");

        builder.append("#endif // #ifndef ").append(def).append("\n\n");
        return builder.toString();
    }

    public static int[] encode(String source) {
        int size = (int)Math.ceil(source.length() * 7f / 8f);
        int[] encoded = new int[size];

        int offset = 0;
        for (int i=0;i<source.length();i++) {
            if (source.charAt(i) < 0x1f) {
                continue;
            }
            int value = (source.charAt(i) - 0x1f) & 0x7f;
            int bit = offset & 0x07;
            int index = offset >> 3;

            encoded[index] |= (value << bit) & 0xff;
            offset += 0x07;

            int next = offset >> 3;
            if (next != index) {
                bit = 8 - bit;
                encoded[next] = value >> bit;
            }
        }
        encoded[(offset>>3)] = 0;
        return encoded;
    }

    private void writeUnpackFunction(StringBuilder builder) {
        builder.append("// use this function to unpack the shader source\n");
        builder.append("void unpack_shader(const char *buffer, char *dest) {\n");
        builder.append("    unsigned char memory = 0;\n");
        builder.append("    int bit = 0;\n");
        builder.append("    while (*buffer != 0) {\n");
        builder.append("        int value = *buffer;\n");
        builder.append("        if (bit != 0) {\n");
        builder.append("            value <<= (0x8 - bit);\n");
        builder.append("            value |= memory >> bit;\n");
        builder.append("        }\n");
        builder.append("        *dest++ = (value & 0x7f) + 0x1f;\n");
        builder.append("        memory = *buffer;\n");
        builder.append("        bit = (bit + 0x7) & 0x7;\n");
        builder.append("        if (bit != 0) {\n");
        builder.append("            buffer++;\n");
        builder.append("        }\n");
        builder.append("    }\n");
        builder.append("}\n\n");
    }
}

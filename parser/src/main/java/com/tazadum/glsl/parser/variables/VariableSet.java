package com.tazadum.glsl.parser.variables;

import com.tazadum.glsl.language.ast.struct.InterfaceBlockNode;
import com.tazadum.glsl.language.ast.struct.StructDeclarationNode;
import com.tazadum.glsl.language.ast.type.ArraySpecifier;
import com.tazadum.glsl.language.ast.variable.VariableDeclarationListNode;
import com.tazadum.glsl.language.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.language.model.ArraySpecifiers;
import com.tazadum.glsl.language.model.StorageQualifier;
import com.tazadum.glsl.language.type.*;
import com.tazadum.glsl.parser.ShaderType;
import com.tazadum.glsl.preprocessor.language.GLSLProfile;

import java.util.ArrayList;
import java.util.List;

import static com.tazadum.glsl.language.model.StorageQualifier.CONST;
import static com.tazadum.glsl.language.type.PredefinedType.INT;
import static com.tazadum.glsl.language.type.PredefinedType.IVEC3;
import static com.tazadum.glsl.util.SourcePosition.TOP;

/**
 * Created by erikb on 2018-10-22.
 */
public class VariableSet {
    public static final int ARRAY_SIZE_OF_UNKNOWNS = 255;

    private List<VariableDeclarationNode> declarations = new ArrayList<>();
    private List<InterfaceBlockNode> interfaceBlocks = new ArrayList<>();

    public VariableSet(GLSLProfile profile, ShaderType shaderType) {
        add(variable(INT, "gl_MaxVertexAttribs", CONST));
        add(variable(INT, "gl_MaxVertexUniformVectors", CONST));
        add(variable(INT, "gl_MaxVertexUniformComponents", CONST));
        add(variable(INT, "gl_MaxVertexOutputComponents", CONST));
        add(variable(INT, "gl_MaxVaryingComponents", CONST));
        add(variable(INT, "gl_MaxVaryingVectors", CONST));
        add(variable(INT, "gl_MaxVertexTextureImageUnits", CONST));
        add(variable(INT, "gl_MaxVertexImageUniforms", CONST));
        add(variable(INT, "gl_MaxVertexAtomicCounters", CONST));
        add(variable(INT, "gl_MaxVertexAtomicCounterBuffers", CONST));
        add(variable(INT, "gl_MaxTessControlInputComponents", CONST));
        add(variable(INT, "gl_MaxTessControlOutputComponents", CONST));
        add(variable(INT, "gl_MaxTessControlTextureImageUnits", CONST));
        add(variable(INT, "gl_MaxTessControlUniformComponents", CONST));
        add(variable(INT, "gl_MaxTessControlTotalOutputComponents", CONST));
        add(variable(INT, "gl_MaxTessControlImageUniforms", CONST));
        add(variable(INT, "gl_MaxTessControlAtomicCounters", CONST));
        add(variable(INT, "gl_MaxTessControlAtomicCounterBuffers", CONST));
        add(variable(INT, "gl_MaxTessEvaluationInputComponents", CONST));
        add(variable(INT, "gl_MaxTessEvaluationOutputComponents", CONST));
        add(variable(INT, "gl_MaxTessEvaluationTextureImageUnits", CONST));
        add(variable(INT, "gl_MaxTessEvaluationUniformComponents", CONST));
        add(variable(INT, "gl_MaxTessEvaluationImageUniforms", CONST));
        add(variable(INT, "gl_MaxTessEvaluationAtomicCounters", CONST));
        add(variable(INT, "gl_MaxTessEvaluationAtomicCounterBuffers", CONST));
        add(variable(INT, "gl_MaxTessPatchComponents", CONST));
        add(variable(INT, "gl_MaxPatchVertices", CONST));
        add(variable(INT, "gl_MaxTessGenLevel", CONST));
        add(variable(INT, "gl_MaxGeometryInputComponents", CONST));
        add(variable(INT, "gl_MaxGeometryOutputComponents", CONST));
        add(variable(INT, "gl_MaxGeometryImageUniforms", CONST));
        add(variable(INT, "gl_MaxGeometryTextureImageUnits", CONST));
        add(variable(INT, "gl_MaxGeometryOutputVertices", CONST));
        add(variable(INT, "gl_MaxGeometryTotalOutputComponents", CONST));
        add(variable(INT, "gl_MaxGeometryUniformComponents", CONST));
        add(variable(INT, "gl_MaxGeometryVaryingComponents", CONST));
        add(variable(INT, "gl_MaxGeometryAtomicCounters", CONST));
        add(variable(INT, "gl_MaxGeometryAtomicCounterBuffers", CONST));
        add(variable(INT, "gl_MaxFragmentImageUniforms", CONST));
        add(variable(INT, "gl_MaxFragmentInputComponents", CONST));
        add(variable(INT, "gl_MaxFragmentUniformVectors", CONST));
        add(variable(INT, "gl_MaxFragmentUniformComponents", CONST));
        add(variable(INT, "gl_MaxFragmentAtomicCounters", CONST));
        add(variable(INT, "gl_MaxFragmentAtomicCounterBuffers", CONST));
        add(variable(INT, "gl_MaxDrawBuffers", CONST));
        add(variable(INT, "gl_MaxTextureImageUnits", CONST));
        add(variable(INT, "gl_MinProgramTexelOffset", CONST));
        add(variable(INT, "gl_MaxProgramTexelOffset", CONST));
        add(variable(INT, "gl_MaxImageUnits", CONST));
        add(variable(INT, "gl_MaxSamples", CONST));
        add(variable(INT, "gl_MaxImageSamples", CONST));
        add(variable(INT, "gl_MaxClipDistances", CONST));
        add(variable(INT, "gl_MaxCullDistances", CONST));
        add(variable(INT, "gl_MaxViewports", CONST));
        add(variable(INT, "gl_MaxComputeImageUniforms", CONST));
        add(variable(IVEC3, 3, "gl_MaxComputeWorkGroupCount", CONST));
        add(variable(IVEC3, 3, "gl_MaxComputeWorkGroupSize", CONST));
        add(variable(INT, "gl_MaxComputeUniformComponents", CONST));
        add(variable(INT, "gl_MaxComputeTextureImageUnits", CONST));
        add(variable(INT, "gl_MaxComputeAtomicCounters", CONST));
        add(variable(INT, "gl_MaxComputeAtomicCounterBuffers", CONST));
        add(variable(INT, "gl_MaxCombinedTextureImageUnits", CONST));
        add(variable(INT, "gl_MaxCombinedImageUniforms", CONST));
        add(variable(INT, "gl_MaxCombinedImageUnitsAndFragmentOutputs", CONST));
        add(variable(INT, "gl_MaxCombinedShaderOutputResources", CONST));
        add(variable(INT, "gl_MaxCombinedAtomicCounters", CONST));
        add(variable(INT, "gl_MaxCombinedAtomicCounterBuffers", CONST));
        add(variable(INT, "gl_MaxCombinedClipAndCullDistances", CONST));
        add(variable(INT, "gl_MaxAtomicCounterBindings", CONST));
        add(variable(INT, "gl_MaxAtomicCounterBufferSize", CONST));
        add(variable(INT, "gl_MaxTransformFeedbackBuffers", CONST));
        add(variable(INT, "gl_MaxTransformFeedbackInterleavedComponents", CONST));

        if (profile == GLSLProfile.COMPATIBILITY) {
            add(variable(INT, "gl_MaxTextureUnits", CONST));
            add(variable(INT, "gl_MaxTextureCoords", CONST));
            add(variable(INT, "gl_MaxClipPlanes", CONST));
            add(variable(INT, "gl_MaxVaryingFloats", CONST));

            if (shaderType != ShaderType.COMPUTE) {

            }
        }
    }

    public List<VariableDeclarationNode> getDeclarations() {
        return declarations;
    }

    public List<InterfaceBlockNode> getInterfaceBlocks() {
        return interfaceBlocks;
    }

    protected void add(VariableDeclarationNode declaration) {
        declarations.add(declaration);
    }

    protected void add(InterfaceBlockNode interfaceBlock) {
        interfaceBlocks.add(interfaceBlock);
    }

    protected VariableDeclarationNode variable(PredefinedType type, String identifier, TypeQualifier... qualifiers) {
        TypeQualifierList qualifierList = new TypeQualifierList();
        for (TypeQualifier qualifier : qualifiers) {
            qualifierList.add(qualifier);
        }
        return new VariableDeclarationNode(TOP, true, new FullySpecifiedType(qualifierList, type), identifier, null, null, null);
    }

    protected VariableDeclarationNode variable(PredefinedType type, int dimension, String identifier, TypeQualifier... qualifiers) {
        TypeQualifierList qualifierList = new TypeQualifierList();
        for (TypeQualifier qualifier : qualifiers) {
            qualifierList.add(qualifier);
        }
        return new VariableDeclarationNode(TOP, true, new FullySpecifiedType(qualifierList, new ArrayType(type, dimension)), identifier, null, null, null);
    }


    protected InterfaceBlockNode block(String typeName, String instanceName, StorageQualifier qualifier, int arrayDimension, VariableDeclarationNode... variables) {
        final TypeQualifierList qualifierList = new TypeQualifierList();
        qualifierList.add(qualifier);

        StructDeclarationNode structDeclaration = new StructDeclarationNode(TOP, typeName);
        for (VariableDeclarationNode variable : variables) {
            VariableDeclarationListNode listNode = new VariableDeclarationListNode(TOP, variable.getFullySpecifiedType());
            listNode.addChild(variable);
            structDeclaration.addFieldDeclaration(listNode);
        }

        ArraySpecifiers arraySpecifiers = null;
        if (arrayDimension > 0) {
            arraySpecifiers = new ArraySpecifiers();
            arraySpecifiers.addSpecifier(new ArraySpecifier(TOP, arrayDimension));
        }

        return new InterfaceBlockNode(TOP, qualifierList, structDeclaration, null, instanceName, arraySpecifiers);
    }


}

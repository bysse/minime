package com.tazadum.glsl.language.type;

import com.tazadum.glsl.exception.NoSuchFieldException;
import com.tazadum.glsl.exception.TypeException;
import com.tazadum.glsl.language.HasToken;
import com.tazadum.glsl.parser.GLSLParser;

import static com.tazadum.glsl.exception.Errors.Type.UNKNOWN_TYPE_ERROR;

/**
 * Created by erikb on 2018-09-15.
 */
public enum PredefinedType implements GLSLType, HasToken {
    VOID("void", GLSLParser.VOID, TypeCategory.NoFields),
    FLOAT("float", GLSLParser.FLOAT, TypeCategory.Scalar),
    DOUBLE("double", GLSLParser.DOUBLE, TypeCategory.Scalar),
    INT("int", GLSLParser.INT, TypeCategory.Scalar),
    UINT("uint", GLSLParser.UINT, TypeCategory.Scalar),
    BOOL("bool", GLSLParser.BOOL, TypeCategory.NoFields),
    VEC2("vec2", GLSLParser.VEC2, TypeCategory.Vector, TypeBase.FLOAT, 2),
    VEC3("vec3", GLSLParser.VEC3, TypeCategory.Vector, TypeBase.FLOAT, 3),
    VEC4("vec4", GLSLParser.VEC4, TypeCategory.Vector, TypeBase.FLOAT, 4),
    DVEC2("dvec2", GLSLParser.DVEC2, TypeCategory.Vector, TypeBase.DOUBLE, 2),
    DVEC3("dvec3", GLSLParser.DVEC3, TypeCategory.Vector, TypeBase.DOUBLE, 3),
    DVEC4("dvec4", GLSLParser.DVEC4, TypeCategory.Vector, TypeBase.DOUBLE, 4),
    BVEC2("bvec2", GLSLParser.BVEC2, TypeCategory.Vector, TypeBase.BOOL, 2),
    BVEC3("bvec3", GLSLParser.BVEC3, TypeCategory.Vector, TypeBase.BOOL, 3),
    BVEC4("bvec4", GLSLParser.BVEC4, TypeCategory.Vector, TypeBase.BOOL, 4),
    IVEC2("ivec2", GLSLParser.IVEC2, TypeCategory.Vector, TypeBase.INT, 2),
    IVEC3("ivec3", GLSLParser.IVEC3, TypeCategory.Vector, TypeBase.INT, 3),
    IVEC4("ivec4", GLSLParser.IVEC4, TypeCategory.Vector, TypeBase.INT, 4),
    UVEC2("uvec2", GLSLParser.UVEC2, TypeCategory.Vector, TypeBase.UINT, 2),
    UVEC3("uvec3", GLSLParser.UVEC3, TypeCategory.Vector, TypeBase.UINT, 3),
    UVEC4("uvec4", GLSLParser.UVEC4, TypeCategory.Vector, TypeBase.UINT, 4),
    MAT2("mat2", GLSLParser.MAT2, TypeCategory.Matrix, TypeBase.FLOAT),
    MAT3("mat3", GLSLParser.MAT3, TypeCategory.Matrix, TypeBase.FLOAT),
    MAT4("mat4", GLSLParser.MAT4, TypeCategory.Matrix, TypeBase.FLOAT),
    MAT2X2("mat2x2", GLSLParser.MAT2X2, TypeCategory.Matrix, TypeBase.FLOAT),
    MAT2X3("mat2x3", GLSLParser.MAT2X3, TypeCategory.Matrix, TypeBase.FLOAT),
    MAT2X4("mat2x4", GLSLParser.MAT2X4, TypeCategory.Matrix, TypeBase.FLOAT),
    MAT3X2("mat3x2", GLSLParser.MAT3X2, TypeCategory.Matrix, TypeBase.FLOAT),
    MAT3X3("mat3x3", GLSLParser.MAT3X3, TypeCategory.Matrix, TypeBase.FLOAT),
    MAT3X4("mat3x4", GLSLParser.MAT3X4, TypeCategory.Matrix, TypeBase.FLOAT),
    MAT4X2("mat4x2", GLSLParser.MAT4X2, TypeCategory.Matrix, TypeBase.FLOAT),
    MAT4X3("mat4x3", GLSLParser.MAT4X3, TypeCategory.Matrix, TypeBase.FLOAT),
    MAT4X4("mat4x4", GLSLParser.MAT4X4, TypeCategory.Matrix, TypeBase.FLOAT),
    DMAT2("dmat2", GLSLParser.DMAT2, TypeCategory.Matrix, TypeBase.DOUBLE),
    DMAT3("dmat3", GLSLParser.DMAT3, TypeCategory.Matrix, TypeBase.DOUBLE),
    DMAT4("dmat4", GLSLParser.DMAT4, TypeCategory.Matrix, TypeBase.DOUBLE),
    DMAT2X2("dmat2x2", GLSLParser.DMAT2X2, TypeCategory.Matrix, TypeBase.DOUBLE),
    DMAT2X3("dmat2x3", GLSLParser.DMAT2X3, TypeCategory.Matrix, TypeBase.DOUBLE),
    DMAT2X4("dmat2x4", GLSLParser.DMAT2X4, TypeCategory.Matrix, TypeBase.DOUBLE),
    DMAT3X2("dmat3x2", GLSLParser.DMAT3X2, TypeCategory.Matrix, TypeBase.DOUBLE),
    DMAT3X3("dmat3x3", GLSLParser.DMAT3X3, TypeCategory.Matrix, TypeBase.DOUBLE),
    DMAT3X4("dmat3x4", GLSLParser.DMAT3X4, TypeCategory.Matrix, TypeBase.DOUBLE),
    DMAT4X2("dmat4x2", GLSLParser.DMAT4X2, TypeCategory.Matrix, TypeBase.DOUBLE),
    DMAT4X3("dmat4x3", GLSLParser.DMAT4X3, TypeCategory.Matrix, TypeBase.DOUBLE),
    DMAT4X4("dmat4x4", GLSLParser.DMAT4X4, TypeCategory.Matrix, TypeBase.DOUBLE),
    ATOMIC_UINT("atomic_uint", GLSLParser.ATOMIC_UINT, TypeCategory.Scalar),
    SAMPLER1D("sampler1D", GLSLParser.SAMPLER1D, TypeCategory.NoFields, TypeBase.FLOAT),
    SAMPLER2D("sampler2D", GLSLParser.SAMPLER2D, TypeCategory.NoFields, TypeBase.FLOAT),
    SAMPLER3D("sampler3D", GLSLParser.SAMPLER3D, TypeCategory.NoFields, TypeBase.FLOAT),
    SAMPLERCUBE("samplerCube", GLSLParser.SAMPLERCUBE, TypeCategory.NoFields, TypeBase.FLOAT),
    SAMPLER1DSHADOW("sampler1DShadow", GLSLParser.SAMPLER1DSHADOW, TypeCategory.NoFields, TypeBase.FLOAT),
    SAMPLER2DSHADOW("sampler2DShadow", GLSLParser.SAMPLER2DSHADOW, TypeCategory.NoFields, TypeBase.FLOAT),
    SAMPLERCUBESHADOW("samplerCubeShadow", GLSLParser.SAMPLERCUBESHADOW, TypeCategory.NoFields, TypeBase.FLOAT),
    SAMPLER1DARRAY("sampler1DArray", GLSLParser.SAMPLER1DARRAY, TypeCategory.NoFields, TypeBase.FLOAT),
    SAMPLER2DARRAY("sampler2DArray", GLSLParser.SAMPLER2DARRAY, TypeCategory.NoFields, TypeBase.FLOAT),
    SAMPLER1DARRAYSHADOW("sampler1DArrayShadow", GLSLParser.SAMPLER1DARRAYSHADOW, TypeCategory.NoFields, TypeBase.FLOAT),
    SAMPLER2DARRAYSHADOW("sampler2DArrayShadow", GLSLParser.SAMPLER2DARRAYSHADOW, TypeCategory.NoFields, TypeBase.FLOAT),
    SAMPLERCUBEARRAY("samplerCubeArray", GLSLParser.SAMPLERCUBEARRAY, TypeCategory.NoFields, TypeBase.FLOAT),
    SAMPLERCUBEARRAYSHADOW("samplerCubeArrayShadow", GLSLParser.SAMPLERCUBEARRAYSHADOW, TypeCategory.NoFields, TypeBase.FLOAT),
    ISAMPLER1D("isampler1D", GLSLParser.ISAMPLER1D, TypeCategory.NoFields, TypeBase.INT),
    ISAMPLER2D("isampler2D", GLSLParser.ISAMPLER2D, TypeCategory.NoFields, TypeBase.INT),
    ISAMPLER3D("isampler3D", GLSLParser.ISAMPLER3D, TypeCategory.NoFields, TypeBase.INT),
    ISAMPLERCUBE("isamplerCube", GLSLParser.ISAMPLERCUBE, TypeCategory.NoFields, TypeBase.INT),
    ISAMPLER1DARRAY("isampler1DArray", GLSLParser.ISAMPLER1DARRAY, TypeCategory.NoFields, TypeBase.INT),
    ISAMPLER2DARRAY("isampler2DArray", GLSLParser.ISAMPLER2DARRAY, TypeCategory.NoFields, TypeBase.INT),
    ISAMPLERCUBEARRAY("isamplerCubeArray", GLSLParser.ISAMPLERCUBEARRAY, TypeCategory.NoFields, TypeBase.INT),
    USAMPLER1D("usampler1D", GLSLParser.USAMPLER1D, TypeCategory.NoFields, TypeBase.UINT),
    USAMPLER2D("usampler2D", GLSLParser.USAMPLER2D, TypeCategory.NoFields, TypeBase.UINT),
    USAMPLER3D("usampler3D", GLSLParser.USAMPLER3D, TypeCategory.NoFields, TypeBase.UINT),
    USAMPLERCUBE("usamplerCube", GLSLParser.USAMPLERCUBE, TypeCategory.NoFields, TypeBase.UINT),
    USAMPLER1DARRAY("usampler1DArray", GLSLParser.USAMPLER1DARRAY, TypeCategory.NoFields, TypeBase.UINT),
    USAMPLER2DARRAY("usampler2DArray", GLSLParser.USAMPLER2DARRAY, TypeCategory.NoFields, TypeBase.UINT),
    USAMPLERCUBEARRAY("usamplerCubeArray", GLSLParser.USAMPLERCUBEARRAY, TypeCategory.NoFields, TypeBase.UINT),
    SAMPLER2DRECT("sampler2DRect", GLSLParser.SAMPLER2DRECT, TypeCategory.NoFields),
    SAMPLER2DRECTSHADOW("sampler2DRectShadow", GLSLParser.SAMPLER2DRECTSHADOW, TypeCategory.NoFields),
    ISAMPLER2DRECT("isampler2DRect", GLSLParser.ISAMPLER2DRECT, TypeCategory.NoFields, TypeBase.INT),
    USAMPLER2DRECT("usampler2DRect", GLSLParser.USAMPLER2DRECT, TypeCategory.NoFields, TypeBase.UINT),
    SAMPLERBUFFER("samplerBuffer", GLSLParser.SAMPLERBUFFER, TypeCategory.NoFields),
    ISAMPLERBUFFER("isamplerBuffer", GLSLParser.ISAMPLERBUFFER, TypeCategory.NoFields, TypeBase.INT),
    USAMPLERBUFFER("usamplerBuffer", GLSLParser.USAMPLERBUFFER, TypeCategory.NoFields, TypeBase.UINT),
    SAMPLER2DMS("sampler2DMS", GLSLParser.SAMPLER2DMS, TypeCategory.NoFields),
    ISAMPLER2DMS("isampler2DMS", GLSLParser.ISAMPLER2DMS, TypeCategory.NoFields, TypeBase.INT),
    USAMPLER2DMS("usampler2DMS", GLSLParser.USAMPLER2DMS, TypeCategory.NoFields, TypeBase.UINT),
    SAMPLER2DMSARRAY("sampler2DMSArray", GLSLParser.SAMPLER2DMSARRAY, TypeCategory.NoFields),
    ISAMPLER2DMSARRAY("isampler2DMSArray", GLSLParser.ISAMPLER2DMSARRAY, TypeCategory.NoFields, TypeBase.INT),
    USAMPLER2DMSARRAY("usampler2DMSArray", GLSLParser.USAMPLER2DMSARRAY, TypeCategory.NoFields, TypeBase.UINT),
    IMAGE1D("image1D", GLSLParser.IMAGE1D, TypeCategory.NoFields, TypeBase.FLOAT),
    IIMAGE1D("iimage1D", GLSLParser.IIMAGE1D, TypeCategory.NoFields, TypeBase.INT),
    UIMAGE1D("uimage1D", GLSLParser.UIMAGE1D, TypeCategory.NoFields, TypeBase.UINT),
    IMAGE2D("image2D", GLSLParser.IMAGE2D, TypeCategory.NoFields, TypeBase.FLOAT),
    IIMAGE2D("iimage2D", GLSLParser.IIMAGE2D, TypeCategory.NoFields, TypeBase.INT),
    UIMAGE2D("uimage2D", GLSLParser.UIMAGE2D, TypeCategory.NoFields, TypeBase.UINT),
    IMAGE3D("image3D", GLSLParser.IMAGE3D, TypeCategory.NoFields, TypeBase.FLOAT),
    IIMAGE3D("iimage3D", GLSLParser.IIMAGE3D, TypeCategory.NoFields, TypeBase.INT),
    UIMAGE3D("uimage3D", GLSLParser.UIMAGE3D, TypeCategory.NoFields, TypeBase.UINT),
    IMAGE2DRECT("image2DRect", GLSLParser.IMAGE2DRECT, TypeCategory.NoFields, TypeBase.FLOAT),
    IIMAGE2DRECT("iimage2DRect", GLSLParser.IIMAGE2DRECT, TypeCategory.NoFields, TypeBase.INT),
    UIMAGE2DRECT("uimage2DRect", GLSLParser.UIMAGE2DRECT, TypeCategory.NoFields, TypeBase.UINT),
    IMAGECUBE("imageCube", GLSLParser.IMAGECUBE, TypeCategory.NoFields, TypeBase.FLOAT),
    IIMAGECUBE("iimageCube", GLSLParser.IIMAGECUBE, TypeCategory.NoFields, TypeBase.INT),
    UIMAGECUBE("uimageCube", GLSLParser.UIMAGECUBE, TypeCategory.NoFields, TypeBase.UINT),
    IMAGEBUFFER("imageBuffer", GLSLParser.IMAGEBUFFER, TypeCategory.NoFields, TypeBase.FLOAT),
    IIMAGEBUFFER("iimageBuffer", GLSLParser.IIMAGEBUFFER, TypeCategory.NoFields, TypeBase.INT),
    UIMAGEBUFFER("uimageBuffer", GLSLParser.UIMAGEBUFFER, TypeCategory.NoFields, TypeBase.UINT),
    IMAGE1DARRAY("image1DArray", GLSLParser.IMAGE1DARRAY, TypeCategory.NoFields, TypeBase.FLOAT),
    IIMAGE1DARRAY("iimage1DArray", GLSLParser.IIMAGE1DARRAY, TypeCategory.NoFields, TypeBase.INT),
    UIMAGE1DARRAY("uimage1DArray", GLSLParser.UIMAGE1DARRAY, TypeCategory.NoFields, TypeBase.UINT),
    IMAGE2DARRAY("image2DArray", GLSLParser.IMAGE2DARRAY, TypeCategory.NoFields, TypeBase.FLOAT),
    IIMAGE2DARRAY("iimage2DArray", GLSLParser.IIMAGE2DARRAY, TypeCategory.NoFields, TypeBase.INT),
    UIMAGE2DARRAY("uimage2DArray", GLSLParser.UIMAGE2DARRAY, TypeCategory.NoFields, TypeBase.UINT),
    IMAGECUBEARRAY("imageCubeArray", GLSLParser.IMAGECUBEARRAY, TypeCategory.NoFields, TypeBase.FLOAT),
    IIMAGECUBEARRAY("iimageCubeArray", GLSLParser.IIMAGECUBEARRAY, TypeCategory.NoFields, TypeBase.INT),
    UIMAGECUBEARRAY("uimageCubeArray", GLSLParser.UIMAGECUBEARRAY, TypeCategory.NoFields, TypeBase.UINT),
    IMAGE2DMS("image2DMS", GLSLParser.IMAGE2DMS, TypeCategory.NoFields, TypeBase.FLOAT),
    IIMAGE2DMS("iimage2DMS", GLSLParser.IIMAGE2DMS, TypeCategory.NoFields, TypeBase.INT),
    UIMAGE2DMS("uimage2DMS", GLSLParser.UIMAGE2DMS, TypeCategory.NoFields, TypeBase.UINT),
    IMAGE2DMSARRAY("image2DMSArray", GLSLParser.IMAGE2DMSARRAY, TypeCategory.NoFields, TypeBase.FLOAT),
    IIMAGE2DMSARRAY("iimage2DMSArray", GLSLParser.IIMAGE2DMSARRAY, TypeCategory.NoFields, TypeBase.INT),
    UIMAGE2DMSARRAY("uimage2DMSArray", GLSLParser.UIMAGE2DMSARRAY, TypeCategory.NoFields);

    private final String token;
    private final int id;
    private final TypeCategory category;
    private final TypeBase typeBase;
    private final int components;

    PredefinedType(String token, int id, TypeCategory category) {
        this.token = token;
        this.id = id;
        this.category = category;
        this.typeBase = null;
        this.components = 0;
    }

    PredefinedType(String token, int id, TypeCategory category, TypeBase typeBase) {
        this.token = token;
        this.id = id;
        this.category = category;
        this.typeBase = typeBase;
        this.components = 0;
    }

    PredefinedType(String token, int id, TypeCategory category, TypeBase typeBase, int components) {
        this.token = token;
        this.id = id;
        this.category = category;
        this.typeBase = typeBase;
        this.components = components;
    }

    @Override
    public String token() {
        return token;
    }

    @Override
    public int tokenId() {
        return id;
    }

    public TypeCategory category() {
        return category;
    }

    @Override
    public GLSLType fieldType(String fieldName) {
        if (category != TypeCategory.Vector) {
            throw new NoSuchFieldException(fieldName, token);
        }

        // verify all characters in the field selection
        for (int i = 0; i < fieldName.length(); i++) {
            if (!VectorField.isVectorComponent(fieldName.charAt(i))) {
                throw TypeException.noSuchField(name(), fieldName);
            }
        }

        final GLSLType baseType = baseType();
        assert baseType != null : "All vector fields should have a valid base type";


        switch (this) {
            case VEC2:
            case VEC3:
            case VEC4:
                return typeSwizzle(fieldName, FLOAT, VEC2, VEC3, VEC4);
            case DVEC2:
            case DVEC3:
            case DVEC4:
                return typeSwizzle(fieldName, DOUBLE, DVEC2, DVEC3, DVEC4);
            case BVEC2:
            case BVEC3:
            case BVEC4:
                return typeSwizzle(fieldName, BOOL, BVEC2, BVEC3, BVEC4);
            case IVEC2:
            case IVEC3:
            case IVEC4:
                return typeSwizzle(fieldName, INT, IVEC2, IVEC3, IVEC4);
            case UVEC2:
            case UVEC3:
            case UVEC4:
                return typeSwizzle(fieldName, UINT, UVEC2, UVEC3, UVEC4);
        }

        throw new TypeException(UNKNOWN_TYPE_ERROR("bad implementation of type " + name()));
    }

    @Override
    public boolean isAssignableBy(GLSLType glslType) {
        assert glslType != null : "Null type passed to function";

        if (this == glslType) {
            return true;
        }

        if (!(glslType instanceof PredefinedType) || isArray() != glslType.isArray()) {
            return false;
        }

        final PredefinedType type = (PredefinedType) glslType;
        switch (this) {
            case INT:
                return comapre(type, UINT, FLOAT, DOUBLE);
            case UINT:
                return comapre(type, FLOAT, DOUBLE);
            case FLOAT:
                return type == DOUBLE;

            case IVEC2:
                return comapre(type, UVEC2, VEC2, DVEC2);
            case IVEC3:
                return comapre(type, UVEC3, VEC3, DVEC3);
            case IVEC4:
                return comapre(type, UVEC4, VEC4, DVEC4);

            case UVEC2:
                return comapre(type, VEC2, DVEC2);
            case UVEC3:
                return comapre(type, VEC3, DVEC3);
            case UVEC4:
                return comapre(type, VEC4, DVEC4);

            case VEC2:
                return type == DVEC2;
            case VEC3:
                return type == DVEC3;
            case VEC4:
                return type == DVEC4;

            case MAT2:
                return type == DMAT2;
            case MAT3:
                return type == DMAT3;
            case MAT4:
                return type == DMAT4;

            case MAT2X2:
                return comapre(type, MAT2, DMAT2);
            case MAT2X3:
                return type == DMAT2X3;
            case MAT2X4:
                return type == DMAT2X4;

            case MAT3X2:
                return type == DMAT3X2;
            case MAT3X3:
                return comapre(type, MAT3, DMAT3);
            case MAT3X4:
                return type == DMAT3X4;

            case MAT4X2:
                return type == DMAT4X2;
            case MAT4X3:
                return type == DMAT4X3;
            case MAT4X4:
                return comapre(type, MAT4, DMAT4);
        }

        return false;
    }

    private boolean comapre(PredefinedType type, PredefinedType... conversions) {
        for (PredefinedType conversion : conversions) {
            if (type == conversion) {
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean isArray() {
        return category == TypeCategory.Matrix;
    }

    @Override
    public GLSLType baseType() {
        if (typeBase != null) {
            switch (typeBase) {
                case INT:
                    return INT;
                case UINT:
                    return UINT;
                case BOOL:
                    return BOOL;
                case DOUBLE:
                    return DOUBLE;
                case FLOAT:
                    return FLOAT;
            }
        }
        return null;
    }

    /**
     * Returns the appropriate type based on the swizzle length.
     */
    private PredefinedType typeSwizzle(String fieldName, PredefinedType... types) {
        final int length = fieldName.length();
        if (length > types.length) {
            throw new NoSuchFieldException(fieldName, token);
        }
        return types[length - 1];
    }

    /**
     * Return the number of components in the type.
     */
    public int components() {
        return components;
    }
}

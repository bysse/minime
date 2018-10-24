package com.tazadum.glsl.parser.functions;

import com.tazadum.glsl.language.function.BuiltInFunctionRegistry;
import com.tazadum.glsl.language.type.GenTypes;
import com.tazadum.glsl.language.type.PredefinedType;
import com.tazadum.glsl.preprocessor.language.GLSLProfile;
import com.tazadum.glsl.util.Pair;

import java.util.ArrayList;
import java.util.List;

import static com.tazadum.glsl.language.type.GenTypes.*;
import static com.tazadum.glsl.language.type.PredefinedType.*;

/**
 * Integer Functions
 * Created by erikb on 2018-10-23.
 */
public class ImageFunctionSet implements FunctionSet {
    List<Pair<GenTypes, PredefinedType>> imageParams;

    public ImageFunctionSet() {
        imageParams = new ArrayList<>();

        imageParams.add(Pair.create(GenImageBuffer, INT));
        imageParams.add(Pair.create(GenImage1D, INT));
        imageParams.add(Pair.create(GenImage1DArray, IVEC2));
        imageParams.add(Pair.create(GenImage2D, IVEC2));
        imageParams.add(Pair.create(GenImageRect, IVEC2));
        imageParams.add(Pair.create(GenImage2DArray, IVEC3));
        imageParams.add(Pair.create(GenImage3D, IVEC3));
        imageParams.add(Pair.create(GenImageCube, IVEC3));
        imageParams.add(Pair.create(GenImageCubeArray, IVEC3));

        imageParams.add(Pair.create(GenImage2DMS, IVEC2)); // + INT
        imageParams.add(Pair.create(GenImage2DMSArray, IVEC3)); // + INT
    }

    @Override
    public void generate(BuiltInFunctionRegistry registry, GLSLProfile profile) {
        BuiltInFunctionRegistry.FunctionDeclarator def = registry.getFunctionDeclarator();

        def.function("imageSize", INT, GenImage1D);
        def.function("imageSize", INT, GenImageBuffer);
        def.function("imageSize", IVEC2, GenImage2D);
        def.function("imageSize", IVEC2, GenImageCube);
        def.function("imageSize", IVEC2, GenImageRect);
        def.function("imageSize", IVEC2, GenImage1DArray);
        def.function("imageSize", IVEC2, GenImage2DMS);
        def.function("imageSize", IVEC3, GenImage3D);
        def.function("imageSize", IVEC3, GenImageCubeArray);
        def.function("imageSize", IVEC3, GenImage2DArray);
        def.function("imageSize", IVEC3, GenImage2DMSArray);

        def.function("imageSamples", INT, GenImage2DMS);
        def.function("imageSamples", INT, GenImage2DMSArray);

        for (Pair<GenTypes, PredefinedType> param : imageParams) {
            final GenTypes genTypes = param.getFirst();
            final PredefinedType P = param.getSecond();

            for (int i = 0; i < 3; i++) {
                PredefinedType gimage = genTypes.types[i];
                PredefinedType gvec4 = GenVec4Type.types[i];

                def.function("imageLoad", gvec4, gimage, P);
                def.function("imageStore", VOID, gimage, P, VEC4);
                def.function("imageAtomicAdd", UINT, gimage, P, UINT);
                def.function("imageAtomicAdd", INT, gimage, P, INT);

                def.function("imageAtomicMin", UINT, gimage, P, UINT);
                def.function("imageAtomicMin", INT, gimage, P, INT);
                def.function("imageAtomicMax", UINT, gimage, P, UINT);
                def.function("imageAtomicMax", INT, gimage, P, INT);
                def.function("imageAtomicAnd", UINT, gimage, P, UINT);
                def.function("imageAtomicAnd", INT, gimage, P, INT);
                def.function("imageAtomicOr", UINT, gimage, P, UINT);
                def.function("imageAtomicOr", INT, gimage, P, INT);
                def.function("imageAtomicXor", UINT, gimage, P, UINT);
                def.function("imageAtomicXor", INT, gimage, P, INT);
                def.function("imageAtomicExchange", UINT, gimage, P, UINT);
                def.function("imageAtomicExchange", INT, gimage, P, INT);
                def.function("imageAtomicExchange", FLOAT, gimage, P, FLOAT);

                def.function("imageAtomicCompSwap", UINT, gimage, P, UINT, UINT);
                def.function("imageAtomicCompSwap", INT, gimage, P, INT, INT);
            }
        }
    }
}

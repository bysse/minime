package com.tazadum.glsl.parser.functions;

import com.tazadum.glsl.language.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.language.function.BuiltInFunctionRegistry;
import com.tazadum.glsl.language.function.FunctionPrototype;
import com.tazadum.glsl.language.model.StorageQualifier;
import com.tazadum.glsl.language.type.FullySpecifiedType;
import com.tazadum.glsl.language.type.PredefinedType;
import com.tazadum.glsl.util.SourcePosition;

import static com.tazadum.glsl.language.model.StorageQualifier.IN;
import static com.tazadum.glsl.language.model.StorageQualifier.OUT;
import static com.tazadum.glsl.language.type.GenTypes.GenIType;
import static com.tazadum.glsl.language.type.GenTypes.GenUType;
import static com.tazadum.glsl.language.type.PredefinedType.INT;
import static com.tazadum.glsl.language.type.PredefinedType.VOID;

/**
 * Integer Functions
 * Created by erikb on 2018-10-23.
 */
public class IntegerFunctionSet implements FunctionSet {

    @Override
    public void generate(BuiltInFunctionRegistry registry) {
        BuiltInFunctionRegistry.FunctionDeclarator declarator = registry.getFunctionDeclarator();

        declarator.function("uaddCarry", GenUType, GenUType, GenUType);
        declarator.function("usubBorrow", GenUType, GenUType, GenUType);

        for (PredefinedType type : GenUType.types) {
            fixedOutput(declarator, "umulExtended", VOID, type, type, type, type);
        }
        for (PredefinedType type : GenIType.types) {
            fixedOutput(declarator, "umulExtended", VOID, type, type, type, type);
        }

        declarator.function("bitfieldExtract", GenIType, GenIType, INT, INT);
        declarator.function("bitfieldExtract", GenUType, GenUType, INT, INT);
        declarator.function("bitfieldInsert", GenIType, GenIType, GenIType, INT, INT);
        declarator.function("bitfieldInsert", GenUType, GenUType, GenUType, INT, INT);
        declarator.function("bitfieldReverse", GenIType, GenIType);
        declarator.function("bitfieldReverse", GenUType, GenUType);
        declarator.function("bitCount", GenIType, GenIType);
        declarator.function("bitCount", GenIType, GenUType);
        declarator.function("findLSB", GenIType, GenIType);
        declarator.function("findLSB", GenIType, GenUType);
        declarator.function("findMSB", GenIType, GenIType);
        declarator.function("findMSB", GenIType, GenUType);
    }

    private void fixedOutput(BuiltInFunctionRegistry.FunctionDeclarator declarator, String identifier, PredefinedType returnType, PredefinedType arg0, PredefinedType arg1, PredefinedType arg2, PredefinedType arg3) {
        final FunctionPrototypeNode node = new FunctionPrototypeNode(SourcePosition.TOP, identifier, new FullySpecifiedType(returnType));

        PredefinedType[] types = {arg0, arg1, arg2, arg3};
        StorageQualifier[] qualifiers = {IN, IN, OUT, OUT};

        node.setPrototype(new FunctionPrototype(true, returnType, types, qualifiers));
        declarator.function(node);
    }
}

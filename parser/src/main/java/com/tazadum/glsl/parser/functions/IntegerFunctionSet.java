package com.tazadum.glsl.parser.functions;

import com.tazadum.glsl.language.function.BuiltInFunctionRegistry;
import com.tazadum.glsl.language.type.PredefinedType;
import com.tazadum.glsl.preprocessor.language.GLSLProfile;

import static com.tazadum.glsl.language.type.GenTypeIterator.inout;
import static com.tazadum.glsl.language.type.GenTypeIterator.out;
import static com.tazadum.glsl.language.type.GenTypes.GenIType;
import static com.tazadum.glsl.language.type.GenTypes.GenUType;
import static com.tazadum.glsl.language.type.PredefinedType.*;

/**
 * Integer Functions
 * Created by erikb on 2018-10-23.
 */
public class IntegerFunctionSet implements FunctionSet {

    @Override
    public void generate(BuiltInFunctionRegistry registry, GLSLProfile profile) {
        BuiltInFunctionRegistry.FunctionDeclarator def = registry.getFunctionDeclarator();

        def.function("uaddCarry", GenUType, GenUType, GenUType);
        def.function("usubBorrow", GenUType, GenUType, GenUType);

        for (PredefinedType type : GenUType.types) {
            def.function("umulExtended", VOID, type, type, out(type), out(type));
        }
        for (PredefinedType type : GenIType.types) {
            def.function("umulExtended", VOID, type, type, out(type), out(type));
        }

        def.function("bitfieldExtract", GenIType, GenIType, INT, INT);
        def.function("bitfieldExtract", GenUType, GenUType, INT, INT);
        def.function("bitfieldInsert", GenIType, GenIType, GenIType, INT, INT);
        def.function("bitfieldInsert", GenUType, GenUType, GenUType, INT, INT);
        def.function("bitfieldReverse", GenIType, GenIType);
        def.function("bitfieldReverse", GenUType, GenUType);
        def.function("bitCount", GenIType, GenIType);
        def.function("bitCount", GenIType, GenUType);
        def.function("findLSB", GenIType, GenIType);
        def.function("findLSB", GenIType, GenUType);
        def.function("findMSB", GenIType, GenIType);
        def.function("findMSB", GenIType, GenUType);

        // Atomic Counter Functions
        def.function("atomicCounterIncrement", UINT, ATOMIC_UINT);
        def.function("atomicCounterDecrement", UINT, ATOMIC_UINT);

        def.function("atomicCounter", UINT, ATOMIC_UINT);
        def.function("atomicCounterAdd", UINT, ATOMIC_UINT, UINT);
        def.function("atomicCounterSubtract", UINT, ATOMIC_UINT, UINT);
        def.function("atomicCounterMin", UINT, ATOMIC_UINT, UINT);
        def.function("atomicCounterMax", UINT, ATOMIC_UINT, UINT);
        def.function("atomicCounterAnd", UINT, ATOMIC_UINT, UINT);
        def.function("atomicCounterOr", UINT, ATOMIC_UINT, UINT);
        def.function("atomicCounterXor", UINT, ATOMIC_UINT, UINT);
        def.function("atomicCounterExchange", UINT, ATOMIC_UINT, UINT);
        def.function("atomicCounterCompSwap", UINT, ATOMIC_UINT, UINT, UINT);

        // Atomic Memory Functions
        def.function("atomicAdd", UINT, inout(UINT), UINT);
        def.function("atomicAdd", INT, inout(INT), INT);
        def.function("atomicMin", UINT, inout(UINT), UINT);
        def.function("atomicMin", INT, inout(INT), INT);
        def.function("atomicMin", UINT, inout(UINT), UINT);
        def.function("atomicMin", INT, inout(INT), INT);
        def.function("atomicMax", UINT, inout(UINT), UINT);
        def.function("atomicMax", INT, inout(INT), INT);
        def.function("atomicAnd", UINT, inout(UINT), UINT);
        def.function("atomicAnd", INT, inout(INT), INT);
        def.function("atomicOr", UINT, inout(UINT), UINT);
        def.function("atomicOr", INT, inout(INT), INT);
        def.function("atomicXor", UINT, inout(UINT), UINT);
        def.function("atomicXor", INT, inout(INT), INT);
        def.function("atomicExchange", UINT, inout(UINT), UINT);
        def.function("atomicExchange", INT, inout(INT), INT);
        def.function("atomicCompSwap", UINT, inout(UINT), UINT, UINT);
        def.function("atomicCompSwap", INT, inout(INT), INT, INT);
    }
}

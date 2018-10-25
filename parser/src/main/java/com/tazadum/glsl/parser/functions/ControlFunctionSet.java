package com.tazadum.glsl.parser.functions;

import com.tazadum.glsl.language.function.BuiltInFunctionRegistry;
import com.tazadum.glsl.preprocessor.language.GLSLProfile;

import static com.tazadum.glsl.language.type.PredefinedType.BOOL;
import static com.tazadum.glsl.language.type.PredefinedType.VOID;

/**
 * Control Functions
 * Created by erikb on 2018-10-24.
 */
public class ControlFunctionSet implements FunctionSet {
    @Override
    public void generate(BuiltInFunctionRegistry registry, GLSLProfile profile) {
        BuiltInFunctionRegistry.FunctionDeclarator def = registry.getFunctionDeclarator();

        def.function("memoryBarrier", VOID);
        def.function("memoryBarrierAtomicCounter", VOID);
        def.function("memoryBarrierBuffer", VOID);
        def.function("memoryBarrierShared", VOID);
        def.function("memoryBarrierImage", VOID);
        def.function("groupMemoryBarrier", VOID);

        def.function("anyInvocation", BOOL, BOOL);
        def.function("allInvocations", BOOL, BOOL);
        def.function("allInvocationsEqual", BOOL, BOOL);
    }
}

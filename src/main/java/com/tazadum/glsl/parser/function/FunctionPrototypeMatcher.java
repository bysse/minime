package com.tazadum.glsl.parser.function;

import com.tazadum.glsl.language.GLSLType;

import java.util.Arrays;

public class FunctionPrototypeMatcher {
    public static final GLSLType ANY = null;

    private final GLSLType returnType;
    private final GLSLType[] parameterTypes;

    public FunctionPrototypeMatcher(GLSLType returnType, GLSLType... parameterTypes) {
        this.returnType = returnType;
        this.parameterTypes = parameterTypes;
    }

    public boolean matches(FunctionPrototype prototype) {
        GLSLType[] parameters = prototype.getParameterTypes();

        if (parameterTypes.length != parameters.length) {
            return false;
        }
        if (returnType != ANY && !returnType.isAssignableBy(prototype.getReturnType())) {
            return false;
        }

        for (int i = 0; i < this.parameterTypes.length; i++) {
            if (this.parameterTypes[i] == ANY) {
                continue;
            }

            if (!parameters[i].isAssignableBy(parameterTypes[i])) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return Arrays.toString(parameterTypes) + "->" + returnType;
    }
}

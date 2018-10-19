package com.tazadum.glsl.language.function;


import com.tazadum.glsl.language.type.GLSLType;

import java.util.Arrays;

/**
 * A class that matches which overloaded function a specific call is meant for.
 */
public class FunctionPrototypeMatcher {
    public static final GLSLType ANY = null;

    private final GLSLType returnType;
    private final GLSLType[] parameterTypes;

    /**
     * Creates a matcher.
     *
     * @param returnType     The return type of the target function.
     * @param parameterTypes The parameter types of the target function.
     */
    public FunctionPrototypeMatcher(GLSLType returnType, GLSLType... parameterTypes) {
        this.returnType = returnType;
        this.parameterTypes = parameterTypes;
    }

    /**
     * Creates a matcher.
     *
     * @param prototype The prototype to use as the template.
     */
    public FunctionPrototypeMatcher(FunctionPrototype prototype) {
        this.returnType = prototype.getReturnType();
        this.parameterTypes = prototype.getParameterTypes();
    }

    /**
     * Tests if a function prototype/call matches a prototype.
     *
     * @param prototype The prototype/call to test.
     * @return True if the prototype/call matches.
     */
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

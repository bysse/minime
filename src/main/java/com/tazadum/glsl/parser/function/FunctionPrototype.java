package com.tazadum.glsl.parser.function;

import com.tazadum.glsl.language.GLSLType;

import java.util.List;

public class FunctionPrototype {
    private GLSLType returnType;
    private GLSLType[] parameterTypes;

    public FunctionPrototype(GLSLType returnType, List<GLSLType> parameterTypes) {
        this.returnType = returnType;
        this.parameterTypes = parameterTypes.toArray(new GLSLType[parameterTypes.size()]);
    }

    public FunctionPrototype(GLSLType returnType, GLSLType... parameterTypes) {
        this.returnType = returnType;
        this.parameterTypes = parameterTypes;
    }

    public GLSLType[] getParameterTypes() {
        return parameterTypes;
    }

    public GLSLType getReturnType() {
        return returnType;
    }

    @Override
    public String toString() {
        return "FunctionPrototype{" +
            "parameterTypes=" + parameterTypes +
            ", returnType=" + returnType +
            '}';
    }
}

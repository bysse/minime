package com.tazadum.glsl.parser.function;

import com.tazadum.glsl.language.GLSLType;

import java.util.Arrays;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FunctionPrototype that = (FunctionPrototype) o;

        if (!returnType.equals(that.returnType)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(parameterTypes, that.parameterTypes);

    }

    @Override
    public int hashCode() {
        int result = returnType.hashCode();
        result = 31 * result + Arrays.hashCode(parameterTypes);
        return result;
    }

    @Override
    public String toString() {
        return "FunctionPrototype{" +
            "parameterTypes=" + parameterTypes +
            ", returnType=" + returnType +
            '}';
    }
}

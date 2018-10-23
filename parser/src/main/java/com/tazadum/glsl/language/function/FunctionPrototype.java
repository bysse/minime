package com.tazadum.glsl.language.function;

import com.tazadum.glsl.language.model.StorageQualifier;
import com.tazadum.glsl.language.type.GLSLType;

import java.util.Arrays;
import java.util.List;

public class FunctionPrototype {
    private boolean builtIn;
    private GLSLType returnType;
    private GLSLType[] parameterTypes;
    private StorageQualifier[] parameterQualifiers;

    public FunctionPrototype(boolean builtIn, GLSLType returnType, List<GLSLType> parameterTypes) {
        this.builtIn = builtIn;
        this.returnType = returnType;
        this.parameterTypes = parameterTypes.toArray(new GLSLType[parameterTypes.size()]);
        this.parameterQualifiers = null;
    }

    public FunctionPrototype(boolean builtIn, GLSLType returnType, GLSLType... parameterTypes) {
        this.builtIn = builtIn;
        this.returnType = returnType;
        this.parameterTypes = parameterTypes;
        this.parameterQualifiers = null;
    }

    public FunctionPrototype(boolean builtIn, GLSLType returnType, List<GLSLType> parameterTypes, List<StorageQualifier> parameterQualifier) {
        this.builtIn = builtIn;
        this.returnType = returnType;
        this.parameterTypes = parameterTypes.toArray(new GLSLType[parameterTypes.size()]);
        this.parameterQualifiers = parameterQualifier.toArray(new StorageQualifier[parameterQualifier.size()]);
    }

    public FunctionPrototype(boolean builtIn, GLSLType returnType, GLSLType[] parameterTypes, StorageQualifier[] parameterQualifiers) {
        this.builtIn = builtIn;
        this.returnType = returnType;
        this.parameterTypes = parameterTypes;
        this.parameterQualifiers = parameterQualifiers;
    }

    public boolean isBuiltIn() {
        return builtIn;
    }

    public GLSLType[] getParameterTypes() {
        return parameterTypes;
    }

    public StorageQualifier getParameterQualifier(int index) {
        if (parameterQualifiers == null) {
            return StorageQualifier.IN;
        }
        return parameterQualifiers[index];
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
        return Arrays.toString(parameterTypes) + "->" + returnType;
    }
}

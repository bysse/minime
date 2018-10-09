package com.tazadum.glsl.language.type;

import java.util.Iterator;

/**
 * Created by Erik on 2016-10-17.
 */
public class GenTypeIterator implements Iterator<PredefinedType[]> {
    private Object[] parameters;
    private GenTypes gentype;
    private int index;

    public GenTypeIterator(Object... parameters) {
        this.parameters = parameters;
        this.gentype = null;
        this.index = 0;
        for (Object parameter : parameters) {
            if (parameter instanceof GenTypes) {
                if (this.gentype == null) {
                    this.gentype = (GenTypes) parameter;
                } else if (this.gentype != parameter) {
                    throw new IllegalArgumentException("Different GenTypes specified!");
                }
                continue;
            }
            if (parameter instanceof PredefinedType) {
                continue;
            }
            throw new IllegalArgumentException("Invalid argument type!");
        }

        if (gentype == null) {
            throw new IllegalArgumentException("No GenTypes specified!");
        }
    }

    @Override
    public boolean hasNext() {
        return index < gentype.concreteTypes.length;
    }

    @Override
    public PredefinedType[] next() {
        final PredefinedType type = gentype.concreteTypes[index++];
        final PredefinedType[] types = new PredefinedType[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            final Object parameter = parameters[i];
            if (parameter instanceof PredefinedType) {
                types[i] = (PredefinedType) parameter;
                continue;
            }
            if (parameter instanceof GenTypes) {
                types[i] = type;
                continue;
            }
            throw new IllegalArgumentException("Unknown argument type");
        }
        return types;
    }
}

package com.tazadum.glsl.language.type;

import java.util.Arrays;
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
                    throw new IllegalArgumentException("Different GenTypes specified! " + Arrays.toString(parameters));
                }
                continue;
            }
            if (parameter instanceof PredefinedType) {
                continue;
            }
            throw new IllegalArgumentException("Invalid argument type!");
        }
    }

    @Override
    public boolean hasNext() {
        if (gentype == null) {
            return index == 0;
        }
        return index < gentype.types.length;
    }

    @Override
    public PredefinedType[] next() {
        if (gentype == null) {
            index++;
            final PredefinedType[] types = new PredefinedType[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                types[i] = (PredefinedType) parameters[i];
            }
            return types;
        }
        final PredefinedType type = gentype.types[index++];
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

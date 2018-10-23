package com.tazadum.glsl.language.type;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by Erik on 2016-10-17.
 */
public class GenTypeIterator implements Iterator<PredefinedType[]> {
    private static final int MAX_GENTYPES = 3;
    private Object[] parameters;
    private GenTypes[] genTypes;
    private int[] genTypeIndex;
    private int[] index;
    private boolean fixedFunction = true;

    public GenTypeIterator(Object... parameters) {
        this.parameters = parameters;

        this.genTypes = new GenTypes[MAX_GENTYPES];
        this.genTypeIndex = new int[parameters.length];
        int types = 0;

        Arrays.fill(genTypeIndex, 0);

        outer:
        for (int i = 0; i < parameters.length; i++) {
            Object parameter = parameters[i];
            if (parameter instanceof GenTypes) {
                if (types >= MAX_GENTYPES) {
                    throw new IllegalArgumentException("More than three GenTypes specified! " + Arrays.toString(parameters));
                }
                GenTypes genType = (GenTypes) parameter;
                for (int j = 0; j < types; j++) {
                    if (this.genTypes[j] == genType) {
                        genTypeIndex[i] = j;
                        continue outer;
                    }
                }

                this.genTypes[types] = genType;
                this.genTypeIndex[i] = types;
                types++;
                continue;
            }
            if (parameter instanceof PredefinedType) {
                continue;
            }
            throw new IllegalArgumentException("Invalid argument type!");
        }

        index = new int[types];
        Arrays.fill(index, 0);
    }

    @Override
    public boolean hasNext() {
        if (index.length == 0) {
            return fixedFunction;
        }

        return index[index.length - 1] < genTypes[index.length - 1].types.length;
    }

    @Override
    public PredefinedType[] next() {
        if (index.length == 0) {
            if (!fixedFunction) {
                throw new NoSuchElementException("Fixed function combination already delivered");
            }
            fixedFunction = false;
            final PredefinedType[] types = new PredefinedType[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                types[i] = (PredefinedType) parameters[i];
            }
            return types;
        }

        if (!hasNext()) {
            throw new NoSuchElementException("Combinations exhausted");
        }

        final PredefinedType[] types = new PredefinedType[parameters.length];


        for (int i = 0; i < parameters.length; i++) {
            final Object parameter = parameters[i];
            if (parameter instanceof PredefinedType) {
                types[i] = (PredefinedType) parameter;
            } else if (parameter instanceof GenTypes) {
                // find out the genTypeIndex
                int index = genTypeIndex[i];
                final int typeIndex = this.index[index];
                final GenTypes genType = genTypes[index];
                types[i] = genType.types[typeIndex];
            }
        }

        // increment counters
        int i = 0;
        while (i < index.length) {
            index[i]++;
            if (index[i] < genTypes[i].types.length) {
                break;
            }
            if (i + 1 < index.length) {
                index[i] = 0;
            }
            i++;
        }
        return types;
    }
}

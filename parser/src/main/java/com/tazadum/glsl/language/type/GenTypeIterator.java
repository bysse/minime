package com.tazadum.glsl.language.type;

import com.tazadum.glsl.exception.NoSuchFieldException;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by Erik on 2016-10-17.
 */
public class GenTypeIterator implements Iterator<GLSLType[]> {
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
            if (parameter instanceof GLSLType) {
                continue;
            }
            throw new IllegalArgumentException("Invalid argument type!");
        }

        index = new int[types];
        Arrays.fill(index, 0);
    }

    public static GLSLType out(GLSLType type) {
        return new OutWrapper(type);
    }

    public static GLSLType inout(GLSLType type) {
        return new InOutWrapper(type);
    }

    @Override
    public boolean hasNext() {
        if (index.length == 0) {
            return fixedFunction;
        }

        return index[index.length - 1] < genTypes[index.length - 1].types.length;
    }

    @Override
    public GLSLType[] next() {
        if (index.length == 0) {
            if (!fixedFunction) {
                throw new NoSuchElementException("Fixed function combination already delivered");
            }
            fixedFunction = false;
            final GLSLType[] types = new GLSLType[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                types[i] = (GLSLType) parameters[i];
            }
            return types;
        }

        if (!hasNext()) {
            throw new NoSuchElementException("Combinations exhausted");
        }

        final GLSLType[] types = new GLSLType[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            final Object parameter = parameters[i];
            if (parameter instanceof GLSLType) {
                types[i] = (GLSLType) parameter;
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

    public static class OutWrapper implements GLSLType {
        private final GLSLType type;

        public OutWrapper(GLSLType type) {
            this.type = type;
        }

        @Override
        public GLSLType fieldType(String fieldName) throws NoSuchFieldException {
            return null;
        }

        @Override
        public boolean isAssignableBy(GLSLType type) {
            return false;
        }

        @Override
        public boolean isArray() {
            return false;
        }

        @Override
        public GLSLType baseType() {
            return type;
        }

        @Override
        public String token() {
            return null;
        }
    }

    public static class InOutWrapper implements GLSLType {
        private final GLSLType type;

        public InOutWrapper(GLSLType type) {
            this.type = type;
        }

        @Override
        public GLSLType fieldType(String fieldName) throws NoSuchFieldException {
            return null;
        }

        @Override
        public boolean isAssignableBy(GLSLType type) {
            return false;
        }

        @Override
        public boolean isArray() {
            return false;
        }

        @Override
        public GLSLType baseType() {
            return type;
        }

        @Override
        public String token() {
            return null;
        }
    }
}

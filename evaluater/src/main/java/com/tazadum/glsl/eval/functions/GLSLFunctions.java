package com.tazadum.glsl.eval.functions;

import com.tazadum.glsl.eval.type.TypedFloat;
import com.tazadum.glsl.eval.type.TypedVariable;

import static com.tazadum.glsl.language.type.PredefinedType.FLOAT;

public class GLSLFunctions {
    public static void verifyArgumentCount(int count, TypedVariable[] arguments) {
        if (arguments.length != count) {
            throw new IllegalArgumentException("Invalid number of arguments, expected " + count);
        }
    }

    public static TypedVariable sin(TypedVariable[] arguments) {
        verifyArgumentCount(1, arguments);

        TypedVariable arg = arguments[0];

        if (arg.getType() == FLOAT) {
            return new TypedFloat((float)Math.sin(arg.getValueAsFloat()));
        }

        // TODO: add for other types

        return null;
    }

    public static TypedVariable cos(TypedVariable[] arguments) {
        verifyArgumentCount(1, arguments);

        TypedVariable arg = arguments[0];

        if (arg.getType() == FLOAT) {
            return new TypedFloat((float)Math.cos(arg.getValueAsFloat()));
        }

        // TODO: add for other types

        return null;
    }

    public static TypedVariable add(TypedVariable[] arguments) {
        if (arguments[0].getType() == FLOAT && arguments[1].getType() == FLOAT) {
            return new TypedFloat(arguments[0].getValueAsFloat() + arguments[1].getValueAsFloat());
        }
        return null;
    }

    public static TypedVariable sub(TypedVariable[] arguments) {
        return null;
    }
}

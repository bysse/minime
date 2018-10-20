package com.tazadum.glsl.language.type;

import static com.tazadum.glsl.exception.Errors.Coarse.ILLEGAL_SWIZZLE;

public class VectorField {
    private static final String[] fieldSelection = {"xyzw", "rgba", "stpq"};

    private int[] field;
    private boolean inOrder;

    public VectorField(StringBuilder builder) throws NoSuchFieldException {
        this(builder.toString());
    }

    public VectorField(String selection) throws NoSuchFieldException {
        field = new int[selection.length()];
        inOrder = true;

        for (int i = 0; i < field.length; i++) {
            final int index = vectorComponentIndex(selection.charAt(i));

            if (index < 0) {
                throw new NoSuchFieldException(ILLEGAL_SWIZZLE(selection.charAt(i)));
            }

            if (i != index) {
                inOrder = false;
            }

            field[i] = index;
        }
    }

    public int components() {
        return field.length;
    }

    /**
     * Returns true if the components in the field are in order.
     *
     * @return true if the components are in order.
     */
    public boolean componentsInOrder() {
        return inOrder;
    }

    public String render(int set) {
        StringBuilder builder = new StringBuilder();
        for (int index : field) {
            builder.append(getVectorComponent(index, set));
        }
        return builder.toString();
    }

    public String toString() {
        return render(0);
    }

    /**
     * Returns true if the provided character is a valid vector field component.
     */
    public static boolean isVectorComponent(char ch) {
        return vectorComponentIndex(ch) >= 0;
    }

    public static char getVectorComponent(int index, int set) {
        if (set < 0 || set >= fieldSelection.length) {
            return fieldSelection[0].charAt(0);
        }
        if (index < 0 || index > 3) {
            return fieldSelection[set].charAt(0);
        }
        return fieldSelection[set].charAt(index);
    }

    public static int vectorComponentIndex(char ch) {
        switch (ch) {
            case 'r': // rgba
            case 's': // stpq
            case 'x': // xyzw
                return 0;
            case 'g': // rgba
            case 't': // stpg
            case 'y': // xyzw
                return 1;
            case 'b': // rgba
            case 'p': // stpg
            case 'z': // xyzw
                return 2;
            case 'a': // rgba
            case 'q': // stpg
            case 'w': // xyzw
                return 3;
            default:
                return -1;
        }
    }
}

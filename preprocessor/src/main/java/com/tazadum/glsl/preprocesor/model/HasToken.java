package com.tazadum.glsl.preprocesor.model;


/**
 * Created by erikb on 2018-09-15.
 */
public interface HasToken {
    /**
     * Returns the token associated with the symbol.
     */
    String token();

    /**
     * Does a linear search through the list of values to search for an item that matches the provided token.
     *
     * @param token  Token to search for.
     * @param values The values to search through.
     * @param <T>    Subtype to HasToken.
     * @return The matched value or null if no value matched the token.
     */
    static <T extends HasToken> T fromString(String token, T... values) {
        if (token == null) {
            throw new IllegalArgumentException("The token must not be null");
        }
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("No possible values provided for resolution");
        }
        for (T t : values) {
            if (token.equals(t.token())) {
                return t;
            }
        }
        return null;
    }
}

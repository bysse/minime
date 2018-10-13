package com.tazadum.glsl.language;


import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by erikb on 2018-09-15.
 */
public interface HasToken {
    int NO_TOKEN_ID = -1;

    /**
     * Returns the token associated with the symbol.
     */
    String token();

    /**
     * Returns the ANTLR parser token id.
     */
    int tokenId();

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

    /**
     * Does a linear search through the list of values to search for an item that matches the provided token.
     *
     * @param tokenId Token id to search for.
     * @param values  The values to search through.
     * @param <T>     Subtype to HasToken.
     * @return The matched value or null if no value matched the token.
     */
    static <T extends HasToken> T fromTokenId(int tokenId, T... values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("No possible values provided for resolution");
        }
        for (T t : values) {
            if (tokenId == t.tokenId()) {
                return t;
            }
        }
        return null;
    }

    /**
     * Does a linear search through the list of values to search for an item that matches the provided token.
     *
     * @param ctx    ANTLR rule context to parse.
     * @param values The values to search through.
     * @param <T>    Subtype to HasToken.
     * @return The matched value or null if no value matched the token.
     */
    static <T extends HasToken> T fromToken(ParserRuleContext ctx, T... values) {
        return fromTokenId(ctx.getStart().getType(), values);
    }

    /**
     * Does a linear search through the list of provided token looking for one that are not null in the context.
     *
     * @param ctx    ANTLR rule context to parse.
     * @param values The values to search through.
     * @param <T>    Subtype to HasToken.
     * @return The matched value or null if no value matched the token.
     */
    static <T extends HasToken> T fromContext(ParserRuleContext ctx, T... values) {
        for (T value : values) {
            if (ctx.getToken(value.tokenId(), 0) != null) {
                return value;
            }
        }
        return null;
    }
}

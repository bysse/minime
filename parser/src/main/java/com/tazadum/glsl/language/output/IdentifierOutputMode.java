package com.tazadum.glsl.language.output;

import com.tazadum.glsl.language.ast.Identifier;

import java.util.function.Function;

/**
 * The modes for how identifiers are rendered.
 */
public enum IdentifierOutputMode implements Function<Identifier, String> {
    Original,
    Replaced,
    None;

    public String apply(Identifier identifier) {
        switch (this) {
            case None:
                return "";
            case Replaced:
                return identifier.token();
            default:
            case Original:
                return identifier.original();
        }
    }
}

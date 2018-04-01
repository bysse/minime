package com.tazadum.glsl.simplification;

import com.tazadum.glsl.ast.Node;

/**
 * Created by Erik on 2018-03-30.
 */
public interface Matcher {
    boolean matches(Node node);

    MatchNodeStorage capture(MatchNodeStorage matchNodeStorage);
}

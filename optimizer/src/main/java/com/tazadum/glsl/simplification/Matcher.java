package com.tazadum.glsl.simplification;

import com.tazadum.glsl.language.ast.Node;

/**
 * Created by Erik on 2018-03-30.
 */
public interface Matcher {
    boolean matches(Node node);

    CaptureGroups getGroups();
}

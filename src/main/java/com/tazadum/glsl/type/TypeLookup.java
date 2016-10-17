package com.tazadum.glsl.type;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.language.GLSLType;

import java.util.Map;

/**
 * Created by Erik on 2016-10-16.
 */
public class TypeLookup {
    private Map<Node, GLSLType> map;

    public TypeLookup(Map<Node, GLSLType> map) {
        this.map = map;
    }
}

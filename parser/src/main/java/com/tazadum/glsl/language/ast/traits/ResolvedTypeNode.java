package com.tazadum.glsl.language.ast.traits;

import com.tazadum.glsl.language.type.FullySpecifiedType;

/**
 * Marker interface for tagging nodes that contains fully resolved types.
 */
public interface ResolvedTypeNode {
    FullySpecifiedType getFullySpecifiedType();
}

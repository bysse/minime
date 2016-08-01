package com.tazadum.glsl.parser.type;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.language.GLSLType;

public interface TypeRegistry {
    void declare(FullySpecifiedType fsType);

    void usage(GLSLType type, Node node);

    FullySpecifiedType resolve(String name);
}

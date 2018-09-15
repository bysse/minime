package com.tazadum.glsl.parser.type;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.language.GLSLType;
import com.tazadum.glsl.parser.GLSLContext;
import com.tazadum.glsl.parser.Usage;

public interface TypeRegistry {
    void declare(FullySpecifiedType fsType);

    void usage(GLSLContext context, GLSLType type, Node node);

    FullySpecifiedType resolve(String name);

    Usage<GLSLType> usagesOf(FullySpecifiedType fst);

    TypeRegistry remap(Node destinationRoot);
}

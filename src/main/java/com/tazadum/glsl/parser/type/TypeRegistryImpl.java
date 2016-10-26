package com.tazadum.glsl.parser.type;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.exception.TypeException;
import com.tazadum.glsl.language.GLSLType;
import com.tazadum.glsl.parser.GLSLContext;
import com.tazadum.glsl.parser.Usage;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TypeRegistryImpl implements TypeRegistry {
    private ConcurrentMap<String, FullySpecifiedType> typeMap;
    private ConcurrentMap<GLSLType, Usage<GLSLType>> usageMap;

    public TypeRegistryImpl() {
        this.typeMap = new ConcurrentHashMap<>();
        this.usageMap = new ConcurrentHashMap<>();
    }

    @Override
    public void declare(FullySpecifiedType fst) {
        typeMap.put(fst.getType().token(), fst);
    }

    @Override
    public void usage(GLSLContext context, GLSLType type, Node node) {
        usageMap.computeIfAbsent(type, Usage::new).add(node);
    }

    @Override
    public FullySpecifiedType resolve(String name) {
        FullySpecifiedType fst = typeMap.get(name);
        if (fst == null) {
            throw TypeException.unknownType(name);
        }
        return fst;
    }

    @Override
    public Usage<GLSLType> usagesOf(FullySpecifiedType fst) {
        return usageMap.get(fst.getType());
    }
}

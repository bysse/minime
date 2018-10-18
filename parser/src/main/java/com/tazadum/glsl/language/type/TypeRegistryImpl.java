package com.tazadum.glsl.language.type;

import com.tazadum.glsl.exception.Errors;
import com.tazadum.glsl.exception.TypeException;
import com.tazadum.glsl.language.ast.Identifier;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.context.GLSLContext;
import com.tazadum.glsl.parser.Usage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TypeRegistryImpl implements TypeRegistry {
    private ConcurrentMap<String, FullySpecifiedType> typeMap;
    private ConcurrentMap<GLSLType, Usage<GLSLType>> usageMap;

    public TypeRegistryImpl() {
        this.typeMap = new ConcurrentHashMap<>();
        this.usageMap = new ConcurrentHashMap<>();
    }

    private TypeRegistryImpl(ConcurrentMap<String, FullySpecifiedType> typeMapCopy, ConcurrentMap<GLSLType, Usage<GLSLType>> usageMapCopy) {
        this.typeMap = typeMapCopy;
        this.usageMap = usageMapCopy;
    }

    @Override
    public void declare(FullySpecifiedType fst) {
        if (fst.getType() instanceof StructType) {
            final Identifier identifier = ((StructType) fst.getType()).getIdentifier();
            if (identifier != null) {
                // only register the original type name
                typeMap.put(identifier.original(), fst);
            }
            // this is an anonymous type, don't register it
        } else {
            typeMap.put(fst.getType().token(), fst);
        }
    }

    @Override
    public void usage(GLSLContext context, GLSLType type, Node node) {
        usageMap.computeIfAbsent(type, Usage::new).add(node);
    }

    @Override
    public FullySpecifiedType resolve(String name) throws TypeException {
        FullySpecifiedType fst = typeMap.get(name);
        if (fst == null) {
            throw new TypeException(Errors.Type.UNKNOWN_TYPE(name));
        }
        return fst;
    }

    @Override
    public Usage<GLSLType> usagesOf(FullySpecifiedType fst) {
        return usageMap.get(fst.getType());
    }

    @Override
    public TypeRegistry remap(Node base) {
        final ConcurrentMap<String, FullySpecifiedType> typeMapCopy = new ConcurrentHashMap<>(typeMap);
        final ConcurrentMap<GLSLType, Usage<GLSLType>> usageMapCopy = new ConcurrentHashMap<>();

        for (Map.Entry<GLSLType, Usage<GLSLType>> entry : usageMap.entrySet()) {
            final GLSLType type = entry.getKey();
            final Usage<GLSLType> usage = entry.getValue();
            usageMapCopy.put(type, usage.remap(base));
        }

        return new TypeRegistryImpl(typeMapCopy, usageMapCopy);
    }
}

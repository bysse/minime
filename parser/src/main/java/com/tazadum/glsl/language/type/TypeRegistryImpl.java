package com.tazadum.glsl.language.type;

import com.tazadum.glsl.exception.TypeException;
import com.tazadum.glsl.language.ast.Identifier;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.context.GLSLContext;
import com.tazadum.glsl.parser.Usage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.tazadum.glsl.exception.Errors.Coarse.UNKNOWN_SYMBOL;

public class TypeRegistryImpl implements TypeRegistry {
    private final Logger logger = LoggerFactory.getLogger(TypeRegistryImpl.class);
    private ConcurrentMap<String, GLSLType> typeMap;
    private ConcurrentMap<GLSLType, Usage<GLSLType>> usageMap;

    public TypeRegistryImpl() {
        this.typeMap = new ConcurrentHashMap<>();
        this.usageMap = new ConcurrentHashMap<>();
    }

    private TypeRegistryImpl(ConcurrentMap<String, GLSLType> typeMapCopy, ConcurrentMap<GLSLType, Usage<GLSLType>> usageMapCopy) {
        this.typeMap = typeMapCopy;
        this.usageMap = usageMapCopy;
    }

    @Override
    public void declare(GLSLType type) {
        if (type instanceof StructType) {
            final Identifier identifier = ((StructType) type).getIdentifier();
            if (identifier != null) {
                // only register the original type name
                typeMap.put(identifier.original(), type);
            }
            // this is an anonymous type, don't register it
        } else {
            typeMap.put(type.token(), type);
        }
    }

    @Override
    public void undeclare(StructType structType) {
        final Identifier identifier = structType.getIdentifier();
        if (identifier != null) {
            GLSLType glslType = typeMap.remove(identifier.original());
            if (glslType != null) {
                logger.trace("  - Removing declaration of {}", identifier.original());
                usageMap.remove(glslType);

                // TODO: do something with the usage
            }
        }

    }

    @Override
    public void usage(GLSLContext context, GLSLType type, Node node) {
        usageMap.computeIfAbsent(type, Usage::new).add(node);
    }

    @Override
    public GLSLType resolve(String name) throws TypeException {
        GLSLType fst = typeMap.get(name);
        if (fst == null) {
            throw new TypeException(UNKNOWN_SYMBOL(name));
        }
        return fst;
    }

    @Override
    public Usage<GLSLType> usagesOf(GLSLType type) {
        Usage<GLSLType> usage = usageMap.get(type);
        if (usage != null) {
            return usage;
        }
        return new Usage<>(type);
    }

    @Override
    public TypeRegistry remap(Node base) {
        final ConcurrentMap<String, GLSLType> typeMapCopy = new ConcurrentHashMap<>(typeMap);
        final ConcurrentMap<GLSLType, Usage<GLSLType>> usageMapCopy = new ConcurrentHashMap<>();

        for (Map.Entry<GLSLType, Usage<GLSLType>> entry : usageMap.entrySet()) {
            final GLSLType type = entry.getKey();
            final Usage<GLSLType> usage = entry.getValue();
            usageMapCopy.put(type, usage.remap(base));
        }

        return new TypeRegistryImpl(typeMapCopy, usageMapCopy);
    }
}

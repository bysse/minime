package com.tazadum.glsl.language.type;

import com.tazadum.glsl.exception.NoSuchFieldException;
import com.tazadum.glsl.language.ast.Identifier;
import com.tazadum.glsl.language.ast.struct.StructDeclarationNode;
import com.tazadum.glsl.language.ast.variable.VariableDeclarationListNode;
import com.tazadum.glsl.language.ast.variable.VariableDeclarationNode;

import java.util.HashMap;
import java.util.Map;

import static com.tazadum.glsl.exception.Errors.Coarse.NO_SUCH_FIELD;

/**
 * Created by erikb on 2018-10-10.
 */
public class StructType implements GLSLType {
    private final Identifier identifier;
    private final Map<String, GLSLType> fieldMap;
    private final Map<String, Integer> indexMap;

    public StructType(StructDeclarationNode declarationNode) {
        identifier = declarationNode.getIdentifier();
        fieldMap = new HashMap<>();
        indexMap = new HashMap<>();

        int index = 0;
        for (int i = 0; i < declarationNode.getChildCount(); i++) {
            VariableDeclarationListNode variableList = declarationNode.getChildAs(i);
            VariableDeclarationNode first = variableList.getChildAs(0);

            StructDeclarationNode structDeclaration = first.getStructDeclaration();
            for (int j = 0; j < variableList.getChildCount(); j++) {
                VariableDeclarationNode fieldNode = variableList.getChildAs(j);
                String fieldName = fieldNode.getIdentifier().original();

                if (structDeclaration == null) {
                    // phew this is an ordinary type
                    fieldMap.put(fieldName, fieldNode.getFullySpecifiedType().getType());
                } else {
                    // a nested struct
                    fieldMap.put(fieldName, new StructType(structDeclaration));
                }
                indexMap.put(fieldName, index++);
            }
        }
    }

    public StructType(Identifier identifier, Map<String, GLSLType> fieldMap, Map<String, Integer> indexMap) {
        this.identifier = identifier;
        this.fieldMap = fieldMap;
        this.indexMap = indexMap;

        assert fieldMap.size() == indexMap.size() : "Field and index map are different sizes";
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    @Override
    public GLSLType fieldType(String fieldName) throws NoSuchFieldException {
        GLSLType glslType = fieldMap.get(fieldName);
        if (glslType == null) {
            String name = identifier == null ? "structure" : identifier.original();
            throw new NoSuchFieldException(NO_SUCH_FIELD(fieldName, name));
        }
        return glslType;
    }

    public int getFieldIndex(String fieldName) throws NoSuchFieldException {
        Integer index = indexMap.get(fieldName);
        if (index == null) {
            String name = identifier == null ? "structure" : identifier.original();
            throw new NoSuchFieldException(NO_SUCH_FIELD(fieldName, name));
        }
        return index;
    }

    @Override
    public boolean isAssignableBy(GLSLType type) {
        if (this == type) {
            return true;
        }

        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public GLSLType baseType() {
        return null;
    }

    @Override
    public String token() {
        if (identifier == null) {
            return null;
        }
        return identifier.token();
    }

    public String toString() {
        return "struct{" + fieldMap.size() + "}";
    }
}

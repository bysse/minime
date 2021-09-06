package com.tazadum.glsl.language.type;

import com.tazadum.glsl.exception.NoSuchFieldException;
import com.tazadum.glsl.language.ast.Identifier;
import com.tazadum.glsl.language.ast.struct.StructDeclarationNode;
import com.tazadum.glsl.language.ast.variable.VariableDeclarationListNode;
import com.tazadum.glsl.language.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.parser.TypeCombination;
import com.tazadum.glsl.parser.TypeComparator;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.tazadum.glsl.exception.Errors.Coarse.NO_SUCH_FIELD;

/**
 * Created by erikb on 2018-10-10.
 */
public class StructType implements GLSLType {
    private final Identifier identifier;
    private final Map<String, GLSLType> fieldMap;
    private final Map<Integer, GLSLType> fieldIndexMap;
    private final Map<String, Integer> indexMap;
    private GLSLType sameFieldType = null;

    public StructType(StructDeclarationNode declarationNode) {
        identifier = declarationNode.getIdentifier();
        fieldMap = new HashMap<>();
        indexMap = new HashMap<>();
        fieldIndexMap = new HashMap<>();

        int index = 0;
        for (int i = 0; i < declarationNode.getChildCount(); i++) {
            VariableDeclarationListNode variableList = declarationNode.getChildAs(i);
            VariableDeclarationNode first = variableList.getChildAs(0);

            StructDeclarationNode structDeclaration = first.getStructDeclaration();
            for (int j = 0; j < variableList.getChildCount(); j++) {
                VariableDeclarationNode fieldNode = variableList.getChildAs(j);
                String fieldName = fieldNode.getIdentifier().original();

                GLSLType type = fieldNode.getFullySpecifiedType().getType();
                if (structDeclaration != null) {
                    // a nested struct
                    type = new StructType(structDeclaration);
                }

                if (i == 0) {
                    sameFieldType = type;
                } else if (sameFieldType != null) {
                    sameFieldType = TypeCombination.compatibleTypeNoException(sameFieldType, type);
                }

                fieldMap.put(fieldName, type);
                fieldIndexMap.put(index, type);
                indexMap.put(fieldName, index++);
            }
        }
    }

    public StructType(Identifier identifier, Map<String, GLSLType> fieldMap, Map<String, Integer> indexMap) {
        this.identifier = identifier;
        this.fieldMap = fieldMap;
        this.indexMap = indexMap;

        assert fieldMap.size() == indexMap.size() : "Field and index map are different sizes";

        this.fieldIndexMap = new HashMap<>();
        boolean first = true;
        for (Map.Entry<String, Integer> entry : indexMap.entrySet()) {
            GLSLType type = fieldMap.get(entry.getKey());
            fieldIndexMap.put(entry.getValue(), type);

            if (type == null) {
                throw new IllegalStateException("Inconsistent structure data for field " + entry.getKey());
            }

            if (first) {
                sameFieldType = type;
                first = false;
            } else if (sameFieldType != null) {
                sameFieldType = TypeCombination.compatibleTypeNoException(sameFieldType, type);
            }
        }
    }

    public int components() {
        return fieldMap.size();
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

    public GLSLType fieldType(int index) throws NoSuchFieldException {
        GLSLType type = fieldIndexMap.get(index);
        if (type == null) {
            String name = identifier == null ? "structure" : identifier.original();
            throw new NoSuchFieldException(NO_SUCH_FIELD("#" + index, name));
        }
        return type;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StructType that = (StructType) o;

        if (!Objects.equals(identifier, that.identifier)) return false;
        return Objects.equals(fieldMap, that.fieldMap);
    }

    @Override
    public int hashCode() {
        int result = identifier != null ? identifier.hashCode() : 0;
        result = 31 * result + (fieldMap != null ? fieldMap.hashCode() : 0);
        return result;
    }

    @Override
    public boolean isAssignableBy(GLSLType type) {
        return TypeComparator.isAssignable(this, type);
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public GLSLType baseType() {
        return sameFieldType;
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

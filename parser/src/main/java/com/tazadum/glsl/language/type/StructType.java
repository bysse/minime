package com.tazadum.glsl.language.type;

import com.tazadum.glsl.exception.NoSuchFieldException;
import com.tazadum.glsl.language.ast.Identifier;
import com.tazadum.glsl.language.ast.struct.StructDeclarationNode;
import com.tazadum.glsl.language.ast.variable.VariableDeclarationListNode;
import com.tazadum.glsl.language.ast.variable.VariableDeclarationNode;

import java.util.HashMap;
import java.util.Map;

import static com.tazadum.glsl.exception.Errors.Type.NO_SUCH_FIELD;

/**
 * Created by erikb on 2018-10-10.
 */
public class StructType implements GLSLType {
    private final Identifier identifier;
    private final Map<String, GLSLType> fieldMap = new HashMap<>();
    private final Map<String, Integer> indexMap = new HashMap<>();

    public StructType(StructDeclarationNode declarationNode) {
        identifier = declarationNode.getIdentifier();

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

    public Identifier getIdentifier() {
        return identifier;
    }

    @Override
    public GLSLType fieldType(String fieldName) throws NoSuchFieldException {
        GLSLType glslType = fieldMap.get(fieldName);
        if (glslType == null) {
            String name = identifier == null ? "anonymous struct" : identifier.original();
            throw new NoSuchFieldException(NO_SUCH_FIELD(fieldName, name));
        }
        return glslType;
    }

    public int getFieldIndex(String fieldName) throws NoSuchFieldException {
        Integer index = indexMap.get(fieldName);
        if (index == null) {
            String name = identifier == null ? "anonymous struct" : identifier.original();
            throw new NoSuchFieldException(NO_SUCH_FIELD(fieldName, name));
        }
        return index;
    }

    @Override
    public boolean isAssignableBy(GLSLType type) {
        return false;
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
}

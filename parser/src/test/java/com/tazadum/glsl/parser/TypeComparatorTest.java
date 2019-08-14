package com.tazadum.glsl.parser;

import com.tazadum.glsl.exception.TypeException;
import com.tazadum.glsl.language.ast.Identifier;
import com.tazadum.glsl.language.ast.type.ArraySpecifier;
import com.tazadum.glsl.language.model.ArraySpecifiers;
import com.tazadum.glsl.language.type.ArrayType;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.language.type.PredefinedType;
import com.tazadum.glsl.language.type.StructType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.tazadum.glsl.language.type.PredefinedType.*;
import static com.tazadum.glsl.util.SourcePosition.TOP;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by erikb on 2018-10-20.
 */
class TypeComparatorTest {

    @Test
    @DisplayName("Test nested arrays")
    void test_1() throws TypeException {
        // float[][]
        GLSLType target = new ArrayType(FLOAT);
        ArraySpecifiers targetSpecifiers = new ArraySpecifiers();
        targetSpecifiers.addSpecifier(new ArraySpecifier(TOP, 2));

        // float[3][2]
        GLSLType source = new ArrayType(new ArrayType(FLOAT, 2), 3);
        ArraySpecifiers sourceSpecifiers = new ArraySpecifiers();

        GLSLType type = TypeComparator.checkAndTransfer(target, targetSpecifiers, source, sourceSpecifiers);
        GLSLType level1 = sourceSpecifiers.transform(type);

        // check first level
        assertNotNull(level1);
        assertTrue(level1.isArray() && level1 instanceof ArrayType);
        assertEquals(3, ((ArrayType) level1).getDimension());

        // check second level
        GLSLType level2 = level1.baseType();
        assertNotNull(level2);
        assertTrue(level2.isArray() && level2 instanceof ArrayType);
        assertEquals(2, ((ArrayType) level2).getDimension());

        // check the element type
        assertEquals(FLOAT, level2.baseType());
        assertTrue(level1.isAssignableBy(source));
    }

    @Test
    @DisplayName("Test structures")
    void test_2() {
        Map<String, GLSLType> fieldMap = new HashMap<>();
        Map<String, Integer> indexMap = new HashMap<>();

        fieldMap.put("a", PredefinedType.FLOAT);
        fieldMap.put("b", PredefinedType.INT);
        indexMap.put("a", 0);
        indexMap.put("b", 1);

        StructType targetType = new StructType(new Identifier("A"), fieldMap, indexMap);
        StructType targetType2 = new StructType(new Identifier("B"), fieldMap, indexMap);
        GLSLType sourceType = new ArrayType(FLOAT, 2);

        assertTrue(TypeComparator.isAssignable(targetType, sourceType));
        assertTrue(targetType.isAssignableBy(sourceType));
        assertTrue(targetType.isAssignableBy(targetType2));

        assertFalse(targetType.isAssignableBy(new ArrayType(FLOAT, 3)));
    }

    @Test
    @DisplayName("Test nested structures")
    void test_3() {

    }

    @Test
    @DisplayName("Test vector array init")
    void test_4() throws TypeException {
        ArraySpecifiers noSpecifiers = new ArraySpecifiers();

        GLSLType array2 = new ArrayType(FLOAT, 2);
        GLSLType array3 = new ArrayType(FLOAT, 3);
        GLSLType array4 = new ArrayType(FLOAT, 4);

        TypeComparator.checkAndTransfer(VEC3, null, array3, noSpecifiers);

        assertThrows(TypeException.class, () -> TypeComparator.checkAndTransfer(VEC3, null, array2, noSpecifiers));
        assertThrows(TypeException.class, () -> TypeComparator.checkAndTransfer(VEC3, null, array4, noSpecifiers));
        assertThrows(TypeException.class, () -> TypeComparator.checkAndTransfer(BVEC3, null, array3, noSpecifiers));
    }
}

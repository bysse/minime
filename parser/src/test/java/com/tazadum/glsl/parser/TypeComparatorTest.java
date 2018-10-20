package com.tazadum.glsl.parser;

import com.tazadum.glsl.exception.TypeException;
import com.tazadum.glsl.language.ast.type.ArraySpecifier;
import com.tazadum.glsl.language.model.ArraySpecifiers;
import com.tazadum.glsl.language.type.ArrayType;
import com.tazadum.glsl.language.type.GLSLType;
import org.junit.jupiter.api.Test;

import static com.tazadum.glsl.language.type.PredefinedType.FLOAT;
import static com.tazadum.glsl.util.SourcePosition.TOP;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by erikb on 2018-10-20.
 */
class TypeComparatorTest {

    @Test
    void test_1() throws TypeException {

        // float[][]
        GLSLType target = new ArrayType(FLOAT);
        ArraySpecifiers targetSpecifiers = new ArraySpecifiers();
        targetSpecifiers.addSpecifier(new ArraySpecifier(TOP, 2));

        // float[3][2]
        GLSLType source = new ArrayType(new ArrayType(FLOAT, 2), 3);
        ArraySpecifiers sourceSpecifiers = new ArraySpecifiers();

        GLSLType type = TypeComparator.checkAndTransfer(source, sourceSpecifiers, target, targetSpecifiers);
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

        // check comparator
        assertTrue(level1.isAssignableBy(source));
    }
}

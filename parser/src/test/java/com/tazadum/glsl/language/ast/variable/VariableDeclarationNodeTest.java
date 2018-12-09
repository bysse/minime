package com.tazadum.glsl.language.ast.variable;

import com.tazadum.glsl.language.type.FullySpecifiedType;
import com.tazadum.glsl.language.type.PredefinedType;
import org.junit.jupiter.api.Test;

import static com.tazadum.glsl.util.SourcePosition.TOP;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by erikb on 2018-11-04.
 */
class VariableDeclarationNodeTest {
    @Test
    void testEquality() {
        FullySpecifiedType type1 = new FullySpecifiedType(PredefinedType.VEC3);
        FullySpecifiedType type2 = new FullySpecifiedType(PredefinedType.VEC3);

        VariableDeclarationNode a = new VariableDeclarationNode(TOP, true, type1, "test", null, null, null);
        VariableDeclarationNode b = new VariableDeclarationNode(TOP, true, type2, "test", null, null, null);

        // not sure if this should work?
        assertEquals(a.hashCode(), b.hashCode());
        assertTrue(a.equals(b));
    }
}

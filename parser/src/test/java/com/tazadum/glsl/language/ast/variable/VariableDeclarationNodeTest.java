package com.tazadum.glsl.language.ast.variable;

import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.ast.util.NodeUtil;
import com.tazadum.glsl.language.type.FullySpecifiedType;
import com.tazadum.glsl.language.type.PredefinedType;
import org.junit.jupiter.api.Test;

import static com.tazadum.glsl.util.SourcePosition.TOP;
import static org.junit.jupiter.api.Assertions.*;

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

        // this should not work
        assertNotEquals(a.hashCode(), b.hashCode());
        assertFalse(a.equals(b));

        // but this should work
        assertTrue(CloneUtils.equal(a, b));
    }
}

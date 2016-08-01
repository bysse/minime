package com.tazadum.glsl.parser.listener;

import com.tazadum.glsl.language.*;
import com.tazadum.glsl.parser.TestUtils;
import com.tazadum.glsl.parser.type.FullySpecifiedType;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TypeListenerTest {
    @Test
    public void showTokens() {
        CommonTokenStream stream = TestUtils.tokenStream("uniform float");
        System.out.println(TestUtils.getTokens(stream));
    }

    @Test
    public void showRuleInvocations() {
        CommonTokenStream stream = TestUtils.tokenStream("uniform float");
        GLSLParser parser = TestUtils.parser(stream);
        TypeListener listener = new TypeListener(null);
        listener.walk(parser.fully_specified_type());
    }

    @Test
    public void testTypeDeclarations() {
        assertEquals(fst(null, null, BuiltInType.FLOAT), parse("float"));
        assertEquals(fst(TypeQualifier.UNIFORM, null, BuiltInType.FLOAT), parse("uniform float"));
        assertEquals(fst(TypeQualifier.UNIFORM, PrecisionQualifier.HIGH_PRECISION, BuiltInType.VEC3), parse("uniform highp vec3"));
    }

    private FullySpecifiedType fst(TypeQualifier qualifier, PrecisionQualifier precision, GLSLType type) {
        return new FullySpecifiedType(qualifier, precision, type);
    }

    private FullySpecifiedType parse(String code) {
        System.out.println("Parsing '" + code + "'");
        CommonTokenStream stream = TestUtils.tokenStream(code);
        GLSLParser parser = TestUtils.parser(stream);
        TypeListener listener = new TypeListener(null);

        listener.walk(parser.fully_specified_type());

        return listener.getResult();
    }
}

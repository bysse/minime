package com.tazadum.glsl.optimizer;

import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.language.context.GLSLContext;
import com.tazadum.glsl.language.type.FullySpecifiedType;
import com.tazadum.glsl.language.variable.VariableRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static com.tazadum.glsl.language.type.PredefinedType.FLOAT;
import static com.tazadum.glsl.language.type.PredefinedType.INT;
import static com.tazadum.glsl.util.SourcePosition.TOP;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Created by Erik on 2016-10-20.
 */
public class FunctionInlineTest extends BaseOptimizerTest {
    @Override
    protected OptimizerType[] getOptimizerTypes() {
        return new OptimizerType[]{OptimizerType.FunctionInline, OptimizerType.DeadCodeEliminationType};
    }

    @BeforeEach
    void setup() {
        testInit(3, false);
    }

    @ParameterizedTest(name = "case: {1}")
    @DisplayName("Optimizations that should work")
    @MethodSource("getPositiveCases")
    void testOptimizerPositive(String expected, String source) {
        GLSLContext context = parserContext.currentContext();
        VariableRegistry registry = parserContext.getVariableRegistry();
        registry.declareVariable(context, new VariableDeclarationNode(TOP, true, new FullySpecifiedType(FLOAT), "fv", null, null, null));
        registry.declareVariable(context, new VariableDeclarationNode(TOP, true, new FullySpecifiedType(INT), "iv", null, null, null));


        Node node = optimize(source);
        sourceEquals(expected, toString(node));
    }

    private static Arguments[] getPositiveCases() {
        return new Arguments[]{
            Arguments.of( // single line void function
                "void main(){iv+=1;}",
                "void f(int b){ iv+=b; } void main(){ f(1); }"),
            Arguments.of( // single line void function with parameter mutation
                "void main(){int _gen0=1;iv+=++_gen0;}",
                "void f(int b){ iv+=++b; } void main(){ f(1); }"),
            Arguments.of( // single line function
                "void main(){iv=2*3;}",
                "int f(int b){ return 2*b; } void main(){ iv=f(3); }"),
            Arguments.of( // single line function with parameter mutation
                "void main(){int _gen0=3;iv=2*_gen0++;}",
                "int f(int b){ return 2*b++; } void main(){ iv=f(3); }"),
            Arguments.of( // single line function with global mutation
                "int a=0;void main(){int b=a,c=(3+a++);iv=b+c;}",
                "int a=0; int f(int b){ return b+a++; } void main(){ int b=a,c=f(3); iv=b+c; }"),
            Arguments.of( // multi line void function
                "void main(){iv+=1;fv++;}",
                "void f(int b){ iv+=b;fv++; } void main(){ f(1); }"),
            Arguments.of( // multi line function
                "void main(){int _gen0=1;for(int _gen1=0;_gen1<2;_gen1++)_gen0*=1;iv=_gen0;}",
                "int f(int a,int b){ int c=1;for(int i=0;i<b;i++){c*=a;} return c; } void main(){ iv=f(1,2); }"),
            Arguments.of( // multi line void function with parameter mutation
                "void main(){int _gen0=1;iv+=++_gen0;fv++;}",
                "void f(int b){ iv+=++b;fv++; } void main(){ f(1); }"),
            Arguments.of( // multi line function that mutates it's parameter
                "void main(){int _gen0=1+1;_gen0+=1;iv=_gen0;}",
                "int f(int a){ a+=1; return a; } void main(){ iv=f(1+1); }"),
            Arguments.of(
                "void main(){float b=1;float _gen0=(2+b)+1;fv=_gen0;}",
                "float func(float a){ float c=a+1; return c; } void main(){ float b=1;fv=func(2+b); }"),
            Arguments.of( // declaration of dependent variable 'b' hinders inline
                "void main(){float b=1;float _gen0=b;float d=_gen0;fv=d;}",
                "float F(float a){ float c=a; return c; } void main(){ float b=1,d=F(b);fv=d; }"),
            Arguments.of(
                "void main(){fv=2;}",
                "float func(float a){ return a; } void main(){ fv=func(2); }"),
            Arguments.of(
                "void main(){fv=2*2;}",
                "float func(float a){ return 2*a; } void main(){ fv=func(2); }"),
            Arguments.of(
                "void main(){float b=1;fv=2*(2+b);}",
                "float func(float a){ return 2*a; } void main(){ float b=1;fv=func(2+b); }"),
            Arguments.of(
                "mat4 M;void main(){fv=M[1].x;}",
                "mat4 M;float col(int i){ return M[i]; } void main(){ fv=col(1).x; }"),
        };
    }

    private void sourceEquals(String expected, String actual) {
        if (expected.contains("?")) {
            if (expected.length() != actual.length()) {
                assertEquals(expected, actual);
            }

            for (int i = 0; i < expected.length(); i++) {
                char ch = expected.charAt(i);
                if (ch == '?') {
                    continue;
                }
                if (ch != actual.charAt(i)) {
                    fail("Expected " + expected + " but was " + actual);
                }
            }
        }
        assertEquals(expected, actual);
    }
}



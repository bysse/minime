package com.tazadum.glsl.parser.optimizer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Erik on 2016-10-20.
 */
public class DeclarationSqueezeTest extends BaseOptimizerTest {
    @Override
    protected OptimizerType[] getOptimizerTypes() {
        return new OptimizerType[] { OptimizerType.DeclarationSqueezeType };
    }


    @BeforeEach
    public void setup() {
        testInit();
    }

    @Test
    public void test_basic_1() {
        assertEquals("vec3 a,b,c;", optimize("vec3 a;vec3 b;vec3 c;"));
    }

    @Test
    public void test_basic_2() {
        assertEquals("float a,b=2,c;", optimize("float a;float b=2;float c;"));
    }

    @Test
    public void test_basic_3() {
        assertEquals("float a,b=2,c=b;", optimize("float a;float b=2;float c=b;"));
    }

    @Test
    public void test_squeeze_1() {
        assertEquals("float a,b,c=a;int x=a;", optimize("float a;float b;int x=a;float c=a;"));
    }

    @Test
    public void test_squeeze_2() {
        assertEquals("float a,b;int x=a++;float c=a;", optimize("float a;float b;int x=a++;float c=a;"));
    }

    @Test
    public void test_uniform_1() {
        assertEquals("uniform int a;void main(){int b,c=a;}", optimize("uniform int a;void main(){int b;int c=a;}"));
    }

    @Test
    public void test_context_1() {
        assertEquals("int a;void main(){int b,c=a;}", optimize("int a;void main(){int b;int c=a;}"));
    }

    @Test
    public void test_context_2() {
        assertEquals("void main(){vec2 a,c=2*a,d=vec2(c);}", optimize("void main(){vec2 a;vec2 c=2*a;vec2 d=vec2(c);}"));
    }

    @Test
    public void test_context_3() {
        assertEquals("uniform float x;void main(){vec2 a,c=vec2(x*a),d=vec2(c);}", optimize("uniform float x;void main(){vec2 a;vec2 c=vec2(x*a);vec2 d=vec2(c);}"));
    }

    //@Test
    public void test_context_4() {
        // x is modified in function call so c shouldn't be squeezed
        assertEquals("float x=0;float f(){return x++;}void main(){float a=0;vec2 b=vec2(f());float c=x;}", optimize("float x=0;float f(){return x++;}void main(){float a=0;vec2 b=vec2(f());float c=x;}"));
    }

    @Test
    public void test_context_5() {
        assertEquals("uniform float x;float a=2;void main(){float b=x+a;}", optimize("uniform float x;float a=2;void main(){float b=x+a;}"));
    }
}

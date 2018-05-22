package com.tazadum.glsl.parser.optimizer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Erik on 2016-10-20.
 */
public class ConstantPropagationTest extends BaseOptimizerTest {
    @Override
    protected OptimizerType[] getOptimizerTypes() {
        return new OptimizerType[] { OptimizerType.ConstantPropagationType };
    }

    @BeforeEach
    public void setup() {
        testInit();
    }

    @Test
    public void test_constants_1() {
        assertEquals("int f(){return 1;}", optimize("const int a=1;int f(){return a;}"));
    }

    @Test
    public void test_constants_2() {
        assertEquals("int f(){return 1;}", optimize("int a=1;int f(){return a;}"));
    }

    @Test
    public void test_constants_3() {
        assertEquals("int f(){return (1+2);}", optimize("int a=(1+2);int f(){return a;}"));
    }

    @Test
    public void test_constants_4() {
        assertEquals("vec3 f(){return vec3(0);}", optimize("vec3 a=vec3(0);vec3 f(){return a;}"));
    }

    @Test
    public void test_constants_5() {
        assertEquals("float f(){return 1+1;}", optimize("float a=1;float f(){return a+a;}"));
    }

    @Test
    public void test_constants_6() {
        assertEquals("int main(){int x=0;for(int i=0;i<5;i++)x+=i;return x;}", optimize("int main(){int x=0;for(int i=0;i<5;i++)x+=i;return x;}"));
    }

    @Test
    public void test_constants_7() {
        assertEquals("int main(){vec2 a=2*(vec2(1,1)-1).xy;return a.x+a.y;}", optimize("vec2 m=vec2(1,1)-1;int main(){vec2 a=2*m.xy;return a.x+a.y;}"));
    }

    @Test
    public void test_constants_8() {
        assertEquals("int a(){return 2;}int main(){int b=a(),x=0;for(int i=0;i<5;i++)x+=i*b;return x;}", optimize("int a(){return 2;}int main(){int b=a(),x=0;for(int i=0;i<5;i++){x+=i*b;}return x;}"));
    }

    @Test
    public void test_fail_1() {
        assertEquals("int a=1;int f(){a++;return a;}", optimize("int a=1;int f(){a++;return a;}"));
    }

    @Test
    public void test_fail_2() {
        assertEquals("int b=0;int g(){return b++;}int a=g();int f(){return a;}", optimize("int b=0;int g(){return b++;}int a=g();int f(){return a;}"));
    }
}

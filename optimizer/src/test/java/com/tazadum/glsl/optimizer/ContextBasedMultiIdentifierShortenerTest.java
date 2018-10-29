package com.tazadum.glsl.optimizer;

import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.output.IdentifierOutputMode;
import com.tazadum.glsl.optimizer.identifier.ContextBasedMultiIdentifierShortener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ContextBasedMultiIdentifierShortenerTest extends BaseTest {
    private ContextBasedMultiIdentifierShortener identifierShortener;

    static final String SHADER_1 = "  uniform vec3 iResolution;\n" +
            "    void main(void) {\n" +
            "        vec2 uv = gl_FragCoord.xy / iResolution.xy;\n" +
            "        vec3 color = vec3(fract(5.0*(uv.x + uv.y)));\n" +
            "        gl_FragColor = vec4(color, 10 * (1 + sin(6*iResolution.z)));\n" +
            "    }";

    static String SHADER_2 = "uniform vec3 iResolution;\n" +
            "uniform sampler2D image;\n" +
        "float WIDTH = iResolution.x;\n" +
        "float HEIGHT = iResolution.y;\n" +
            "const float GA = 2.399; \n" +
            "const mat2 rot = mat2(cos(GA),sin(GA),-sin(GA),cos(GA));\n" +
            "vec3 dof(sampler2D tex,vec2 uv,float rad) {\n" +
            "    vec3 acc=vec3(0);\n" +
            "    vec2 pixel=vec2(.002*HEIGHT/WIDTH,.002),angle=vec2(0,rad);\n" +
            "    for (int j=0;j<80;j++) {  \n" +
            "        rad += 1./rad;\n" +
            "        angle*=rot;\n" +
            "        acc+=texture(tex,uv+pixel*(rad-1.)*angle).xyz;\n" +
            "    }\n" +
            "    return acc/80.;\n" +
            "}\n" +
            "void main(void) {\n" +
            "    vec2 uv=gl_FragCoord.xy/vec2(WIDTH, HEIGHT);\n" +
            "    gl_FragColor=vec4(dof(image,uv,texture(image,uv).w),1.);\n" +
            "}";

    @BeforeEach
    void setup() {
        testInit();

        outputConfig = outputConfig.edit().identifierMode(IdentifierOutputMode.Replaced).build();
        identifierShortener = new ContextBasedMultiIdentifierShortener(true, outputConfig);
    }

    @Test
    void test_basic_1() {
        Node node1 = compile(parserContext, "uniform int x;void main(){float y=tan(0.0)*tan(0.0);}");
        Node node2 = compile(parserContext, "void main(){gl_FragColor=vec4(1.0);}");

        identifierShortener.register(parserContext, node1);
        identifierShortener.register(parserContext, node2);
        identifierShortener.apply();

        assertEquals("uniform int n;void main(){float i=tan(0)*tan(0);}", toString(node1));
        assertEquals("void main(){gl_FragColor=vec4(1);}", toString(node2));

        // test that permutation ends
        int escape = 10;
        while (identifierShortener.permutateIdentifiers() && escape > 0) {
            escape--;
        }
        assertTrue(escape > 0);
    }

    @Test
    void test_basic_2() {
        Node node1 = compile(parserContext, "uniform vec2 X; void main(){ for (int Y=0;Y<10;Y++) { X.x += 1; }}");
        Node node2 = compile(parserContext, "uniform vec2 X; void main() { gl_FragColor = vec4(X.xxy, 0); }");

        identifierShortener.register(parserContext, node1);
        identifierShortener.register(parserContext, node2);
        identifierShortener.apply();

        assertEquals("uniform vec2 v;void main(){for(int i=0;i<10;i++)v.x+=1;}", toString(node1));
        assertEquals("uniform vec2 c;void main(){gl_FragColor=vec4(v.xxy,0);}", toString(node2));
    }

    @Test
    void test_basic_3() {
        Node node = compile(parserContext, SHADER_1);
        identifierShortener.register(parserContext, node);
        identifierShortener.apply();

        assertEquals(
            "uniform vec3 v;void main(){vec2 c=gl_FragCoord.xy/v.xy;vec3 e=vec3(fract(5*(c.x+c.y)));gl_FragColor=vec4(e,10*(1+sin(6*v.z)));}",
            toString(node)
        );
    }

    @Test
    public void test_basic_4() {
        Node node1 = compile(parserContext, SHADER_1);
        Node node2 = compile(parserContext, SHADER_2);

        identifierShortener.register(parserContext, node1);
        identifierShortener.register(parserContext, node2);

        identifierShortener.apply();

        do {
            System.out.println(toString(node1));
            System.out.println(toString(node2));
            System.out.println("");

        } while(identifierShortener.permutateIdentifiers());
    }

    @Test
    public void test_basic_5() {
        Node node1 = compile(parserContext, "uniform vec2 a; float x=a.x; float y=a.y; vec2 f(float b){return b*a*x/y;}void main(){ gl_FragColor.xy = f(2);}");
        identifierShortener.register(parserContext, node1);

        do {
            identifierShortener.apply();

            System.out.println(toString(node1));
            System.out.println("");

        } while(identifierShortener.permutateIdentifiers());
    }

    @Test
    public void test_for_loops() {
        Node node1 = compile(parserContext,
                "" +
                        "void main(){" +
                        " vec4 color=vec4(1);" +
                        " for(int i=0;i<5;i++) {" +
                        "  color.x += 0.1*i;" +
                        " }" +
                        " for(int i=0;i<5;i++) {" +
                        "  color.y += 0.1*i;" +
                        " }" +
                        " gl_FragColor = color;" +
                        "}"
        );

        identifierShortener.register(parserContext, node1);

        do {
            identifierShortener.apply();

            System.out.println(toString(node1));
            System.out.println("");

        } while(identifierShortener.permutateIdentifiers());
    }
}

package com.tazadum.glsl.stage;

import com.tazadum.glsl.cli.OptimizerReport;
import com.tazadum.glsl.cli.options.OptimizerOptions;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.language.output.IdentifierOutputMode;
import com.tazadum.glsl.language.type.FullySpecifiedType;
import com.tazadum.glsl.language.type.PredefinedType;
import com.tazadum.glsl.optimizer.BaseTest;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.util.Pair;
import com.tazadum.glsl.util.SourcePosition;
import com.tazadum.glsl.util.SourcePositionId;
import com.tazadum.glsl.util.SourcePositionMapper;
import com.tazadum.slf4j.TLogConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.event.Level;

import static com.tazadum.glsl.util.SourcePosition.TOP;

/**
 * Created by erikb on 2018-10-31.
 */
class OptimizerStageTest extends BaseTest {
    private OptimizerStage stage;

    @BeforeEach
    void setUp() {
        TLogConfiguration.get().useGlobalConfiguration();
        TLogConfiguration.get().getConfig().setLogLevel(Level.TRACE);

        testInit(true, false);

        OptimizerOptions options = new OptimizerOptions();
        OptimizerReport report = new OptimizerReport();
        outputConfig = outputConfig.edit()
                .indentation(2)
                .renderNewLines(true)
                .identifierMode(IdentifierOutputMode.Replaced)
                .build();

        parserContext.getVariableRegistry().declareVariable(
                parserContext.globalContext(),
                new VariableDeclarationNode(
                        TOP, true, new FullySpecifiedType(PredefinedType.VEC4), "gl_FragColor", null, null, null
                )
        );

        stage = new OptimizerStage(options, outputConfig, report);
    }

    @Test
    void test() {
        String source = "uniform float time;\n" +
                "float small(vec3 v, float a) { vec3 q=a*v-vec3(1,2,3); return q.x+q.y+q.z; }\n" +
                "float tiny(vec3 v) { return 2.*v.x; }\n" +
                "vec3 medium(in vec3 p) {\n" +
                "    float A = 1.0 - p.y;\n" +
                "    vec3 B = p - vec3(A,1,0)*time;\n" +
                "    float C = 1. * tiny(B);\n" +
                "    C += tiny(B);\n" +
                "    C += tiny(B);\n" +
                "    C += small(A*B, C);\n" +
                "    vec4 D = vec4(B, C);\n" +
                "    return D.xyz;\n" +
                "}\n" +
                "void main() {\n" +
                "    gl_FragColor = vec4(medium(vec3(1,0,0)) + medium(vec3(0,1,0)), 0.5);\n" +
                "}";

        Node node = compile(parserContext, source);
        SourcePositionMapper mapper = new SourcePositionMapper();
        mapper.remap(SourcePosition.TOP, SourcePositionId.DEFAULT);

        StageData<Pair<Node, ParserContext>> data = stage.process(StageData.from(Pair.create(node, parserContext), mapper));
        Node optimizedNode = data.getData().getFirst();

        System.out.println();
        System.out.println(toString(optimizedNode));
    }

    @Test
    void testSnippet() {
        String source = "mat2 rot(float angle) {\n" +
                "  float cos_a=cos(angle);\n" +
                "  float sin_a=sin(angle);\n" +
                "  return mat2(cos_a,sin_a,-sin_a,cos_a);\n" +
                "}\n" +
                "void main() {gl_FragColor=vec4(0,0,vec2(1,2)*rot(1));}" +
                "";

        Node node = compile(parserContext, source);
        SourcePositionMapper mapper = new SourcePositionMapper();
        mapper.remap(SourcePosition.TOP, SourcePositionId.DEFAULT);

        StageData<Pair<Node, ParserContext>> data = stage.process(StageData.from(Pair.create(node, parserContext), mapper));
        Node optimizedNode = data.getData().getFirst();

        System.out.println();
        System.out.println(toString(optimizedNode));
    }

    @Test
    void testSnippet2() {
        String source = "vec3 tweak = vec3(10,9,0);\n" +
                "vec3 wavy = vec3(1,2,0.5);\n" +
                "vec4 x_landscape = vec4(tweak.x, tweak.y, tweak.z, wavy.z);\n" +
                "mat2 rot(float angle) {\n" +
                "  float cos_a=cos(angle);\n" +
                "  float sin_a=sin(angle);\n" +
                "  return mat2(cos_a,sin_a,-sin_a,cos_a);\n" +
                "}\n" +
                "void main() {\n" +
                "for(int i=0;i<5;i++){\n" +
                "  x_landscape.xz*=rot(float(i) + 1.21);" +
                "}\n" +
                "gl_FragColor=x_landscape;}" +
                "";

        Node node = compile(parserContext, source);
        SourcePositionMapper mapper = new SourcePositionMapper();
        mapper.remap(SourcePosition.TOP, SourcePositionId.DEFAULT);

        StageData<Pair<Node, ParserContext>> data = stage.process(StageData.from(Pair.create(node, parserContext), mapper));
        Node optimizedNode = data.getData().getFirst();

        System.out.println();
        System.out.println(toString(optimizedNode));
    }

    @Test
    @DisplayName("vec array construction")
    void testSnippet4() {
        String source = "vec4 vecs[3]=vec4[](vec4(0),vec4(1),vec4(2));\n" +
                "void main() {\n" +
                "gl_FragColor=vecs[2];}" +
                "";

        Node node = compile(parserContext, source);
        SourcePositionMapper mapper = new SourcePositionMapper();
        mapper.remap(SourcePosition.TOP, SourcePositionId.DEFAULT);

        StageData<Pair<Node, ParserContext>> data = stage.process(StageData.from(Pair.create(node, parserContext), mapper));
        Node optimizedNode = data.getData().getFirst();

        System.out.println();
        System.out.println(toString(optimizedNode));
    }

    @Test
    void testDuplication() {
        String source = "float func(float a) { if (a<0) a=2; return 2*a;}\n" +
                "void main() {\n" +
                "float x = func(1);\n" +
                "vec2 a=vec2(x);\n" +
                "vec2 b=vec2(x);\n" +
                "gl_FragColor=vec4(2*a, 2*b);\n" +
                "}";

        Node node = compile(parserContext, source);
        SourcePositionMapper mapper = new SourcePositionMapper();
        mapper.remap(SourcePosition.TOP, SourcePositionId.DEFAULT);

        StageData<Pair<Node, ParserContext>> data = stage.process(StageData.from(Pair.create(node, parserContext), mapper));
        Node optimizedNode = data.getData().getFirst();

        System.out.println();
        System.out.println(toString(optimizedNode));
    }
}

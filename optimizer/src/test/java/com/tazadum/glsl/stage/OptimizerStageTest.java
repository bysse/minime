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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.tazadum.glsl.util.SourcePosition.TOP;

/**
 * Created by erikb on 2018-10-31.
 */
class OptimizerStageTest extends BaseTest {
    private OptimizerStage stage;

    @BeforeEach
    void setUp() {
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
        String source = "uniform float iTime;void dead(){}\n" +
            "float noise(vec3 w){return w.x+w.y+w.z;}\n" +
            "float FUN(float a){ int b=1;return b+2*a;}\n" +
            "vec4 map( in vec3 p ) {\n" +
            " float d = 0.2 - p.y;\n" +
            " vec3 q = p - vec3(1.0,0.1,0.0)*iTime;\n" +
            " float f;\n" +
            " f  = 0.5000*noise( q ); q = q*2.02;\n" +
            " f += 0.2500*noise( q ); q = q*2.03;\n" +
            " f += 0.1250*noise( q ); q = q*2.01;\n" +
            " f += 0.0625*noise( q );\n" +
            " d += 3.0 * f+FUN(1.0);\n" +
            " d = clamp( d, 0.0, 1.0 );\n" +
            " vec4 res = vec4( d );\n" +
            " res.xyz = mix( 1.15*vec3(1.0,0.95,0.8), vec3(0.7,0.7,0.7), res.x );\n" +
            " return res;\n" +
            "}\n" +
            "void main() { gl_FragColor=map(vec3(1.0));}";

        Node node = compile(parserContext, source);
        SourcePositionMapper mapper = new SourcePositionMapper();
        mapper.remap(SourcePosition.TOP, SourcePositionId.DEFAULT);

        StageData<Pair<Node, ParserContext>> data = stage.process(StageData.from(Pair.create(node, parserContext), mapper));
        Node optimizedNode = data.getData().getFirst();

        System.out.println("");
        System.out.println(toString(optimizedNode));
    }
}

package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.GLSLOptimizerContext;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.language.GLSLParser;
import com.tazadum.glsl.output.IdentifierOutput;
import com.tazadum.glsl.output.Output;
import com.tazadum.glsl.output.OutputConfig;
import com.tazadum.glsl.output.OutputSizeDecider;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.TestUtils;
import com.tazadum.glsl.parser.visitor.ContextVisitor;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Created by Erik on 2016-10-20.
 */
public class MultiOptimizerTest {
    final private OutputSizeDecider decider = new OutputSizeDecider();

    private GLSLOptimizerContext optimizerContext;
    private Output output;
    private OutputConfig outputConfig;

    @BeforeEach
    public void setup() {
        optimizerContext = TestUtils.optimizerContext("test");
        output = new Output();
        outputConfig = new OutputConfig();

        decider.getConfig().setMaxDecimals(3);
    }

    @Test
    public void testDofShader() throws Exception {
        String result = optimize(
                "uniform vec3 iResolution;\n" +
                "uniform sampler2D image;\n" +
                "float WIDTH = iResolution.x;\n" +
                "float HEIGHT = iResolution.y;\n" +
                "const float GA = 2.399; \n" +
                "const mat2 rot = mat2(cos(GA),sin(GA),-sin(GA),cos(GA));" +
                "void main(){\n" +
                "    vec2 uv=gl_FragCoord.xy/vec2(WIDTH, HEIGHT);\n" +
                "    float rad=texture(image,uv).w;\n" +
                "    vec4 acc=vec4(0);\n" +
                "    vec2 pixel=vec2(.002*HEIGHT/WIDTH,.002),angle=vec2(0,rad);\n" +
                "    for (int j=0;j<80;j++) {  \n" +
                "        rad+=1/rad;\n" +
                "        angle*=rot;\n" +
                "        acc+=texture(image,uv+pixel*(rad-1.)*angle);\n" +
                "    }\n" +
                "    gl_FragColor=acc/80.;\n" +
                "}\n"
        );

        System.out.println(result);

        int usage = result.indexOf("angle=vec2(0,rad)");
        int declaration = result.indexOf("rad=texture(image,uv).w");
        assertTrue(declaration < usage);
    }

    @Test
    public void testFolding() throws Exception {
        String result = optimize(
                "uniform vec3 uniformA;\n" +
                "float uniformProxy = uniformA.z;\n" +
                "float remove = 10/uniformProxy;\n" +
                "void main(){\n" +
                "    float deadcode=uniformProxy;\n" +
                "    gl_FragColor=vec3(remove);\n" +
                "}\n"
        );

        System.out.println(result);
    }

    @Test
    public void testSqueeze() throws Exception {
        String result = optimize(
                        "void main(){\n" +
                        "    vec2 modified=vec2(1,1);\n" +
                        "    modified.x = 2;\n" +
                        "    vec2 color=modified.yx;" +
                        "    gl_FragColor=vec4(color, color);\n" +
                        "}\n"
        );

        System.out.println(result);
    }

    @Test
    public void testRenaming() throws Exception {
        outputConfig.setIdentifiers(IdentifierOutput.Replaced);

        String result = optimize(
                "uniform sampler2D fontTexture;\n" +
                        "float character(vec3 p, int i) {\n" +
                        "    float d = 1e5;\n" +
                        "    for (int y=0;y<5;y++) {\n" +
                        "        for (int x=0;x<5;x++) {\n" +
                        "            float a = 0.05 * texture(fontTexture, .5+vec2(x, i)).x;\n" +
                        "            d = min(d, length(max(abs(p - 0.10 * vec3(x,y,0))-a,0.0)));\n" +
                        "        }\n" +
                        "        i++;\n" +
                        "    }\n" +
                        "    return d;\n" +
                        "}" +
                        "void main(){" +
                        "gl_FragColor=vec3(character(gl_FragCoord.xyz, 4));" +
                        "}"
        );

        System.out.println(result);
    }

    private String optimize(String source) {
        try {
            final CommonTokenStream stream = TestUtils.tokenStream(source);
            final GLSLParser parser = TestUtils.parser(stream);
            final ParserContext parserContext = optimizerContext.parserContext();

            final ContextVisitor visitor = new ContextVisitor(parserContext);
            Node node = parser.translation_unit().accept(visitor);
            optimizerContext.typeChecker().check(parserContext, node);

            Node optimized = optimize(node);
            ContextBasedMultiIdentifierShortener shortener = new ContextBasedMultiIdentifierShortener(true);

            OutputConfig config = new OutputConfig();
            config.setNewlines(false);
            config.setIndentation(0);
            config.setIdentifiers(IdentifierOutput.None);

            shortener.register(parserContext, optimized, config);
            shortener.apply();

            return output.render(optimized, outputConfig);
        } catch (Exception e) {
            throw e;
        }
    }

    private Node optimize(Node shaderNode) {

        OptimizerPipeline pipeline = new SingleShaderOptimizerPipeline(outputConfig, OptimizerType.values());
        return pipeline.optimize(optimizerContext, shaderNode, true);
    }

    private void output(String format, Object... args) {
        System.out.format(format, args);
    }
}
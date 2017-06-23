package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.language.GLSLParser;
import com.tazadum.glsl.output.Output;
import com.tazadum.glsl.output.OutputConfig;
import com.tazadum.glsl.output.OutputSizeDecider;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.TestUtils;
import com.tazadum.glsl.parser.type.TypeChecker;
import com.tazadum.glsl.parser.visitor.ContextVisitor;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by Erik on 2016-10-20.
 */
public class MultiOptimizerTest {
    final private OutputSizeDecider decider = new OutputSizeDecider();

    private ParserContext parserContext;
    private Output output;
    private TypeChecker typeChecker;
    private OutputConfig outputConfig;

    @Before
    public void setup() {
        parserContext = TestUtils.parserContext();
        output = new Output();
        outputConfig = new OutputConfig();
        typeChecker = new TypeChecker();

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

    private String optimize(String source) {
        try {
            final CommonTokenStream stream = TestUtils.tokenStream(source);
            final GLSLParser parser = TestUtils.parser(stream);
            final ContextVisitor visitor = new ContextVisitor(parserContext);
            Node node = parser.translation_unit().accept(visitor);
            typeChecker.check(parserContext, node);

            return output.render(optimize(node), outputConfig);
        } catch (Exception e) {
            throw e;
        }
    }

    private Node optimize(Node shaderNode) {
        final OutputSizeDecider decider = new OutputSizeDecider();

        // instantiate all the optimizers
        final DeadCodeElimination deadCodeElimination = new DeadCodeElimination();
        final ConstantFolding constantFolding = new ConstantFolding();
        final ConstantPropagation constantPropagation = new ConstantPropagation();
        final DeclarationSqueeze declarationSqueeze = new DeclarationSqueeze();

        Node node = shaderNode;

        int changes, iteration = 0;
        do {
            final int size = output.render(node, outputConfig).length();
            output("Iteration #%d: %d bytes\n", iteration++, size);

            // apply dead code elimination
            final Optimizer.OptimizerResult deadCodeResult = deadCodeElimination.run(parserContext, decider, node);
            changes = deadCodeResult.getChanges();
            node = deadCodeResult.getNode();
            if (changes > 0) {
                output("  - %d dead code eliminations\n", changes);
            }

            // apply constant folding
            final Optimizer.OptimizerResult foldResult = constantFolding.run(parserContext, decider, node);
            changes = foldResult.getChanges();
            node = foldResult.getNode();
            if (changes > 0) {
                output("  - %d constant folding replacements\n", changes);
            }

            // apply constant propagation
            final Optimizer.OptimizerResult propagationResult = constantPropagation.run(parserContext, decider, node);
            changes = propagationResult.getChanges();
            node = propagationResult.getNode();
            if (changes > 0) {
                output("  - %d constant propagation\n", changes);
            }

            // apply declaration squeeze
            final Optimizer.OptimizerResult squeezeResult = declarationSqueeze.run(parserContext, decider, node);
            changes = squeezeResult.getChanges();
            node = squeezeResult.getNode();
            if (changes > 0) {
                output("  - %d declaration squeezes\n", changes);
            }
        } while (changes > 0);
        return node;
    }

    private void output(String format, Object... args) {
        System.out.format(format, args);
    }
}
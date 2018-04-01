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
import org.antlr.v4.runtime.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Erik on 2016-10-20.
 */
public class DeadCodeEliminationTest {
    final private OutputSizeDecider decider = new OutputSizeDecider();

    private ParserContext parserContext;
    private Output output;
    private DeadCodeElimination deadCodeElimination;
    private TypeChecker typeChecker;
    private OutputConfig config;

    @BeforeEach
    public void setup() {
        parserContext = TestUtils.parserContext();
        output = new Output();
        deadCodeElimination = new DeadCodeElimination();
        typeChecker = new TypeChecker();
        config = new OutputConfig();
        config.setNewlines(false);
        config.setIndentation(0);

        decider.getConfig().setMaxDecimals(3);
    }

    @Test
    public void test_basic_preservation() {
        assertEquals("void main(){}", optimize("void main(){}"));
        assertEquals("void mainImage(out vec4 fragColor,in vec2 fragCoord){}", optimize("void mainImage(out vec4 fragColor,in vec2 fragCoord){}"));
    }

    @Test
    public void test_basic_elimination() {
        assertEquals("void main(){}", optimize("void main(){int a=0;}"));
    }

    @Test
    public void test_advanced_elimination() {
        assertEquals("void main(){}", optimize("int a=0;void f(){a=1;}void main(){}"));
    }

    private String optimize(String source) {
        try {
            final CommonTokenStream stream = TestUtils.tokenStream(source);
            final GLSLParser parser = TestUtils.parser(stream);
            final ContextVisitor visitor = new ContextVisitor(parserContext);
            Node node = parser.translation_unit().accept(visitor);
            typeChecker.check(parserContext, node);

            //node.accept(new IdVisitor());

            Optimizer.OptimizerResult result = deadCodeElimination.run(parserContext, decider, node);
            return output.render(result.getNode(), config).trim();
        } catch (Exception e) {
            for (Token token : TestUtils.getTokens(TestUtils.tokenStream(source))) {
                System.out.println(token);
            }
            throw e;
        }
    }
}

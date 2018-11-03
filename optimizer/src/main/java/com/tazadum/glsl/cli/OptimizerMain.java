package com.tazadum.glsl.cli;

import com.tazadum.glsl.cli.options.CompilerOptions;
import com.tazadum.glsl.cli.options.OptimizerOptions;
import com.tazadum.glsl.cli.options.OutputFormat;
import com.tazadum.glsl.cli.options.PreprocessorOptions;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.output.IdentifierOutputMode;
import com.tazadum.glsl.language.output.OutputConfig;
import com.tazadum.glsl.language.output.OutputConfigBuilder;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.ShaderType;
import com.tazadum.glsl.stage.*;
import com.tazadum.glsl.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.Set;
import java.util.TreeSet;

import static com.tazadum.glsl.cli.CommandLineBase.*;

/**
 * Created by erikb on 2018-10-24.
 */
public class OptimizerMain {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(OptimizerMain.class);

        PreprocessorOptions preprocessorOption = new PreprocessorOptions();
        CompilerOptions compilerOption = new CompilerOptions(3, true);
        OptimizerOptions optimizerOption = new OptimizerOptions();

        CommandLineBase cli = new CommandLineBase(
            OptimizerMain.class.getName(),
            "GLSL Minifier.",
            preprocessorOption,
            optimizerOption,
            compilerOption
        );

        InputOutput inputOutput = cli.process(args);
        if (inputOutput == null) {
            cli.showHelp(false);
            System.exit(RET_SYNTAX);
            return;
        }

        try {
            // global setup
            final boolean small = optimizerOption.isOptimizeSmall();
            final Set<String> blacklistedKeywords = new TreeSet<>();

            if (small) {
                // add ignored keywords for the small profile
                blacklistedKeywords.add("const");
                blacklistedKeywords.add("in");
            }

            // setup the preprocessor
            PreprocessorStage preprocess = new PreprocessorStage(preprocessorOption.getGLSLVersion());
            for (Pair<String, String> pair : preprocessorOption.getObjectLike()) {
                preprocess.define(pair.getFirst(), pair.getSecond());
            }

            Stage<String, String> postPreprocess = new NoOpStage();
            if (preprocessorOption.outputIntermediateResult()) {
                // if intermediate results are requested
                final Path outputPath = preprocessorOption.generateOutput(inputOutput.getInput());
                postPreprocess = new FileWriterStage(outputPath);
            }

            // setup the compiler stage
            CompilerStage compile = new CompilerStage(compilerOption.getShaderType(), compilerOption.getProfile());
            blacklistedKeywords.addAll(compilerOption.getKeywords());

            OutputConfig config = new OutputConfigBuilder()
                .identifierMode(IdentifierOutputMode.Replaced)
                .indentation(get(compilerOption.getIndentation(), small, 0))
                .renderNewLines(get(compilerOption.isNewLines(), small, false))
                .blacklistKeyword(blacklistedKeywords)
                .showOriginalIdentifiers(true)
                .build();

            // setup the optimizer stage
            final OptimizerReport report = new OptimizerReport();
            final SizeStage startSize = new SizeStage(report, OptimizerReport.START);
            final SizeStage finalSize = new SizeStage(report, OptimizerReport.END);

            OptimizerStage optimizer = new OptimizerStage(optimizerOption, config, report);

            // setup the rendering stage
            Stage<Pair<Node, ParserContext>, String> renderStage;

            if (compilerOption.getOutputFormat() == OutputFormat.C_HEADER) {
                final String shaderId = compilerOption.getShaderId(inputOutput.getInput());
                final String shaderHeader = generateShaderHeader(compilerOption.getShaderType());
                renderStage = new HeaderRenderStage(shaderId, shaderHeader, config);
            } else {
                renderStage = new RenderStage(config, compilerOption.getOutputFormat());
            }

            // setup the output
            FileWriterStage writerStage = new FileWriterStage(inputOutput.getOutput());

            StagePipeline<Path, String> pipeline = StagePipeline
                .create(preprocess)
                .chain(startSize)
                .chain(postPreprocess)
                .chain(compile)
                .chain(optimizer)
                .chain(renderStage)
                .chain(finalSize)
                .chain(writerStage)
                .build();

            report.mark();
            pipeline.process(new PathStageData(inputOutput.getInput()));
            report.display();

            System.exit(RET_OK);
        } catch (StageException e) {
            logger.error(e.getMessage(), e);
            System.exit(RET_EXCEPTION);
        }
    }

    private static String generateShaderHeader(ShaderType shaderType) {
        if (shaderType != ShaderType.SHADER_TOY) {
            return "";
        }

        // TODO: implement shader toy support
        throw new UnsupportedOperationException("Not implemented");
    }

    private static int get(int value, boolean control, int override) {
        return control ? override : value;
    }

    private static boolean get(boolean value, boolean control, boolean override) {
        return control ? override : value;
    }

}

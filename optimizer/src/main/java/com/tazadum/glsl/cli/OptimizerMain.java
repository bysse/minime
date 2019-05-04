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
import com.tazadum.glsl.stage.*;
import com.tazadum.glsl.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static com.tazadum.glsl.cli.CommandLineBase.*;

/**
 * Created by erikb on 2018-10-24.
 */
public class OptimizerMain {
    private final Logger logger = LoggerFactory.getLogger(OptimizerMain.class);
    public static boolean noExit = false;

    private final PreprocessorOptions preprocessorOption;
    private final CompilerOptions compilerOption;
    private final OptimizerOptions optimizerOption;

    private final Set<String> blacklistedKeywords = new TreeSet<>();
    private OutputConfig outputConfig;

    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(OptimizerMain.class);

        PreprocessorOptions preprocessorOption = new PreprocessorOptions();
        CompilerOptions compilerOption = new CompilerOptions(3, true);
        OptimizerOptions optimizerOption = new OptimizerOptions();

        CommandLineBase cli = new CommandLineBase(
            OptimizerMain.class.getName(),
            "GLSL Minifier.",
            true,
            preprocessorOption,
            optimizerOption,
            compilerOption
        );

        if (cli.process(args) == null) {
            cli.showHelp(false);
            if (!noExit) {
                System.exit(RET_SYNTAX);
            }
            return;
        }

        try {
            OptimizerMain main = new OptimizerMain(preprocessorOption, compilerOption, optimizerOption);
            main.process(cli.getInputOutputs(), cli.isSingleOutput());

            if (!noExit) {
                System.exit(RET_OK);
            }
        } catch (StageException e) {
            logger.error(e.getMessage(), e);
            if (!noExit) {
                System.exit(RET_EXCEPTION);
            }
        }
    }

    private OptimizerMain(PreprocessorOptions preprocessorOption, CompilerOptions compilerOption, OptimizerOptions optimizerOption) {
        this.preprocessorOption = preprocessorOption;
        this.compilerOption = compilerOption;
        this.optimizerOption = optimizerOption;
    }

    private void process(List<InputOutput> inputOutputs, boolean singleOutput) {
        final boolean small = optimizerOption.isOptimizeSmall();
        final boolean header = compilerOption.getOutputFormat() == OutputFormat.C_HEADER;

        blacklistedKeywords.addAll(compilerOption.getKeywords());

        if (small) {
            // add ignored keywords for the small profile
            blacklistedKeywords.add("const");
            blacklistedKeywords.add("in");
        }

        outputConfig = new OutputConfigBuilder()
            .identifierMode(IdentifierOutputMode.Replaced)
            .indentation(get(compilerOption.getIndentation(), small, 0))
            .renderNewLines(get(compilerOption.isNewLines(), small, false))
            .blacklistKeyword(blacklistedKeywords)
            .showOriginalIdentifiers(true)
            .build();

        if (singleOutput) {
            if (!header && inputOutputs.size() > 1) {
                throw new StageException("Multiple shader files will be written to the same output file! Please use '-format c'");
            }

            final FileWriterStage writerStage = new FileWriterStage(inputOutputs.get(0).getOutput());
            final ConcatStage concatStage = new ConcatStage("\n");

            for (InputOutput inputOutput : inputOutputs) {
                final OptimizerReport report = new OptimizerReport();

                report.header(inputOutput.getInput());

                StagePipeline<Path, String> pipeline = singleInput(report, inputOutput)
                    .chain(concatStage)
                    .build();

                report.mark();
                pipeline.process(new PathStageData(inputOutput.getInput()));
                report.display();
            }

            writerStage.process(StageData.from(concatStage.getData(), null));
        } else {
            for (InputOutput inputOutput : inputOutputs) {
                final OptimizerReport report = new OptimizerReport();
                final FileWriterStage writerStage = new FileWriterStage(inputOutput.getOutput());

                report.header(inputOutput.getInput());

                StagePipeline<Path, String> pipeline = singleInput(report, inputOutput)
                    .chain(writerStage)
                    .build();

                report.mark();
                pipeline.process(new PathStageData(inputOutput.getInput()));
                report.display();
            }
        }
    }

    private StagePipeline.Builder<Path, String> singleInput(OptimizerReport report, InputOutput inputOutput) {
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

        // setup the optimizer stage
        final SizeStage startSize = new SizeStage(report, OptimizerReport.START);
        final SizeStage finalSize = new SizeStage(report, OptimizerReport.END);
        final OptimizerStage optimizer = new OptimizerStage(optimizerOption, outputConfig, report);

        // setup the rendering stage
        Stage<Pair<Node, ParserContext>, String> renderStage;

        if (compilerOption.getOutputFormat() == OutputFormat.C_HEADER) {
            final String shaderId = compilerOption.getShaderId(inputOutput.getInput());
            final OutputConfig headerOutputConfig = outputConfig.edit().indentation(4).build();

            renderStage = new HeaderRenderStage(shaderId, "", headerOutputConfig)
                    .setVersionSupplier(() -> preprocess.getResult().getGLSLVersion());
        } else {
            renderStage = new RenderStage(outputConfig, compilerOption.getOutputFormat())
                    .setVersionSupplier(() -> preprocess.getResult().getGLSLVersion());
        }

        // setup the output
        FileWriterStage writerStage = new FileWriterStage(inputOutput.getOutput());

        return StagePipeline
            .create(preprocess)
            .chain(startSize)
            .chain(postPreprocess)
            .chain(compile)
            .chain(optimizer)
            .chain(renderStage)
            .chain(finalSize);
    }

    private static int get(int value, boolean control, int override) {
        return control ? override : value;
    }

    private static boolean get(boolean value, boolean control, boolean override) {
        return control ? override : value;
    }

}

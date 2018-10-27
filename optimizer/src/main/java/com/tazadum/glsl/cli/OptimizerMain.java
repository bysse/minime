package com.tazadum.glsl.cli;

import com.tazadum.glsl.cli.options.CompilerOptions;
import com.tazadum.glsl.cli.options.OptimizerOptions;
import com.tazadum.glsl.cli.options.PreprocessorOptions;
import com.tazadum.glsl.language.output.IdentifierOutputMode;
import com.tazadum.glsl.language.output.OutputConfigBuilder;
import com.tazadum.glsl.stage.*;
import com.tazadum.glsl.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

import static com.tazadum.glsl.cli.CommandLineBase.*;

/**
 * Created by erikb on 2018-10-24.
 */
public class OptimizerMain {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(OptimizerMain.class);

        PreprocessorOptions preprocessorOption = new PreprocessorOptions();
        CompilerOptions compilerOption = new CompilerOptions(0, false);
        OptimizerOptions optimizerOption = new OptimizerOptions();

        CommandLineBase cli = new CommandLineBase(
            OptimizerMain.class.getName(),
            "GLSL Minifier.",
            preprocessorOption,
            compilerOption,
            optimizerOption
        );

        InputOutput inputOutput = cli.process(args);
        if (inputOutput == null) {
            cli.showHelp(false);
            System.exit(RET_SYNTAX);
            return;
        }

        try {
            // setup the preprocessor
            PreprocessorStage preprocess = new PreprocessorStage(preprocessorOption.getGLSLVersion());
            for (Pair<String, String> pair : preprocessorOption.getObjectLike()) {
                preprocess.define(pair.getFirst(), pair.getSecond());
            }

            // setup the compiler stage
            CompilerStage compile = new CompilerStage(compilerOption.getShaderType(), compilerOption.getProfile());

            // setup the optimizer stage
            OptimizerStage optimizer = new OptimizerStage();

            // setup the rendering stage
            final OutputConfigBuilder builder = new OutputConfigBuilder()
                .identifierMode(IdentifierOutputMode.Replaced)
                .indentation(compilerOption.getIndentation())
                .renderNewLines(compilerOption.isNewLines());

            for (String keyword : compilerOption.getKeywords()) {
                builder.blacklistKeyword(keyword);
            }

            RenderStage render = new RenderStage(builder.build());

            // setup the output
            FileWriterStage writerStage = new FileWriterStage(inputOutput.getOutput());

            StagePipeline<Path, String> pipeline = StagePipeline
                .create(preprocess)
                .chain(compile)
                .chain(optimizer)
                .chain(render)
                // TODO: insert format stage here and in the compiler
                .chain(writerStage)
                .build();

            pipeline.process(new PathStageData(inputOutput.getInput()));
            System.exit(RET_OK);
        } catch (StageException e) {
            logger.error(e.getMessage(), e);
            System.exit(RET_EXCEPTION);
        }
    }
}

package com.tazadum.glsl.cli;

import com.tazadum.glsl.cli.options.PreprocessorOptions;
import com.tazadum.glsl.preprocessor.PreprocessorException;
import com.tazadum.glsl.stage.*;
import com.tazadum.glsl.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

import static com.tazadum.glsl.cli.CommandLineBase.*;

/**
 * Created by erikb on 2018-10-24.
 */
public class PreprocessorMain {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(PreprocessorMain.class);
        PreprocessorOptions options = new PreprocessorOptions();

        CommandLineBase cli = new CommandLineBase(
                "minime-preprocessor",
                "MiniMe: Preprocessor.",
                false,
                options
        );


        CommandLineBase.InputOutput inputOutput = cli.process(args);
        if (inputOutput == NO_RESULT) {
            cli.showHelp(false);
            System.exit(RET_SYNTAX);
            return;
        }

        try {
            // setup the preprocessor
            PreprocessorStage stage1 = new PreprocessorStage(options.getGLSLVersion());
            for (Pair<String, String> pair : options.getObjectLike()) {
                stage1.define(pair.getFirst(), pair.getSecond());
            }

            // setup the output
            Stage<String, String> writerStage = cli.isOutputToStdout() ? new StandardOutWriterStage() : new FileWriterStage(inputOutput.getOutput());

            StagePipeline<Path, String> pipeline = StagePipeline
                    .create(stage1)
                    .chain(writerStage)
                    .build();

            pipeline.process(new PathStageData(inputOutput.getInput()));

            System.exit(RET_OK);
        } catch (PreprocessorException | StageException e) {
            logger.error(e.getMessage());
            System.exit(RET_EXCEPTION);
        }
    }
}

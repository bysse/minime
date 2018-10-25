package com.tazadum.glsl.cli;

import com.tazadum.glsl.cli.options.PreprocessorOptions;
import com.tazadum.glsl.stage.*;
import com.tazadum.glsl.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

/**
 * Created by erikb on 2018-10-24.
 */
public class PreprocessorMain {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(PreprocessorMain.class);
        PreprocessorOptions options = new PreprocessorOptions();

        CommandLineBase cli = new CommandLineBase(options, PreprocessorMain.class.getName());
        CommandLineBase.InputOutput inputOutput = cli.process(args);
        if (inputOutput == null) {
            return;
        }

        try {

            PreprocessorStage stage1 = new PreprocessorStage(options.getGLSLVersion());
            for (Pair<String, String> pair : options.getObjectLike()) {
                stage1.define(pair.getFirst(), pair.getSecond());
            }

            FileWriterStage writerStage = new FileWriterStage(inputOutput.getOutput());

            StagePipeline<Path, String> pipeline = StagePipeline
                .create(stage1)
                .chain(writerStage)
                .build();

            pipeline.process(new PathStageData(inputOutput.getInput()));
        } catch (StageException e) {
            logger.error(e.getMessage(), e);
        }
    }
}

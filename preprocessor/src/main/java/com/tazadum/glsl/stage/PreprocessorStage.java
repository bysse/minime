package com.tazadum.glsl.stage;

import com.tazadum.glsl.cli.builder.PreprocessorExecutor;
import com.tazadum.glsl.preprocessor.Preprocessor;
import com.tazadum.glsl.preprocessor.language.GLSLVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

/**
 * Created by erikb on 2018-10-24.
 */
public class PreprocessorStage implements Stage<Path, String> {
    private final Logger logger = LoggerFactory.getLogger(PreprocessorStage.class);
    private final PreprocessorExecutor builder;
    private Preprocessor.Result result;

    public PreprocessorStage(GLSLVersion glslVersion) {
        builder = PreprocessorExecutor.create().version(glslVersion);
    }

    public Preprocessor.Result getResult() {
        return result;
    }

    public void define(String macro, String value) {
        builder.define(macro, value);
    }

    @Override
    public StageData<String> process(StageData<Path> input) {
        builder.source(input.getData());
        result = builder.process();

        for (String warning : result.getWarnings()) {
            logger.warn(warning);
        }

        return StageData.from(result.getSource(), result.getMapper());
    }
}

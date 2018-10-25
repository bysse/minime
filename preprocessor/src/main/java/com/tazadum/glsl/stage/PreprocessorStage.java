package com.tazadum.glsl.stage;

import com.tazadum.glsl.preprocessor.DefaultPreprocessor;
import com.tazadum.glsl.preprocessor.Preprocessor;
import com.tazadum.glsl.preprocessor.language.GLSLVersion;
import com.tazadum.glsl.util.io.FileSource;
import com.tazadum.glsl.util.io.Source;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by erikb on 2018-10-24.
 */
public class PreprocessorStage implements Stage<Path, String> {
    private Logger logger = LoggerFactory.getLogger(PreprocessorStage.class);
    private Preprocessor preprocessor;

    public PreprocessorStage(GLSLVersion glslVersion) {
        preprocessor = new DefaultPreprocessor(glslVersion);
    }

    public void define(String macro, String value) {
        preprocessor.define(macro, value);
    }

    @Override
    public StageData<String> process(StageData<Path> input) {
        try {
            Source source = new FileSource(input.getData());
            Preprocessor.Result result = preprocessor.process(source);

            for (String warning : result.getWarnings()) {
                logger.warn(warning);
            }

            return StageData.from(result.getSource(), result.getMapper());
        } catch (IOException e) {
            throw new StageException(e.getMessage(), e);
        }
    }
}

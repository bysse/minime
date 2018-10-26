package com.tazadum.glsl.cli;

import com.tazadum.glsl.cli.options.ParserOptions;
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
public class CompilerMain {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(CompilerMain.class);

        PreprocessorOptions preprocessor = new PreprocessorOptions();
        ParserOptions parser = new ParserOptions(4, true);

        CommandLineBase cli = new CommandLineBase(
            CompilerMain.class.getName(),
            "GLSL Parser and type checker.",
            preprocessor,
            parser
        );


        CommandLineBase.InputOutput inputOutput = cli.process(args);
        if (inputOutput == null) {
            cli.showHelp(false);
            System.exit(RET_SYNTAX);
            return;
        }

        try {
            // setup the preprocessor
            PreprocessorStage preprocess = new PreprocessorStage(preprocessor.getGLSLVersion());
            for (Pair<String, String> pair : preprocessor.getObjectLike()) {
                preprocess.define(pair.getFirst(), pair.getSecond());
            }

            // setup the compiler stage
            CompilerStage compile = new CompilerStage(parser.getShaderType(), parser.getProfile());

            // setup the rendering stage
            final OutputConfigBuilder builder = new OutputConfigBuilder()
                .identifierMode(IdentifierOutputMode.Original)
                .indentation(parser.getIndentation())
                .renderNewLines(parser.isNewLines());

            for (String keyword : parser.getKeywords()) {
                builder.blacklistKeyword(keyword);
            }

            RenderStage render = new RenderStage(builder.build());

            // setup the output
            FileWriterStage writerStage = new FileWriterStage(inputOutput.getOutput());

            StagePipeline<Path, String> pipeline = StagePipeline
                .create(preprocess)
                .chain(compile)
                .chain(render)
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

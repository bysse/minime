package com.tazadum.glsl.cli;

import com.tazadum.glsl.cli.options.CompilerOptions;
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

import static com.tazadum.glsl.cli.CommandLineBase.*;

/**
 * Created by erikb on 2018-10-24.
 */
public class CompilerMain {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(CompilerMain.class);

        PreprocessorOptions preprocessorOption = new PreprocessorOptions();
        CompilerOptions compilerOption = new CompilerOptions(4, true);

        CommandLineBase cli = new CommandLineBase(
                CompilerMain.class.getName(),
                "GLSL Parser and type checker.",
                false,
                preprocessorOption,
                compilerOption
        );


        CommandLineBase.InputOutput inputOutput = cli.process(args);
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

            Stage<String, String> postPreprocess = new NoOpStage();
            if (preprocessorOption.outputIntermediateResult()) {
                // if intermediate results are requested
                final Path outputPath = preprocessorOption.generateOutput(inputOutput.getInput());
                postPreprocess = new FileWriterStage(outputPath);
            }

            // setup the compiler stage
            CompilerStage compile = new CompilerStage(compilerOption.getShaderType(), compilerOption.getProfile());

            // setup the rendering stage
            Stage<Pair<Node, ParserContext>, String> renderStage;

            final OutputConfig config = new OutputConfigBuilder()
                    .identifierMode(IdentifierOutputMode.Original)
                    .indentation(compilerOption.getIndentation())
                    .renderNewLines(compilerOption.isNewLines())
                    .blacklistKeyword(compilerOption.getKeywords())
                    .build();

            if (compilerOption.getOutputFormat() == OutputFormat.C_HEADER) {
                final String shaderId = compilerOption.getShaderId(inputOutput.getInput());
                final String shaderHeader = generateShaderHeader(compilerOption.getShaderType());


                renderStage = new HeaderRenderStage(shaderId, shaderHeader, config.edit().indentation(4).build())
                        .setVersionSupplier(() -> preprocess.getResult().getGLSLVersion());
            } else {
                renderStage = new RenderStage(config, compilerOption.getOutputFormat())
                        .setVersionSupplier(() -> preprocess.getResult().getGLSLVersion());
            }

            // setup the output
            FileWriterStage writerStage = new FileWriterStage(inputOutput.getOutput());

            StagePipeline<Path, String> pipeline = StagePipeline
                    .create(preprocess)
                    .chain(postPreprocess)
                    .chain(compile)
                    .chain(renderStage)
                    .chain(writerStage)
                    .build();

            pipeline.process(new PathStageData(inputOutput.getInput()));
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
}

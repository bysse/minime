package com.tazadum.glsl.stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by erikb on 2018-10-25.
 */
public class FileWriterStage implements Stage<String, String> {
    private final Logger logger = LoggerFactory.getLogger(FileWriterStage.class);
    private final Path outputPath;

    public FileWriterStage(Path outputPath) {
        this.outputPath = outputPath;
    }

    @Override
    public StageData<String> process(StageData<String> s) {
        try {
            byte[] bytes = s.getData().getBytes(StandardCharsets.UTF_8);
            Files.write(outputPath, bytes);
        } catch (IOException e) {
            logger.error("Failed to write to file : " + outputPath.toAbsolutePath(), e);
        }

        return s;
    }
}

package com.tazadum.glsl.test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class TestData {
    private static final Pattern SPLIT = Pattern.compile("^---+$", Pattern.MULTILINE);

    public static Stream<Data> read(Path basePath, String... excludes) throws IOException {
        if (!Files.isDirectory(basePath)) {
            throw new IllegalArgumentException(basePath + " is not a directory");
        }

        final Set<String> excludeSet = new HashSet<>();
        Collections.addAll(excludeSet, excludes);

        // find all files to read
        final List<Path> paths = new ArrayList<>();

        Files.walkFileTree(basePath, new FileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes basicFileAttributes) {
                if (excludeSet.contains(path.getFileName().toString())) {
                    return FileVisitResult.SKIP_SUBTREE;
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) {
                if (!excludeSet.contains(path.getFileName().toString())) {
                    paths.add(path);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path path, IOException e) {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path path, IOException e) {
                return FileVisitResult.CONTINUE;
            }
        });

        return paths.stream().map((path) -> readFileData(basePath, path));
    }

    private static Data readFileData(Path basePath, Path path) {
        try {
            String content = Files.readString(path);
            String[] parts = SPLIT.split(content, 2);

            if (parts.length != 2) {
                throw new IllegalArgumentException("The format of " + path + " is not correct. Failed to split the file.");
            }

            String filename = basePath.relativize(path).toString();

            return new Data(filename, parts[0], parts[1], !filename.endsWith("fail"));
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read " + path, e);
        }
    }


    public static class Data {
        private final String filename;
        private final String expected;
        private final String input;
        private final boolean shouldWork;

        Data(String filename, String expected, String input, boolean shouldWork) {
            this.filename = filename;
            this.expected = expected.trim();
            this.input = input.trim();
            this.shouldWork = shouldWork;
        }

        public String getFilename() {
            return filename;
        }

        public String getInput() {
            return input;
        }

        public String getExpected() {
            return expected;
        }

        public boolean isShouldWork() {
            return shouldWork;
        }

        public String toString() {
            return filename;
        }
    }
}

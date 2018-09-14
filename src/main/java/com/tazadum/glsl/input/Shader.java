package com.tazadum.glsl.input;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by erikb on 2018-09-14.
 */
public class Shader {
    private static final Pattern pragmaInclude = Pattern.compile("\\s*#pragma\\s+include\\(([^)]+)\\)\\s*");

    private final String filename;
    private final TreeMap<Integer, LineMapper> lineMap;
    private final Set<Path> visitedShaders;
    private final StringBuilder content;

    public static Shader load(String filename) {
        return new Shader(filename);
    }

    public Shader(String filename) {
        this.filename = filename;
        this.lineMap = new TreeMap<>();
        this.visitedShaders = new HashSet<>();
        this.content = new StringBuilder(4096);

        loadPath(Paths.get(filename), 1);
    }

    public GLSLSource getSource() {
        final FileMapper fileMapper = new FileMapper(Paths.get(filename), lineMap);
        return new GLSLSource(filename, content.toString(), fileMapper);
    }

    private int loadPath(Path path, int lineNumber) {
        if (visitedShaders.contains(path)) {
            throw new IllegalArgumentException("Include loop found!");
        }
        visitedShaders.add(path);

        lineMap.put(lineNumber, LineMapper.fromOffset(path, lineNumber));

        int inserted = 0;
        Path directory = path.getParent();

        try (BufferedReader reader = new BufferedReader(createReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // do a fast line match first
                if (line.contains("#pragma")) {
                    Matcher matcher = pragmaInclude.matcher(line);
                    if (matcher.matches()) {
                        String filename = matcher.group(1);
                        Path includePath = directory.resolve(filename);

                        int nextLineNumber = loadPath(includePath, lineNumber);
                        inserted += nextLineNumber - lineNumber;
                        lineNumber = nextLineNumber;

                        // continue on this file after the inclusion
                        lineMap.put(lineNumber, LineMapper.fromOffset(path, inserted - 1));
                        continue;
                    }
                }

                content.append(line).append('\n');
                lineNumber++;
            }
            return lineNumber;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load file " + path, e);
        }
    }

    private Reader createReader(Path path) throws FileNotFoundException {
        return new FileReader(path.toFile());
    }
}

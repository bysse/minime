package com.tazadum.glsl.util.io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by erikb on 2018-09-28.
 */
public class FileSource implements Source {
    private final String id;
    private final BufferedReader reader;

    private int lineNumber = 0;
    private final Path path;

    public FileSource(Path path) throws FileNotFoundException {
        this.path = path;
        this.id = path.toString();
        this.reader = new BufferedReader(new FileReader(path.toFile()));
    }

    @Override
    public String getSourceId() {
        return id;
    }

    @Override
    public int getLineNumber() {
        return lineNumber;
    }

    @Override
    public String readLine() throws IOException {
        lineNumber++;
        return reader.readLine();
    }

    @Override
    public Source resolve(String filePath) throws FileNotFoundException {
        if (path.getParent() == null) {
            return new FileSource(Paths.get(filePath));
        }

        return new FileSource(path.getParent().resolve(filePath));
    }

    public Path getPath() {
        return path;
    }

    public String toString() {
        return id + ":" + lineNumber;
    }
}

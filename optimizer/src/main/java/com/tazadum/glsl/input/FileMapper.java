package com.tazadum.glsl.input;

import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by erikb on 2018-09-14.
 */
public class FileMapper {
    private final Path path;
    private final TreeMap<Integer, LineMapper> lineMap;

    public FileMapper(Path path, TreeMap<Integer, LineMapper> lineMap) {
        this.path = path;
        this.lineMap = lineMap;
    }

    /**
     * Returns the Path of the file.
     */
    public Path getPath() {
        return path;
    }

    /**
     * Returns a LineMapper for the file.
     */
    public FilePosition map(int lineNumber) {
        final Map.Entry<Integer, LineMapper> entry = lineMap.floorEntry(lineNumber);
        if (entry == null) {
            return new FilePosition(path, lineNumber, true);
        }

        final LineMapper mapper = entry.getValue();
        final Path shaderPath = mapper.getPath();
        final int localLineNumber = mapper.map(lineNumber);

        return new FilePosition(shaderPath, localLineNumber, path.equals(shaderPath));
    }
}

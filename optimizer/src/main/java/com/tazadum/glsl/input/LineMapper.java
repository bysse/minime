package com.tazadum.glsl.input;

import java.nio.file.Path;

/**
 * Created by erikb on 2018-09-14.
 */
public interface LineMapper {
    /**
     * Returns the path of the mapping.
     */
    Path getPath();

    /**
     * Maps a line number from the global shader to a local line number.
     */
    int map(int line);

    static LineMapper fromOffset(Path path, int offset) {
        return new LineMapper() {
            @Override
            public Path getPath() {
                return path;
            }

            @Override
            public int map(int line) {
                return line - offset + 1;
            }
        };
    }
}

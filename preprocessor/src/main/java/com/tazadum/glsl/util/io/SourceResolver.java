package com.tazadum.glsl.util.io;

import java.io.IOException;

public interface SourceResolver {
    /**
     * Attempts to resolve and create a Source for the provided filePath.
     *
     * @param resource A relative or absolute path toa  file.
     */
    Source resolve(String resource) throws IOException;
}

package com.tazadum.glsl.util.io;

import java.io.IOException;

/**
 * Created by erikb on 2018-09-28.
 */
public interface Source extends SourceResolver {
    /**
     * Returns the id of the Source.
     */
    String getSourceId();

    /**
     * Returns the current line number.
     */
    int getLineNumber();

    /**
     * Read a line from the active Source. If the EOF is reached for all Sources null will be returned.
     */
    String readLine() throws IOException;
}

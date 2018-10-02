package com.tazadum.glsl.util.io;

import java.io.IOException;

/**
 * Created by erikb on 2018-09-28.
 */
public interface Source {
    String getSourceId();

    int getLineNumber();

    String readLine() throws IOException;

    Source resolve(String filePath) throws IOException;
}

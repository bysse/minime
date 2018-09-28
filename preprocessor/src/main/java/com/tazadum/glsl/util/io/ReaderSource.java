package com.tazadum.glsl.util.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Created by erikb on 2018-09-28.
 */
public class ReaderSource implements Source {
    private final String id;
    private final BufferedReader reader;

    private int lineNumber = 0;

    public ReaderSource(String id, Reader reader) {
        this.id = id;
        this.reader = new BufferedReader(reader);
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

    public String toString() {
        return id + ":" + lineNumber;
    }
}

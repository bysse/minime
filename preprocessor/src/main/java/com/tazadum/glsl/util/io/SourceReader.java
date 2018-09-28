package com.tazadum.glsl.util.io;

import java.io.IOException;
import java.util.Stack;

/**
 * Created by erikb on 2018-09-28.
 */
public class SourceReader {
    private Stack<Source> sources;
    private Source activeSource;
    private int lineNumber;

    public SourceReader(Source source) {
        this.sources = new Stack<>();
        this.activeSource = source;
        this.lineNumber = 0;
    }

    public void push(Source source) {
        sources.add(source);
        activeSource = source;
    }

    public Source pop() {
        try {
            return sources.pop();
        } finally {
            activeSource = sources.peek();
        }
    }

    public Source peek() {
        return activeSource;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String readLine() throws IOException {
        lineNumber++;
        return activeSource.readLine();
    }

    public String toString() {
        return String.format("%d from %s", lineNumber, activeSource);
    }
}

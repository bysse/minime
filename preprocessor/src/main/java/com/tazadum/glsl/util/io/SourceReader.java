package com.tazadum.glsl.util.io;

import java.io.IOException;
import java.util.Stack;

/**
 * Created by erikb on 2018-09-28.
 */
public class SourceReader implements Source {
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

    public Source getActiveSource() {
        return activeSource;
    }

    @Override
    public String getSourceId() {
        return activeSource.getSourceId();
    }

    /**
     * Return the reader's line number.
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * Returns the active source's line number.
     */
    public int getSourceLineNumber() {
        return activeSource.getLineNumber();
    }

    public String readLine() throws IOException {
        lineNumber++;
        String line = activeSource.readLine();
        while (line == null) {
            if (sources.size() <= 1) {
                // the stack is empty, end of file reached
                return null;
            }

            // read a line from the new source
            pop();
            line = activeSource.readLine();
        }
        return line;
    }

    @Override
    public Source resolve(String filePath) throws IOException {
        return activeSource.resolve(filePath);
    }

    public String toString() {
        return String.format("%d from %s", lineNumber, activeSource);
    }
}

package com.tazadum.glsl.util.io;

import com.tazadum.glsl.util.SourcePosition;
import com.tazadum.glsl.util.SourcePositionId;
import com.tazadum.glsl.util.SourcePositionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static com.tazadum.glsl.util.SourcePosition.TOP;

/**
 * Created by erikb on 2018-09-28.
 */
public class SourceReader implements Source {
    private static Logger logger = LoggerFactory.getLogger(SourceReader.class);

    private final Stack<Source> sources;
    private Source activeSource;
    private int lineNumber;
    private SourcePositionMapper mapper;
    private List<SourceResolver> resolvers = new ArrayList<>();

    public SourceReader(Source source) {
        this.sources = new Stack<>();
        this.sources.push(source);
        this.activeSource = source;
        this.lineNumber = 0;
        this.mapper = new SourcePositionMapper();
        this.mapper.remap(TOP, SourcePositionId.create(source.getSourceId(), TOP));
    }

    public void addResolver(SourceResolver sourceResolver) {
        resolvers.add(sourceResolver);
    }

    public int getDepth() {
        return sources.size();
    }

    public SourcePositionMapper getMapper() {
        return mapper;
    }

    public void push(Source source) {
        mapper.remap(SourcePosition.create(lineNumber, 0), SourcePositionId.create(source.getSourceId(), TOP));

        sources.add(source);
        activeSource = source;
    }

    public Source pop() {
        try {
            return sources.pop();
        } finally {
            activeSource = sources.peek();

            mapper.remap(
                    SourcePosition.create(lineNumber, 0),
                    SourcePositionId.create(activeSource.getSourceId(), SourcePosition.create(activeSource.getLineNumber(), 0))
            );
        }
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

    /**
     * Read a line from the active Source. If the EOF is reached for all Sources null will be returned.
     */
    public String readLine() throws IOException {
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

        lineNumber++;
        return line;
    }

    @Override
    public Source resolve(String resource) throws IOException {
        for (SourceResolver resolver : resolvers) {
            try {
                Source source = resolver.resolve(resource);
                if (source != null) {
                    return source;
                }
            } catch (IOException e) {
                logger.warn("Failed to resolve {}", resolver, e);
            }
        }

        return activeSource.resolve(resource);
    }

    public String toString() {
        return String.format("%d from %s", lineNumber, activeSource);
    }
}

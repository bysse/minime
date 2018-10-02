package com.tazadum.glsl.util.io;

import com.tazadum.glsl.util.LineTokenizer;

import java.io.IOException;

/**
 * Created by erikb on 2018-09-28.
 */
public class StringSource implements Source {
    private final String id;
    private final LineTokenizer tokenizer;

    public StringSource(String id, String source) {
        this.id = id;
        this.tokenizer = new LineTokenizer(source);
    }

    @Override
    public String getSourceId() {
        return id;
    }

    @Override
    public int getLineNumber() {
        return tokenizer.getLineNumber();
    }

    @Override
    public String readLine() throws IOException {
        return tokenizer.getLine();
    }

    @Override
    public Source resolve(String filePath) {
        return null;
    }
}

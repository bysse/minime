package com.tazadum.glsl.stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by erikb on 2018-10-25.
 */
public class ConcatStage implements Stage<String, String> {
    private final Logger logger = LoggerFactory.getLogger(ConcatStage.class);
    private final String separator;
    private final StringBuilder builder;

    public ConcatStage(String separator) {
        this.separator = separator;
        this.builder = new StringBuilder();
    }

    @Override
    public StageData<String> process(StageData<String> input) {
        builder.append(input.getData()).append(separator);
        return input;
    }

    public String getData() {
        return builder.toString();
    }
}

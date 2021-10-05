package com.tazadum.glsl.stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by erikb on 2021-10-05
 */
public class StandardOutWriterStage implements Stage<String, String> {
    private final Logger logger = LoggerFactory.getLogger(StandardOutWriterStage.class);

    public StandardOutWriterStage() {
    }

    @Override
    public StageData<String> process(StageData<String> s) {
        System.out.print(s.getData());
        return s;
    }
}

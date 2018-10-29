package com.tazadum.glsl.stage;

/**
 * Created by erikb on 2018-10-29.
 */
public class NoOpStage implements Stage<String, String> {
    @Override
    public StageData<String> process(StageData<String> s) {
        return s;
    }
}

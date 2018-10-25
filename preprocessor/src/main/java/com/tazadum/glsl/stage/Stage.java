package com.tazadum.glsl.stage;

/**
 * Created by erikb on 2018-10-24.
 */
public interface Stage<Input, Output> {
    StageData<Output> process(StageData<Input> input);
}

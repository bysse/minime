package com.tazadum.glsl.stage;

import com.tazadum.glsl.cli.OptimizerReport;

/**
 * Created by erikb on 2018-10-29.
 */
public class SizeStage implements Stage<String, String> {
    private final OptimizerReport reporter;
    private final String tag;

    public SizeStage(OptimizerReport reporter, String tag) {
        this.reporter = reporter;
        this.tag = tag;
    }

    @Override
    public StageData<String> process(StageData<String> input) {
        reporter.sizeAt(tag, input.getData().length());
        return input;
    }
}

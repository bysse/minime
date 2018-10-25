package com.tazadum.glsl.stage;

/**
 * Created by erikb on 2018-10-25.
 */
public class StagePipeline<Input, Output> implements Stage<Input, Output> {
    private final Stage<Input, Output> stage;

    public static <Input, Output> Builder<Input, Output> create(Stage<Input, Output> stage) {
        return new Builder<>(stage);
    }

    private StagePipeline(Stage<Input, Output> stage) {
        this.stage = stage;
    }

    @Override
    public StageData<Output> process(StageData<Input> input) {
        return stage.process(input);
    }

    public static class Builder<Input, Output> {
        private final Stage<Input, Output> stage;

        Builder(Stage<Input, Output> stage) {
            this.stage = stage;
        }

        public <Next> Builder<Input, Next> chain(Stage<Output, Next> nextStage) {
            return new Builder<>(new ChainStage<>(stage, nextStage));
        }

        public StagePipeline<Input, Output> build() {
            return new StagePipeline<>(stage);
        }
    }

    public static class ChainStage<A, B, T> implements Stage<A, B> {
        private final Stage<A, T> first;
        private final Stage<T, B> second;

        ChainStage(Stage<A, T> first, Stage<T, B> second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public StageData<B> process(StageData<A> input) {
            return second.process(first.process(input));
        }
    }
}

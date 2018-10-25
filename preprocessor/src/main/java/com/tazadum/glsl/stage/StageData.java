package com.tazadum.glsl.stage;

import com.tazadum.glsl.util.SourcePositionMapper;

/**
 * Created by erikb on 2018-10-25.
 */
public interface StageData<T> {
    T getData();

    SourcePositionMapper getMapper();

    static <T> StageData<T> from(T data, SourcePositionMapper mapper) {
        return new StageData<T>() {
            @Override
            public T getData() {
                return data;
            }

            @Override
            public SourcePositionMapper getMapper() {
                return mapper;
            }
        };
    }
}

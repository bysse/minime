package com.tazadum.glsl.util;

import java.util.Objects;

/**
 * Created by erikb on 2018-10-24.
 */
public interface Pair<U, V> {
    static <U, V> Pair<U, V> create(U u, V v) {
        return new Pair<U, V>() {
            @Override
            public U getFirst() {
                return u;
            }

            @Override
            public V getSecond() {
                return v;
            }

            public String toString() {
                return "(" + Objects.toString(u) + ", " + Objects.toString(v) + ")";
            }
        };
    }

    U getFirst();

    V getSecond();
}

package com.tazadum.glsl.ast.id;

import java.util.concurrent.atomic.AtomicInteger;

public class IdentifierCreator {
    private AtomicInteger count = new AtomicInteger(0);
    private String prefix;

    public IdentifierCreator(String prefix) {
        this.prefix = prefix;
    }

    public String get() {
        return "_" + prefix + count.getAndIncrement();
    }
}

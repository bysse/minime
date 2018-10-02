package com.tazadum.glsl.preprocessor;

import com.tazadum.glsl.util.SourcePositionId;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class LogKeeper {
    private List<String> warnings;

    public LogKeeper() {
        this.warnings = new ArrayList<>(1000);
    }

    public void reset() {
        warnings.clear();
    }

    public void addWarning(SourcePositionId sourcePosition, String format, Object... args) {
        warnings.add(sourcePosition.format() + " " + String.format(format, args));
    }

    public Stream<String> getWarnings() {
        return warnings.stream();
    }
}

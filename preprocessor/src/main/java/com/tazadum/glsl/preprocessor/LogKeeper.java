package com.tazadum.glsl.preprocessor;

import com.tazadum.glsl.util.SourcePosition;

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

    public void addWarning(int line, int column, String format, Object... args) {
        addWarning(SourcePosition.create(line, column), format, args);
    }

    public void addWarning(SourcePosition sourcePosition, String format, Object... args) {
        final String position = sourcePosition.getLine() + "(" + sourcePosition.getColumn() + ")";
        warnings.add(position + " " + String.format(format, args));
    }

    public Stream<String> getWarnings() {
        return warnings.stream();
    }
}

package com.tazadum.glsl.preprocessor;

import com.tazadum.glsl.util.SourcePositionId;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for storing and format warnings.
 */
public class LogKeeper {
    private final List<String> warnings;

    public LogKeeper() {
        this.warnings = new ArrayList<>(1000);
    }

    public void reset() {
        warnings.clear();
    }

    public void addWarning(SourcePositionId sourcePosition, String format, Object... args) {
        warnings.add(sourcePosition.format() + " " + String.format(format, args));
    }

    public List<String> getWarnings() {
        return warnings;
    }
}

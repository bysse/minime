package com.tazadum.slf4j;

import org.slf4j.event.Level;
import org.slf4j.spi.LocationAwareLogger;

/**
 * Created by erikb on 2018-10-25.
 */
public class LoggerConfig {
    public static final int LOG_LEVEL_TRACE = LocationAwareLogger.TRACE_INT;
    public static final int LOG_LEVEL_DEBUG = LocationAwareLogger.DEBUG_INT;
    public static final int LOG_LEVEL_INFO = LocationAwareLogger.INFO_INT;
    public static final int LOG_LEVEL_WARN = LocationAwareLogger.WARN_INT;
    public static final int LOG_LEVEL_ERROR = LocationAwareLogger.ERROR_INT;

    private int logLevel = LOG_LEVEL_INFO;
    private int errorStreamLevel = LOG_LEVEL_ERROR;
    private boolean showLoggerName = false;

    private String traceLabel = "TRACE";
    private String debugLabel = "DEBUG";
    private String infoLabel = "INFO";
    private String warningLabel = "WARNING";
    private String errorLabel = "ERROR";

    public int getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(int logLevel) {
        this.logLevel = logLevel;
    }

    public void setLogLevel(Level level) {
        this.logLevel = level.toInt();
    }


    public int getErrorStreamLevel() {
        return errorStreamLevel;
    }

    public void setErrorStreamLevel(int errorStreamLevel) {
        this.errorStreamLevel = errorStreamLevel;
    }

    public String getTraceLabel() {
        return traceLabel;
    }

    public boolean isShowLoggerName() {
        return showLoggerName;
    }

    public void setShowLoggerName(boolean showLoggerName) {
        this.showLoggerName = showLoggerName;
    }

    public void setTraceLabel(String traceLabel) {
        this.traceLabel = traceLabel;
    }

    public String getDebugLabel() {
        return debugLabel;
    }

    public void setDebugLabel(String debugLabel) {
        this.debugLabel = debugLabel;
    }

    public String getInfoLabel() {
        return infoLabel;
    }

    public void setInfoLabel(String infoLabel) {
        this.infoLabel = infoLabel;
    }

    public String getWarningLabel() {
        return warningLabel;
    }

    public void setWarningLabel(String warningLabel) {
        this.warningLabel = warningLabel;
    }

    public String getErrorLabel() {
        return errorLabel;
    }

    public void setErrorLabel(String errorLabel) {
        this.errorLabel = errorLabel;
    }
}

package com.tazadum.slf4j;

import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;

import java.io.PrintStream;

import static com.tazadum.slf4j.LoggerConfig.*;

/**
 * Created by erikb on 2018-10-25.
 */
public class TLogLogger extends MarkerIgnoringBase {
    private final String name;
    private final LoggerConfig config;

    public TLogLogger(String name) {
        this.name = name;
        this.config = TLogConfiguration.get().getConfig(name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isTraceEnabled() {
        return isLevelEnabled(LOG_LEVEL_TRACE);
    }

    @Override
    public void trace(String msg) {
        log(LOG_LEVEL_TRACE, msg, null);
    }

    @Override
    public void trace(String format, Object arg) {
        formatAndLog(LOG_LEVEL_TRACE, format, arg, null);
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        formatAndLog(LOG_LEVEL_TRACE, format, arg1, arg2);
    }

    @Override
    public void trace(String format, Object... arguments) {
        formatAndLog(LOG_LEVEL_TRACE, format, arguments);
    }

    @Override
    public void trace(String msg, Throwable t) {
        log(LOG_LEVEL_TRACE, msg, t);
    }

    @Override
    public boolean isDebugEnabled() {
        return isLevelEnabled(LOG_LEVEL_DEBUG);
    }

    @Override
    public void debug(String msg) {
        log(LOG_LEVEL_DEBUG, msg, null);
    }

    @Override
    public void debug(String format, Object arg) {
        formatAndLog(LOG_LEVEL_DEBUG, format, arg, null);
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        formatAndLog(LOG_LEVEL_DEBUG, format, arg1, arg2);
    }

    @Override
    public void debug(String format, Object... arguments) {
        formatAndLog(LOG_LEVEL_DEBUG, format, arguments);
    }

    @Override
    public void debug(String msg, Throwable t) {
        log(LOG_LEVEL_DEBUG, msg, t);
    }

    @Override
    public boolean isInfoEnabled() {
        return isLevelEnabled(LOG_LEVEL_INFO);
    }

    @Override
    public void info(String msg) {
        log(LOG_LEVEL_INFO, msg, null);
    }

    @Override
    public void info(String format, Object arg) {
        formatAndLog(LOG_LEVEL_INFO, format, arg, null);
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        formatAndLog(LOG_LEVEL_INFO, format, arg1, arg2);
    }

    @Override
    public void info(String format, Object... arguments) {
        formatAndLog(LOG_LEVEL_INFO, format, arguments);
    }

    @Override
    public void info(String msg, Throwable t) {
        log(LOG_LEVEL_INFO, msg, t);
    }

    @Override
    public boolean isWarnEnabled() {
        return isLevelEnabled(LOG_LEVEL_WARN);
    }

    @Override
    public void warn(String msg) {
        log(LOG_LEVEL_WARN, msg, null);
    }

    @Override
    public void warn(String format, Object arg) {
        formatAndLog(LOG_LEVEL_WARN, format, arg, null);
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        formatAndLog(LOG_LEVEL_WARN, format, arg1, arg2);
    }

    @Override
    public void warn(String format, Object... arguments) {
        formatAndLog(LOG_LEVEL_WARN, format, arguments);
    }

    @Override
    public void warn(String msg, Throwable t) {
        log(LOG_LEVEL_WARN, msg, t);
    }

    @Override
    public boolean isErrorEnabled() {
        return isLevelEnabled(LOG_LEVEL_ERROR);
    }

    @Override
    public void error(String msg) {
        log(LOG_LEVEL_ERROR, msg, null);
    }

    @Override
    public void error(String format, Object arg) {
        formatAndLog(LOG_LEVEL_ERROR, format, arg, null);
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        formatAndLog(LOG_LEVEL_ERROR, format, arg1, arg2);
    }

    @Override
    public void error(String format, Object... arguments) {
        formatAndLog(LOG_LEVEL_ERROR, format, arguments);
    }

    @Override
    public void error(String msg, Throwable t) {
        log(LOG_LEVEL_ERROR, msg, t);
    }

    /**
     * For formatted messages, first substitute arguments and then log.
     */
    private void formatAndLog(int level, String format, Object arg1, Object arg2) {
        if (!isLevelEnabled(level)) {
            return;
        }
        FormattingTuple tp = MessageFormatter.format(format, arg1, arg2);
        log(level, tp.getMessage(), tp.getThrowable());
    }

    /**
     * For formatted messages, first substitute arguments and then log.
     */
    private void formatAndLog(int level, String format, Object... arguments) {
        if (!isLevelEnabled(level)) {
            return;
        }
        FormattingTuple tp = MessageFormatter.arrayFormat(format, arguments);
        log(level, tp.getMessage(), tp.getThrowable());
    }

    private void log(int level, String message, Throwable t) {
        if (!isLevelEnabled(level)) {
            return;
        }

        String label = renderLevel(level);
        if (!label.isEmpty()) {
            label += ' ';
        }

        PrintStream printStream = getPrintStream(level);
        if (config.isShowLoggerName()) {
            printStream.print(label + name + ' ' + message);
        } else {
            printStream.print(label + message);
        }
        if (t != null) {
            t.printStackTrace(printStream);
        }
        printStream.flush();
    }

    private String renderLevel(int level) {
        switch (level) {
            case LOG_LEVEL_TRACE:
                return config.getTraceLabel();
            case LOG_LEVEL_DEBUG:
                return config.getDebugLabel();
            case LOG_LEVEL_INFO:
                return config.getInfoLabel();
            case LOG_LEVEL_WARN:
                return config.getWarningLabel();
            case LOG_LEVEL_ERROR:
                return config.getErrorLabel();
        }
        throw new IllegalStateException("Unrecognized level [" + level + "]");
    }

    private boolean isLevelEnabled(int logLevel) {
        return (logLevel >= config.getLogLevel());
    }

    private PrintStream getPrintStream(int level) {
        if (level >= config.getErrorStreamLevel()) {
            return System.err;
        }
        return System.out;
    }

}

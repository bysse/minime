package com.tazadum.slf4j;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by erikb on 2018-10-25.
 */
public class TLogLoggerFactory implements ILoggerFactory {
    private ConcurrentMap<String, TLogLogger> loggerMap = new ConcurrentHashMap<>();

    @Override
    public Logger getLogger(String name) {
        return loggerMap.computeIfAbsent(name, this::newLogger);
    }

    private TLogLogger newLogger(String loggerName) {
        return new TLogLogger(loggerName);
    }

    void reset() {
        loggerMap.clear();
    }
}

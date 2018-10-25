package com.tazadum.slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by erikb on 2018-10-25.
 */
public class TLogConfiguration {
    private static volatile TLogConfiguration _instance = null;

    public static TLogConfiguration get() {
        if (_instance == null) {
            synchronized (TLogConfiguration.class) {
                if (_instance == null) {
                    _instance = new TLogConfiguration();
                }
            }
        }
        return _instance;
    }

    private final ConcurrentMap<String, LoggerConfig> configMap;
    private LoggerConfig globalConfiguration;

    private TLogConfiguration() {
        configMap = new ConcurrentHashMap<>();
    }

    public void useGlobalConfiguration() {
        this.globalConfiguration = new LoggerConfig();
    }

    public LoggerConfig getConfig() {
        if (globalConfiguration == null) {
            throw new IllegalStateException("No global configuration has been set");
        }
        return globalConfiguration;
    }

    public LoggerConfig getConfig(String logger) {
        if (globalConfiguration != null) {
            return globalConfiguration;
        }

        if (logger == null) {
            throw new IllegalArgumentException("No logger configuration can be associated with a null logger name.");
        }
        return configMap.computeIfAbsent(logger, (name) -> new LoggerConfig());
    }

    void reset() {
        configMap.clear();
    }
}

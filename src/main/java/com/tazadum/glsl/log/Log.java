package com.tazadum.glsl.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log {
    public static Logger getApplicationLogger() {
        return LoggerFactory.getLogger("glsl-optimizier");
    }
}

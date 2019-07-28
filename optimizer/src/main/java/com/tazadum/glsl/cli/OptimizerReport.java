package com.tazadum.glsl.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.Locale;

/**
 * Created by erikb on 2018-10-29.
 */
public class OptimizerReport {
    public static final String START = "start";
    public static final String END = "end";

    private static final long MINUTE_MS = 60_000;
    private static final long SECOND_MS = 1000;

    private Logger logger = LoggerFactory.getLogger(OptimizerReport.class);

    private int startSize = 0;
    private int endSize = 0;
    private int branches = 0;
    private int iterations = 0;
    private int changes = 0;
    private long timestamp;

    public OptimizerReport() {
    }

    public void sizeAt(String tag, int size) {
        switch (tag) {
            case START:
                startSize = size;
                break;
            case END:
                endSize = size;
                break;
        }
    }

    public void addBranches(int branches) {
        this.branches += branches;
    }

    public void addChanges(int changes) {
        this.changes += changes;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public void mark() {
        timestamp = System.currentTimeMillis();
    }

    public void header(Path path) {
        logger.info("-----------------------------------------------------------------");
        logger.info("Filename: {}", path);
        logger.info("-----------------------------------------------------------------");
    }

    public void header(String shaderId) {
        logger.info("-----------------------------------------------------------------");
        logger.info("Shader: {}", shaderId);
        logger.info("-----------------------------------------------------------------");
    }

    public void display() {
        long duration = System.currentTimeMillis() - timestamp;
        String percent = String.format(Locale.US, "%.1f", 100f * endSize / startSize);

        logger.info("-----------------------------------------------------------------");
        logger.info(" Start size: {} bytes", startSize);
        logger.info(" Final size: {} bytes", endSize);
        logger.info("   absolute: {} bytes", endSize - startSize);
        logger.info("    percent: {}%", percent);
        logger.info("");
        logger.info(String.format(Locale.US, " Explored %d branches over %d iterations and made %d changes.", branches, iterations, changes));
        logger.info(" Time: {}", friendlyDuration(duration));
        logger.info("-----------------------------------------------------------------");
        logger.info("");
    }

    private String friendlyDuration(long duration) {
        long min = duration / MINUTE_MS;
        duration -= min * MINUTE_MS;
        long sec = duration / SECOND_MS;
        duration -= sec * SECOND_MS;

        String result = String.format(Locale.US, "%d ms", duration);
        if (sec > 0) {
            result = String.format(Locale.US, "%d sec ", sec) + result;
        }
        if (min > 0) {
            result = String.format(Locale.US, "%d min ", min) + result;
        }
        return result;
    }
}

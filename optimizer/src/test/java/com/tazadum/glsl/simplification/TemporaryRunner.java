package com.tazadum.glsl.simplification;

public class TemporaryRunner {
    public static void main(String[] args) throws Exception {
        RuleOptimizerTest test = new RuleOptimizerTest();
        test.setUp();
        test.testSimple();

        test.setUp();
        test.testDivision();

        test.setUp();
        test.testReArrangement();
    }
}

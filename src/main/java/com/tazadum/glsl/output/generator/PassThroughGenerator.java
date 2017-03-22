package com.tazadum.glsl.output.generator;// (C) King.com Ltd 2017

import com.tazadum.glsl.GLSLOptimizerContext;

public class PassThroughGenerator implements FileGenerator {
    @Override
    public String generate(GLSLOptimizerContext optimizerContext, String shaderGLSL) {
        return shaderGLSL;
    }
}

package com.tazadum.glsl.output.generator;// (C) King.com Ltd 2017

import com.tazadum.glsl.GLSLOptimizerContext;

public interface FileGenerator {
    String generate(GLSLOptimizerContext optimizerContext, String shaderGLSL);
}

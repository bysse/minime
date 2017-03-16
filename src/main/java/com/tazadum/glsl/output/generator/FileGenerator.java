package com.tazadum.glsl.output.generator;// (C) King.com Ltd 2017

import com.tazadum.glsl.GLSLOptimizerContext;
import com.tazadum.glsl.ast.Node;

public interface FileGenerator {
    String generate(GLSLOptimizerContext optimizerContext, Node shaderNode, String shaderGLSL);
}

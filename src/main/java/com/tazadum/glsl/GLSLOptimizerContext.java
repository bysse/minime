package com.tazadum.glsl;// (C) King.com Ltd 2017

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.ParserContextImpl;
import com.tazadum.glsl.parser.function.FunctionRegistryImpl;
import com.tazadum.glsl.parser.type.TypeChecker;
import com.tazadum.glsl.parser.type.TypeRegistryImpl;
import com.tazadum.glsl.parser.variable.VariableRegistryImpl;

public class GLSLOptimizerContext {
    private final String shaderName;

    private TypeChecker typeChecker;
    private ParserContext parserContext;
    private Node node;
    private String source;
    private String header;

    public GLSLOptimizerContext(String shaderName) {
        this.shaderName = shaderName;
        this.typeChecker = new TypeChecker();
        this.parserContext = new ParserContextImpl(new TypeRegistryImpl(), new VariableRegistryImpl(), new FunctionRegistryImpl());
        this.header = "";
    }

    public String getShaderName() {
        return shaderName;
    }

    public TypeChecker typeChecker() {
        return typeChecker;
    }

    public ParserContext parserContext() {
        return parserContext;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getHeader() {
        return header;
    }
}

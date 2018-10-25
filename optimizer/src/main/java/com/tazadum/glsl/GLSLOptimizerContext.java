package com.tazadum.glsl;

import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.parser.ParserContext;

public class GLSLOptimizerContext {
    private final String shaderName;

    private ParserContext parserContext;
    private Node node;
    private String source;
    private String header;

    public GLSLOptimizerContext(String shaderName, ParserContext parserContext) {
        this.shaderName = shaderName;
        this.parserContext = parserContext;
        this.header = "";
    }

    public String getShaderName() {
        return shaderName;
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

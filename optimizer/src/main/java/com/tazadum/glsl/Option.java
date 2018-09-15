package com.tazadum.glsl;

public enum Option {
    ShaderToy("shadertoy", "Output ShaderToy compatible shaders.", false),
    SilentOutput("silent", "No output during optimizations.", false),

    ShareFunctionsBetweenShaders("share", "Share all functions except main among the input shaders", false),
    NoArithmeticSimplifications("no-arithmetic", "Turn off arithmetic optimizations.", false),

    NoIdentifierRenaming("no-renaming", "Do not rename any symbols.", false),
    NoPragmaOnce("no-pragma-once", "Do not add #pragma once to the output file.", false),
    PassThrough("no-op", "Do not modify the input.", false),
    BitPackSource("bit-pack", "Only use 7 bits for each shader character.", false),
    OutputLineBreaks("line-breaks", "Output line breaks.", false),
    ;

    private final String symbol;
    private final String description;
    private final boolean defval;

    Option(String symbol, String description, boolean defval) {
        this.symbol = symbol;
        this.description = description;
        this.defval = defval;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getDescription() {
        return description;
    }

    public boolean getDefault() {
        return defval;
    }
}

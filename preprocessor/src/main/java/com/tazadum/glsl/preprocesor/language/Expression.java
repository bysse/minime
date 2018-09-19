package com.tazadum.glsl.preprocesor.language;

import com.tazadum.glsl.preprocesor.language.ast.IdentifierNode;
import com.tazadum.glsl.preprocesor.language.ast.IntegerNode;

public interface Expression extends Node {
    void accept(Visitor visitor);

    interface Visitor {

        void visit(IntegerNode node);

        void visit(IdentifierNode node);
    }
}

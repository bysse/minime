package com.tazadum.glsl.preprocesor.language;

import com.tazadum.glsl.preprocesor.language.ast.*;
import com.tazadum.glsl.preprocesor.language.ast.flow.*;
import com.tazadum.glsl.preprocesor.model.DeclarationType;

/**
 * Created by erikb on 2018-09-17.
 */
public interface Declaration extends Node {
    DeclarationType getDeclarationType();

    void accept(Visitor visitor);

    interface Visitor {
        void visit(NoOpDeclarationNode node);

        void visit(ExtensionDeclarationNode node);

        void visit(VersionDeclarationNode node);

        void visit(LineDeclarationNode node);

        void visit(PragmaDeclarationNode node);

        void visit(PragmaIncludeDeclarationNode node);

        void visit(MacroDeclarationNode node);

        void visit(IfFlowNode node);

        void visit(IfDefinedFlowNode node);

        void visit(IfNotDefinedFlowNode node);

        void visit(ElseFlowNode node);

        void visit(ElseIfFlowNode node);

        void visit(EndIfFlowNode node);

        void visit(UnDefineFlowNode node);
    }
}

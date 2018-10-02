package com.tazadum.glsl.preprocessor.language;

import com.tazadum.glsl.preprocessor.language.ast.*;
import com.tazadum.glsl.preprocessor.language.ast.flow.*;

/**
 * Created by erikb on 2018-09-17.
 */
public interface Declaration extends Node {
    DeclarationType getDeclarationType();

    void accept(Visitor visitor);

    interface Visitor {
        default void visit(NoOpDeclarationNode node) {
        }

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

        void visit(ErrorDeclarationNode node);
    }
}

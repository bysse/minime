package com.tazadum.glsl.preprocessor.model;

import com.tazadum.glsl.preprocessor.PreprocessorException;
import com.tazadum.glsl.preprocessor.language.Declaration;
import com.tazadum.glsl.preprocessor.language.ast.*;
import com.tazadum.glsl.preprocessor.language.ast.flow.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by erikb on 2018-09-17.
 */
public class PreprocessorState {
    private GLSLVersion version;
    private GLSLProfile profile;

    private Map<String, MacroDefinition> macroMap;

    private StateVisitor stateVisitor;

    public PreprocessorState() {
        version = GLSLVersion.OpenGL20;
        profile = null;

        macroMap = new HashMap<>();
        stateVisitor = new StateVisitor();
    }

    public boolean skipLines() {
        return false;
    }

    public void accept(int lineNumber, Declaration declaration) {
        if (lineNumber > 1 && declaration instanceof VersionDeclarationNode) {
            throw new PreprocessorException("The #version directive must occur in a shader before anything else, except for comments and white space.");
        }
        declaration.accept(stateVisitor);
    }

    private class StateVisitor implements Declaration.Visitor {
        @Override
        public void visit(NoOpDeclarationNode node) {

        }

        @Override
        public void visit(ExtensionDeclarationNode node) {

        }

        @Override
        public void visit(MacroDeclarationNode node) {

        }

        @Override
        public void visit(IfFlowNode node) {

        }

        @Override
        public void visit(IfDefinedFlowNode node) {

        }

        @Override
        public void visit(IfNotDefinedFlowNode node) {

        }

        @Override
        public void visit(ElseFlowNode node) {

        }

        @Override
        public void visit(ElseIfFlowNode node) {

        }

        @Override
        public void visit(EndIfFlowNode node) {

        }

        @Override
        public void visit(UnDefineFlowNode node) {

        }

        @Override
        public void visit(VersionDeclarationNode node) {

        }

        @Override
        public void visit(LineDeclarationNode node) {

        }

        @Override
        public void visit(PragmaDeclarationNode node) {

        }

        @Override
        public void visit(PragmaIncludeDeclarationNode node) {

        }
    }
}

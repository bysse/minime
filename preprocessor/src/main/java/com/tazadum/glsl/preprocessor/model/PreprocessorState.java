package com.tazadum.glsl.preprocessor.model;

import com.tazadum.glsl.preprocessor.LogKeeper;
import com.tazadum.glsl.preprocessor.Message;
import com.tazadum.glsl.preprocessor.PreprocessorException;
import com.tazadum.glsl.preprocessor.language.Declaration;
import com.tazadum.glsl.preprocessor.language.GLSLProfile;
import com.tazadum.glsl.preprocessor.language.GLSLVersion;
import com.tazadum.glsl.preprocessor.language.ast.*;
import com.tazadum.glsl.preprocessor.language.ast.flow.*;
import com.tazadum.glsl.util.SourcePosition;
import com.tazadum.glsl.util.SourcePositionId;
import com.tazadum.glsl.util.io.Source;
import com.tazadum.glsl.util.io.SourceReader;

import java.io.IOException;
import java.util.Stack;

import static com.tazadum.glsl.preprocessor.model.BoolIntLogic.isTrue;

/**
 * Created by erikb on 2018-09-17.
 */
public class PreprocessorState {
    public static final int MAX_INCLUDE_DEPTH = 30;

    private final LogKeeper logKeeper;
    private final MacroRegistry registry;
    private final StateVisitor stateVisitor;
    private final ExpressionEvaluator evaluator;
    private final Stack<Boolean> enabledSection;

    private int disableDepth = -1;

    private GLSLVersion version;
    private GLSLProfile profile;
    private SourceReader sourceReader;

    public PreprocessorState() {
        version = GLSLVersion.OpenGL20;
        profile = null;

        logKeeper = new LogKeeper();
        registry = new MacroRegistry();
        evaluator = new ExpressionEvaluator(registry);
        stateVisitor = new StateVisitor();
        enabledSection = new Stack<>();

        enabledSection.push(Boolean.TRUE);
    }

    /**
     * Returns the MacroRegistry that holds the state of all macros.
     */
    public MacroRegistry getMacroRegistry() {
        return registry;
    }

    /**
     * Returns true if the current preprocessor section is enabled.
     */
    public boolean isSectionEnabled() {
        return enabledSection.peek();
    }

    public void accept(SourceReader sourceReader, int lineNumber, Declaration declaration) {
        this.sourceReader = sourceReader;
        if (lineNumber > 1 && declaration instanceof VersionDeclarationNode) {
            throw new PreprocessorException(SourcePosition.create(lineNumber, 0), Message.Error.VERSION_NOT_FIRST);
        }
        declaration.accept(stateVisitor);
    }

    public LogKeeper getLogKeeper() {
        return logKeeper;
    }

    private void include(SourcePositionId sourcePosition, String filePath) {
        if (sourceReader.getDepth() >= MAX_INCLUDE_DEPTH) {
            throw new PreprocessorException(sourcePosition, Message.Error.INCLUDE_RECURSION);
        }

        try {
            Source source = sourceReader.resolve(filePath);
            if (source == null) {
                throw new PreprocessorException(sourcePosition, Message.Error.FILE_NOT_FOUND, filePath);
            }

            sourceReader.push(source);
        } catch (IOException e) {
            throw new PreprocessorException(sourcePosition, e, Message.Error.FILE_NOT_OPEN, filePath);
        }
    }

    public GLSLVersion getGLSLVersion() {
        return version;
    }

    private class StateVisitor implements Declaration.Visitor {
        @Override
        public void visit(ExtensionDeclarationNode node) {
            if (isSectionEnabled()) {
                // TODO: store the shit
            }
        }

        @Override
        public void visit(LineDeclarationNode node) {
            if (isSectionEnabled()) {
                logKeeper.addWarning(node.getSourcePosition(), Message.Warning.LINE_NOT_SUPPORTED);
            }
        }

        @Override
        public void visit(PragmaDeclarationNode node) {
            if (isSectionEnabled()) {
                logKeeper.addWarning(node.getSourcePosition(), Message.Warning.UNRECOGNIZED_PRAGMA, node.toString());
            }
        }

        @Override
        public void visit(PragmaIncludeDeclarationNode node) {
            if (isSectionEnabled()) {
                include(node.getSourcePosition(), node.getFilePath());
            }
        }

        @Override
        public void visit(MacroDeclarationNode node) {
            if (isSectionEnabled()) {
                // only do macro declarations in enabled sections
                final MacroRegistry registry = getMacroRegistry();
                final String name = node.getIdentifier();

                if (name.startsWith("__")) {
                    logKeeper.addWarning(node.getSourcePosition(), Message.Warning.RESERVED_MACRO_NAME);
                }

                if (name.startsWith("GL_")) {
                    throw new PreprocessorException(node.getSourcePosition(), Message.Error.RESERVED_MACRO_NAME);
                }

                if (node.getParameters() == null) {
                    // object like macro
                    registry.define(name, node.getValue());
                } else {
                    // function like macro
                    String[] parameters = node.getParameters().toArray(new String[0]);
                    registry.define(name, parameters, node.getValue());
                }
            }
        }

        @Override
        public void visit(IfFlowNode node) {
            if (isSectionEnabled()) {
                startOfSection(isTrue(node.getExpression().accept(evaluator)));
            } else {
                enabledSection.push(Boolean.FALSE);
            }
        }

        @Override
        public void visit(IfDefinedFlowNode node) {
            if (isSectionEnabled()) {
                startOfSection(registry.isDefined(node.getIdentifier()));
            } else {
                enabledSection.push(Boolean.FALSE);
            }
        }

        @Override
        public void visit(IfNotDefinedFlowNode node) {
            if (isSectionEnabled()) {
                startOfSection(!registry.isDefined(node.getIdentifier()));
            } else {
                enabledSection.push(Boolean.FALSE);
            }
        }

        @Override
        public void visit(ElseFlowNode node) {
            if (enabledSection.isEmpty()) {
                throw new PreprocessorException(node.getSourcePosition(), "Found '#else' with any corresponding #if, #ifdef or #ifndef.");
            }

            if (endOfSection()) {
                enabledSection.push(!enabledSection.pop());
            } else {
                enabledSection.pop();
                enabledSection.push(Boolean.FALSE);
            }
        }

        @Override
        public void visit(ElseIfFlowNode node) {
            if (enabledSection.isEmpty()) {
                throw new PreprocessorException(node.getSourcePosition(), "Found '#elif' with any corresponding #if, #ifdef or #ifndef.");
            }

            if (endOfSection()) {
                enabledSection.pop();
                startOfSection(isTrue(node.getExpression().accept(evaluator)));
            } else {
                enabledSection.pop();
                enabledSection.push(Boolean.FALSE);
            }
        }

        @Override
        public void visit(EndIfFlowNode node) {
            if (endOfSection()) {
                disableDepth = -1;
            }

            enabledSection.pop();

            if (enabledSection.isEmpty()) {
                throw new PreprocessorException(node.getSourcePosition(), "Found '#endif' with any corresponding #if, #ifdef or #ifndef.");
            }
        }

        @Override
        public void visit(UnDefineFlowNode node) {
            if (isSectionEnabled()) {
                registry.undefine(node.getIdentifier());
            }
        }

        @Override
        public void visit(ErrorDeclarationNode node) {
            if (isSectionEnabled()) {
                throw new PreprocessorException(node.getSourcePosition(), node.getMessage());
            }
        }

        @Override
        public void visit(VersionDeclarationNode node) {
            version = node.getVersion();
            profile = node.getProfile();
        }

        private void startOfSection(boolean enabled) {
            enabledSection.push(enabled);
            if (!enabled) {
                disableDepth = enabledSection.size();
            }
        }

        private boolean endOfSection() {
            return isSectionEnabled() || disableDepth == enabledSection.size();
        }
    }
}

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
    private final LogKeeper logKeeper;
    private final MacroRegistry registry;
    private final StateVisitor stateVisitor;
    private final ExpressionEvaluator evaluator;
    private final Stack<Boolean> enabledSection;

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
                String[] parameters = node.getParameters().toArray(new String[node.getParameters().size()]);
                registry.define(node.getIdentifier(), parameters, node.getValue());
            }
        }

        @Override
        public void visit(IfFlowNode node) {
            if (isEnabled()) {
                enabledSection.push(isTrue(node.getExpression().accept(evaluator)));
            }
        }

        @Override
        public void visit(IfDefinedFlowNode node) {
            if (isEnabled()) {
                enabledSection.push(registry.isDefined(node.getIdentifier()));
            }
        }

        @Override
        public void visit(IfNotDefinedFlowNode node) {
            if (isEnabled()) {
                enabledSection.push(!registry.isDefined(node.getIdentifier()));
            }
        }

        @Override
        public void visit(ElseFlowNode node) {
            if (isSectionEnabled()) {
                if (enabledSection.isEmpty()) {
                    throw new PreprocessorException(node.getSourcePosition(), "Found '#else' with any corresponding #if, #ifdef or #ifndef.");
                }
                enabledSection.push(!enabledSection.pop());
            }
        }

        @Override
        public void visit(ElseIfFlowNode node) {
            if (isSectionEnabled()) {
                if (enabledSection.isEmpty()) {
                    throw new PreprocessorException(node.getSourcePosition(), "Found '#elif' with any corresponding #if, #ifdef or #ifndef.");
                }
                enabledSection.pop();
                enabledSection.push(isTrue(node.getExpression().accept(evaluator)));
            }
        }

        @Override
        public void visit(EndIfFlowNode node) {
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
        public void visit(VersionDeclarationNode node) {
            version = node.getVersion();
            profile = node.getProfile();
        }

        /**
         * Check if the section if enabled. If not false is pushed on the stack.
         */
        private boolean isEnabled() {
            boolean enabled = enabledSection.peek();
            if (!enabled) {
                // nested elements are also false
                enabledSection.push(Boolean.FALSE);
            }
            return enabled;
        }

    }
}

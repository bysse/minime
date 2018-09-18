package com.tazadum.glsl.preprocessor.parser;

import com.tazadum.glsl.preprocesor.PreprocessorException;
import com.tazadum.glsl.preprocesor.language.Node;
import com.tazadum.glsl.preprocesor.language.PreprocessorVisitor;
import com.tazadum.glsl.preprocesor.language.ast.*;
import com.tazadum.glsl.preprocesor.model.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AstParserTest {
    private PreprocessorState state;

    @BeforeEach
    public void setup() {
        state = new PreprocessorState();
    }

    @Test
    public void testExtensionDeclaration() {
        assertThrows(PreprocessorException.class, () ->
            // extra tokens on the line
            parse(ExtensionDeclarationNode.class, "#extension all : warn FAIL")
        );

        assertThrows(ParseCancellationException.class, () ->
            // invalid syntax
            parse(ExtensionDeclarationNode.class, "#extension all : FAIL")
        );

        ExtensionDeclarationNode node = parse(ExtensionDeclarationNode.class, "#extension all : enable");

        assertEquals(DeclarationType.EXTENSION, node.getDeclarationType());
        assertEquals("all", node.getExtension());
        assertEquals(ExtensionBehavior.ENABLE, node.getBehavior());
    }

    @Test
    public void testVersionDeclaration() {
        assertThrows(PreprocessorException.class, () ->
            // extra tokens on the line
            parse(VersionDeclarationNode.class, "#version 400 core FAIL")
        );

        assertThrows(PreprocessorException.class, () ->
            // invalid version
            parse(VersionDeclarationNode.class, "#version 300")
        );

        VersionDeclarationNode node = parse(VersionDeclarationNode.class, "#version 440 core");

        assertEquals(DeclarationType.VERSION, node.getDeclarationType());
        assertEquals(GLSLVersion.OpenGL44, node.getVersion());
        assertEquals(GLSLProfile.CORE, node.getProfile());
    }

    @Test
    public void testLineDeclaration() {
        assertThrows(PreprocessorException.class, () ->
            // extra tokens on the line
            parse(LineDeclarationNode.class, "#line 10 10 FAIL")
        );

        assertThrows(ParseCancellationException.class, () ->
            // invalid syntax
            parse(LineDeclarationNode.class, "#line MACRO 10")
        );

        LineDeclarationNode node = parse(LineDeclarationNode.class, "#line 10 100");

        assertEquals(DeclarationType.LINE, node.getDeclarationType());
        assertEquals(10, node.getLineNumber());
        assertEquals(100, node.getSourceLineNumber());
    }

    @Test
    public void testPragmaDeclaration() {
        PragmaDeclarationNode node1 = parse(PragmaDeclarationNode.class, "#pragma optimizations(1)");
        assertEquals(DeclarationType.PRAGMA, node1.getDeclarationType());
        assertEquals("optimizations ( 1 )", node1.getDeclaration());

        final String filePath = "../shared/shared.glsl";
        PragmaIncludeDeclarationNode node2 = parse(PragmaIncludeDeclarationNode.class, "#pragma include(" + filePath + ")");
        assertEquals(DeclarationType.PRAGMA_INCLUDE, node2.getDeclarationType());
        assertEquals(filePath, node2.getFilePath());

    }

    private <T extends Node> T parse(Class<T> type, String source) {
        ParserRuleContext context = TestUtil.parse(source);
        Node node = context.accept(new PreprocessorVisitor());
        assertNotNull(node, "Resulting node should not be null");

        if (!type.isAssignableFrom(node.getClass())) {
            fail("The resulting AST node is not of type " + type.getSimpleName() + " it's of type " + node.getClass().getSimpleName());
        }
        return (T) node;
    }
}

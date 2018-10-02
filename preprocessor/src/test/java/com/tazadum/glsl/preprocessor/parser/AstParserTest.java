package com.tazadum.glsl.preprocessor.parser;

import com.tazadum.glsl.preprocessor.PreprocessorException;
import com.tazadum.glsl.preprocessor.language.*;
import com.tazadum.glsl.preprocessor.language.ast.*;
import com.tazadum.glsl.preprocessor.language.ast.flow.*;
import com.tazadum.glsl.preprocessor.model.*;
import com.tazadum.glsl.util.SourcePosition;
import com.tazadum.glsl.util.SourcePositionId;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AstParserTest {
    private PreprocessorState state;

    @BeforeEach
    public void setup() {
        state = new PreprocessorState();
    }

    @Test
    @DisplayName("#extension")
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
    @DisplayName("#version")
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
    @DisplayName("#line")
    public void testLineDeclaration() {
        assertThrows(PreprocessorException.class, () ->
            // extra tokens on the line
            parse(LineDeclarationNode.class, "#line 10 10 FAIL")
        );

        assertThrows(ParseCancellationException.class, () ->
            // invalid syntax
            parse(LineDeclarationNode.class, "#line MACRO 10")
        );

        LineDeclarationNode node_1 = parse(LineDeclarationNode.class, "#line 10 100");

        assertEquals(DeclarationType.LINE, node_1.getDeclarationType());
        assertEquals(10, node_1.getLineNumber());
        assertEquals(100, node_1.getSourceLineNumber());

        LineDeclarationNode node_2 = parse(LineDeclarationNode.class, "#line 10");

        assertEquals(DeclarationType.LINE, node_2.getDeclarationType());
        assertEquals(10, node_2.getLineNumber());
        assertEquals(LineDeclarationNode.NO_VALUE, node_2.getSourceLineNumber());

    }

    @Test
    @DisplayName("#pragma")
    public void testPragmaDeclaration() {
        PragmaDeclarationNode node1 = parse(PragmaDeclarationNode.class, "#pragma optimizations(1)");
        assertEquals(DeclarationType.PRAGMA, node1.getDeclarationType());
        assertEquals("optimizations(1)", node1.getDeclaration());

        final String filePath = "../shared/shared.glsl";
        PragmaIncludeDeclarationNode node2 = parse(PragmaIncludeDeclarationNode.class, "#pragma include(" + filePath + ")");
        assertEquals(DeclarationType.PRAGMA_INCLUDE, node2.getDeclarationType());
        assertEquals(filePath, node2.getFilePath());
    }

    @Test
    @DisplayName("#define")
    public void testMacroDeclaration() {
        MacroDeclarationNode node_1 = parse(MacroDeclarationNode.class, "#define MACRO");
        assertEquals(DeclarationType.DEFINE, node_1.getDeclarationType());
        assertEquals("MACRO", node_1.getIdentifier());
        assertNull(node_1.getParameters());
        assertNull(node_1.getValue());

        MacroDeclarationNode node_2 = parse(MacroDeclarationNode.class, "#define MACRO 1");
        assertEquals("MACRO", node_2.getIdentifier());
        assertNull(node_2.getParameters());
        assertEquals("1", node_2.getValue());

        MacroDeclarationNode node_3 = parse(MacroDeclarationNode.class, "#define MACRO(x,y) x* y");
        assertEquals("MACRO", node_3.getIdentifier());
        assertNotNull(node_3.getParameters());
        assertEquals(2, node_3.getParameters().size());
        assertEquals("x", node_3.getParameters().get(0));
        assertEquals("y", node_3.getParameters().get(1));
        assertEquals("x* y", node_3.getValue());
    }

    @Test
    @DisplayName("#else and #endif")
    public void testDeclarationWithoutArguments() {
        ElseFlowNode node1 = parse(ElseFlowNode.class, "#else // comment");
        assertEquals(DeclarationType.ELSE, node1.getDeclarationType());

        EndIfFlowNode node2 = parse(EndIfFlowNode.class, "#endif");
        assertEquals(DeclarationType.END_IF, node2.getDeclarationType());
    }

    @Test
    @DisplayName("#ifdef")
    public void testIfDefined() {
        assertThrows(PreprocessorException.class, () ->
                // extra tokens on the line
                parse(IfDefinedFlowNode.class, "#ifdef MACRO FAIL")
        );

        IfDefinedFlowNode node = parse(IfDefinedFlowNode.class, "#ifdef MACRO");
        assertEquals(DeclarationType.IF_DEFINED, node.getDeclarationType());
        assertEquals("MACRO", node.getIdentifier());
    }

    @Test
    @DisplayName("#ifndef")
    public void testIfNotDefined() {
        assertThrows(PreprocessorException.class, () ->
                // extra tokens on the line
                parse(IfNotDefinedFlowNode.class, "#ifndef MACRO FAIL")
        );

        IfNotDefinedFlowNode node = parse(IfNotDefinedFlowNode.class, "#ifndef MACRO");
        assertEquals(DeclarationType.IF_NOT_DEFINED, node.getDeclarationType());
        assertEquals("MACRO", node.getIdentifier());
    }

    @Test
    @DisplayName("#undef")
    public void testUndef() {
        assertThrows(PreprocessorException.class, () ->
                // extra tokens on the line
                parse(UnDefineFlowNode.class, "#undef MACRO FAIL")
        );

        UnDefineFlowNode node = parse(UnDefineFlowNode.class, "#undef MACRO");
        assertEquals(DeclarationType.UNDEF, node.getDeclarationType());
        assertEquals("MACRO", node.getIdentifier());
    }

    @Test
    @DisplayName("#if and #elif")
    public void testIfAndElseIf() {
        IfFlowNode node_1 = parse(IfFlowNode.class, "#if 1+(2*3)%4");
        assertEquals(DeclarationType.IF, node_1.getDeclarationType());
        assertNotNull(node_1.getExpression());

        IfFlowNode node_2 = parse(IfFlowNode.class, "#if defined(MACRO) || (APA << 2) >= 3^-2");
        assertEquals(DeclarationType.IF, node_2.getDeclarationType());
        assertNotNull(node_2.getExpression());

        ElseIfFlowNode node_3 = parse(ElseIfFlowNode.class, "#elif 4/MACRO && (!04 < 1 || ~0x14 >> 2)");
        assertEquals(DeclarationType.ELSE_IF, node_3.getDeclarationType());
        assertNotNull(node_3.getExpression());

        ElseIfFlowNode node_4 = parse(ElseIfFlowNode.class, "#elif 1|4 != 3");
        assertEquals(DeclarationType.ELSE_IF, node_4.getDeclarationType());
        assertNotNull(node_4.getExpression());
    }

    private <T extends Node> T parse(Class<T> type, String source) {
        ParserRuleContext context = TestUtil.parse(source);

        Node node = context.accept(new PreprocessorVisitor(SourcePositionId.create("test", SourcePosition.TOP)));
        assertNotNull(node, "Resulting node should not be null");

        if (!type.isAssignableFrom(node.getClass())) {
            fail("The resulting AST node is not of type " + type.getSimpleName() + " it's of type " + node.getClass().getSimpleName());
        }
        return (T) node;
    }
}

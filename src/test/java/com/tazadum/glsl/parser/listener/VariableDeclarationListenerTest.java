package com.tazadum.glsl.parser.listener;

import com.tazadum.glsl.ast.VariableDeclarationListNode;
import com.tazadum.glsl.language.GLSLParser;
import com.tazadum.glsl.parser.TestUtils;
import org.antlr.v4.runtime.CommonTokenStream;

public class VariableDeclarationListenerTest {

    private VariableDeclarationListNode parse(String code) {
        System.out.println("Parsing '" + code + "'");
        CommonTokenStream stream = TestUtils.tokenStream(code);
        GLSLParser parser = TestUtils.parser(stream);
        VariableDeclarationListener listener = new VariableDeclarationListener(null);

        listener.walk(parser.fully_specified_type());

        return listener.getResult();
    }

}

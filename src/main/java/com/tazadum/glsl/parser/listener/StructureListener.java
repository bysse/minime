package com.tazadum.glsl.parser.listener;

import com.tazadum.glsl.ast.ParentNode;
import com.tazadum.glsl.ast.Shader;
import com.tazadum.glsl.language.GLSLBaseListener;
import com.tazadum.glsl.language.GLSLParser;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.ParserListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.Stack;

import static com.tazadum.glsl.parser.listener.PrintingListener.wrap;

/**
 * @author erikb
 * @since 2016-07-31
 */
public class StructureListener extends GLSLBaseListener implements ParserListener {
    private final ParserContext parserContext;

    private Shader shader;
    private Stack<ParentNode> contextStack;

    public StructureListener(ParserContext parserContext) {
        this.parserContext = parserContext;

        contextStack = new Stack<>();
        contextStack.add(new Shader());
    }

    @Override
    public Shader getShader() {
        return shader;
    }


    @Override
    public void enterVariableDeclaration(GLSLParser.VariableDeclarationContext ctx) {
        VariableDeclarationListener listener = new VariableDeclarationListener(parserContext);

        ParseTreeWalker.DEFAULT.walk(wrap(listener), ctx.init_declarator_list());

        contextStack.peek().addChild(listener.getResult());
    }

    @Override
    public void exitVariableDeclaration(GLSLParser.VariableDeclarationContext ctx) {
    }
}

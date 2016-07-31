package com.tazadum.glsl.parser.listener;

import com.tazadum.glsl.ast.LeafNode;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.language.GLSLBaseListener;
import com.tazadum.glsl.language.GLSLParser;
import com.tazadum.glsl.parser.ParserContext;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import static com.tazadum.glsl.parser.listener.PrintingListener.wrap;

/**
 * @author erikb
 * @since 2016-07-31
 */
public class VariableDeclarationListener extends GLSLBaseListener {
    private final ParserContext parserContext;

    private Node result;

    public VariableDeclarationListener(ParserContext parserContext) {
        this.parserContext = parserContext;
    }

    public Node getResult() {
        return new LeafNode();
    }

    @Override
    public void enterSingle_declaration(GLSLParser.Single_declarationContext ctx) {
        TypeListener listener = new TypeListener(parserContext);
        ParseTreeWalker.DEFAULT.walk(wrap(listener), ctx.fully_specified_type());

        // TODO: get type from listener
    }

    @Override
    public void exitSingle_declaration(GLSLParser.Single_declarationContext ctx) {
    }
}

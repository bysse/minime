package com.tazadum.glsl.language.ast.type;

import com.tazadum.glsl.exception.BadImplementationException;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.parser.GLSLBaseVisitor;
import com.tazadum.glsl.parser.GLSLParser;

@Deprecated
public class TypeParserHelper {
    /**
     * Translates the results of the rule 'type_qualifier' to the AST model.
     */
    public static TypeQualifierListNode parseTypeQualifier(GLSLBaseVisitor<Node> visitor, GLSLParser.Type_qualifierContext qualifierContext) {
        throw new BadImplementationException();
    }
}

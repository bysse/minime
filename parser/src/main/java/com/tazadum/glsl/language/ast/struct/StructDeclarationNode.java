package com.tazadum.glsl.language.ast.struct;

import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.util.SourcePosition;

/**
 * Created by erikb on 2018-10-13.
 */
public class StructDeclarationNode extends ParentNode {
    public StructDeclarationNode(SourcePosition position) {
        super(position);
    }

    public StructDeclarationNode(SourcePosition position, ParentNode parentNode) {
        super(position, parentNode);
    }
}

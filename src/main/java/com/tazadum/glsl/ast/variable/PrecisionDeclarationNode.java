package com.tazadum.glsl.ast.variable;

import com.tazadum.glsl.ast.LeafNode;
import com.tazadum.glsl.ast.ParentNode;
import com.tazadum.glsl.language.BuiltInType;
import com.tazadum.glsl.language.PrecisionQualifier;

public class PrecisionDeclarationNode extends LeafNode {
    private final PrecisionQualifier qualifier;
    private final BuiltInType builtInType;

    public PrecisionDeclarationNode(PrecisionQualifier qualifier, BuiltInType builtInType) {
        this(null, qualifier, builtInType);
    }

    public PrecisionDeclarationNode(ParentNode newParent, PrecisionQualifier qualifier, BuiltInType builtInType) {
        super(newParent);
        this.qualifier = qualifier;
        this.builtInType = builtInType;
    }

    @Override
    public PrecisionDeclarationNode clone(ParentNode newParent) {
        return new PrecisionDeclarationNode(newParent, qualifier, builtInType);
    }
}

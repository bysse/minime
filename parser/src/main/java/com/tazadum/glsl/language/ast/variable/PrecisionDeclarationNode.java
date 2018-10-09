package com.tazadum.glsl.language.ast.variable;

import com.tazadum.glsl.language.PrecisionQualifier;
import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.LeafNode;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.language.type.PredefinedType;

public class PrecisionDeclarationNode extends LeafNode {
    private final PrecisionQualifier qualifier;
    private final PredefinedType builtInType;

    public PrecisionDeclarationNode(PrecisionQualifier qualifier, PredefinedType builtInType) {
        this(null, qualifier, builtInType);
    }

    public PrecisionDeclarationNode(ParentNode newParent, PrecisionQualifier qualifier, PredefinedType builtInType) {
        super(newParent);
        this.qualifier = qualifier;
        this.builtInType = builtInType;
    }

    public PrecisionQualifier getQualifier() {
        return qualifier;
    }

    public PredefinedType getPredefinedType() {
        return builtInType;
    }

    @Override
    public PrecisionDeclarationNode clone(ParentNode newParent) {
        return new PrecisionDeclarationNode(newParent, qualifier, builtInType);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitPrecision(this);
    }

    @Override
    public GLSLType getType() {
        return null;
    }
}

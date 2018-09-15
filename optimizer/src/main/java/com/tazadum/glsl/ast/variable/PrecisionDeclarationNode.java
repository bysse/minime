package com.tazadum.glsl.ast.variable;

import com.tazadum.glsl.ast.ASTVisitor;
import com.tazadum.glsl.ast.LeafNode;
import com.tazadum.glsl.ast.ParentNode;
import com.tazadum.glsl.language.BuiltInType;
import com.tazadum.glsl.language.GLSLType;
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

    public PrecisionQualifier getQualifier() {
        return qualifier;
    }

    public BuiltInType getBuiltInType() {
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

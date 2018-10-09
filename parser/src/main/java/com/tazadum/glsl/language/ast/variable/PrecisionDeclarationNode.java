package com.tazadum.glsl.language.ast.variable;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.LeafNode;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.model.PrecisionQualifier;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.language.type.PredefinedType;
import com.tazadum.glsl.util.SourcePosition;

public class PrecisionDeclarationNode extends LeafNode {
    private final PrecisionQualifier qualifier;
    private final PredefinedType builtInType;

    public PrecisionDeclarationNode(SourcePosition position, PrecisionQualifier qualifier, PredefinedType builtInType) {
        this(position, null, qualifier, builtInType);
    }

    public PrecisionDeclarationNode(SourcePosition position, ParentNode newParent, PrecisionQualifier qualifier, PredefinedType builtInType) {
        super(position, newParent);
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
        return new PrecisionDeclarationNode(getSourcePosition(), newParent, qualifier, builtInType);
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

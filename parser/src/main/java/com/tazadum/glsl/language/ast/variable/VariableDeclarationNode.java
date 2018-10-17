package com.tazadum.glsl.language.ast.variable;

import com.tazadum.glsl.language.ast.*;
import com.tazadum.glsl.language.ast.traits.HasConstState;
import com.tazadum.glsl.language.ast.traits.HasSharedState;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.model.ArraySpecifiers;
import com.tazadum.glsl.language.model.StorageQualifier;
import com.tazadum.glsl.language.type.FullySpecifiedType;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.util.SourcePosition;

public class VariableDeclarationNode extends FixedChildParentNode implements HasSharedState, HasConstState {
    protected final boolean builtIn;
    protected final FullySpecifiedType type;
    protected final FullySpecifiedType originalType;
    protected final ArraySpecifiers arraySpecifiers;

    protected Identifier identifier;
    private boolean shared;

    public VariableDeclarationNode(SourcePosition position, boolean builtIn, FullySpecifiedType fst, String identifier, ArraySpecifiers arraySpecifiers, Node initializer) {
        this(position, null, builtIn, fst, Identifier.orNull(identifier), arraySpecifiers, initializer);
    }

    protected VariableDeclarationNode(SourcePosition position, ParentNode newParent, boolean builtIn, FullySpecifiedType fst, Identifier identifier, ArraySpecifiers arraySpecifiers, Node initializer) {
        super(position, 2, newParent);

        this.builtIn = builtIn;
        this.identifier = identifier;
        this.arraySpecifiers = arraySpecifiers;
        this.originalType = fst;

        if (arraySpecifiers == null) {
            this.type = fst;
        } else {
            this.type = FullySpecifiedType.mergeArraySpecifier(fst, arraySpecifiers);
        }

        setInitializer(initializer);
    }

    public boolean isBuiltIn() {
        return builtIn;
    }


    public Identifier getIdentifier() {
        return identifier;
    }

    public Node getInitializer() {
        return getChild(0);
    }

    public void setInitializer(Node initializer) {
        setChild(0, initializer);
    }

    public FullySpecifiedType getFullySpecifiedType() {
        return type;
    }

    /**
     * Retruns the original type of the variable before any post fix array specifier has been applied.
     */
    public FullySpecifiedType getOriginalType() {
        return originalType;
    }

    public ArraySpecifiers getArraySpecifiers() {
        return arraySpecifiers;
    }

    @Override
    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    @Override
    public boolean isConstant() {
        Node initializer = getInitializer();
        if (initializer != null && type.getQualifiers().contains(StorageQualifier.CONST)) {
            return HasConstState.isConst(initializer);
        }
        return false;
    }

    @Override
    public ParentNode clone(ParentNode newParent) {
        final VariableDeclarationNode node = new VariableDeclarationNode(getSourcePosition(), newParent, builtIn, type, identifier, arraySpecifiers, null);
        node.setInitializer(CloneUtils.clone(getInitializer(), node));
        return node;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitVariableDeclaration(this);
    }

    @Override
    public GLSLType getType() {
        return type.getType();
    }

    @Override
    public String toString() {
        return "VariableDeclaration('" + identifier + "')";
    }
}

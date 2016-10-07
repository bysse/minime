package com.tazadum.glsl.ast.variable;

import com.tazadum.glsl.ast.FixedChildParentNode;
import com.tazadum.glsl.ast.ParentNode;
import com.tazadum.glsl.util.CloneUtils;

/**
 * Created by Erik on 2016-10-07.
 */
public class FieldSelectionNode extends FixedChildParentNode {
    private String selection;

    public FieldSelectionNode(String selection) {
        this(null, selection);
    }

    public FieldSelectionNode(ParentNode parentNode, String selection) {
        super(1, parentNode);
        this.selection = selection;
    }

    public String getSelection() {
        return selection;
    }

    @Override
    public FieldSelectionNode clone(ParentNode newParent) {
        return CloneUtils.cloneChildren(this, new FieldSelectionNode(newParent, selection));
    }
}

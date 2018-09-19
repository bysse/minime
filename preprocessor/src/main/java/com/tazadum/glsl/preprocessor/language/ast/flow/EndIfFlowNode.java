package com.tazadum.glsl.preprocessor.language.ast.flow;

import com.tazadum.glsl.preprocessor.language.Flow;
import com.tazadum.glsl.preprocessor.language.ast.BaseNode;
import com.tazadum.glsl.preprocessor.language.DeclarationType;
import com.tazadum.glsl.util.SourcePosition;

/**
 * Created by erikb on 2018-09-17.
 */
public class EndIfFlowNode extends BaseNode implements Flow {

    public EndIfFlowNode(SourcePosition sourcePosition) {
        super(sourcePosition);
    }

    @Override
    public DeclarationType getDeclarationType() {
        return DeclarationType.END_IF;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public String toString() {
        return "#endif";
    }
}

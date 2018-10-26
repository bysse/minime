package com.tazadum.glsl.language.type;

import com.tazadum.glsl.exception.NoSuchFieldException;
import com.tazadum.glsl.preprocessor.model.HasToken;

import static com.tazadum.glsl.exception.Errors.Coarse.NO_SUCH_FIELD;

/**
 * Created by erikb on 2018-10-10.
 */
public class UnresolvedType implements GLSLType {
    private String identifier;

    public UnresolvedType(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public GLSLType fieldType(String fieldName) throws NoSuchFieldException {
        throw new NoSuchFieldException(NO_SUCH_FIELD(identifier));
    }

    @Override
    public boolean isAssignableBy(GLSLType type) {
        return false;
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public GLSLType baseType() {
        return null;
    }

    @Override
    public String token() {
        return identifier;
    }

    @Override
    public int tokenId() {
        return HasToken.NO_TOKEN_ID;
    }
}

package com.tazadum.glsl.language.type;

import com.tazadum.glsl.exception.NoSuchFieldException;

/**
 * Created by erikb on 2018-10-10.
 */
public class StructType implements GLSLType {


    @Override
    public GLSLType fieldType(String fieldName) throws NoSuchFieldException {
        return null;
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
        return null;
    }

    @Override
    public int tokenId() {
        return 0;
    }
}

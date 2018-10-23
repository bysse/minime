package com.tazadum.glsl.language.function;

import com.tazadum.glsl.language.ast.Identifier;
import com.tazadum.glsl.language.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.language.type.GLSLType;

/**
 * Created by erikb on 2018-10-23.
 */
public interface BuiltInFunctionRegistry {
    /**
     * Returns an utility class for defining built-in functions.
     */
    FunctionDeclarator getFunctionDeclarator();

    /**
     * Searches for a function prototype based on the parameters and identifier.
     *
     * @param identifier Identifier of the function
     * @param parameters The parameter types.
     * @return A FunctionPrototypeNode or null if no function was found.
     */
    FunctionPrototypeNode resolve(Identifier identifier, GLSLType... parameters);

    FunctionPrototypeNode resolve(Identifier identifier, FunctionPrototypeMatcher prototypeMatcher);


    interface FunctionDeclarator {
        void function(FunctionPrototypeNode node);

        void function(String identifier, Object... parameterTypes);
    }
}

package com.tazadum.glsl.preprocessor.language;

import com.tazadum.glsl.preprocessor.model.HasToken;

public enum ExtensionBehavior implements HasToken {
    /**
     * Give a compile-time error on the #extension if the extension extension_name is not
     * supported, or if all is specified.
     */
    REQUIRE("require"),
    /**
     * Warn on the #extension if the extension extension_name is not supported.
     * Give a compile-time error on the #extension if all is specified.
     */
    ENABLE("enable"),
    /**
     * Behave (including issuing errors and warnings) as if the extension extension_name is
     * not part of the language definition. If all is specified, then behavior must revert
     * back to that of the non-extended core version of the language being compiled to.
     * Warn on the #extension if the extension extension_name is not supported.
     */
    DISABLE("disable"),
    /**
     * Behave as specified by the extension extension_name, except issue warnings on any
     * detectable use of that extension, unless such use is supported by other enabled or
     * required extensions. If all is specified, then warn on all detectable uses of any
     * extension used. Warn on the #extension if the extension extension_name is not supported.
     */
    WARN("warn");

    private final String token;

    ExtensionBehavior(String token) {
        this.token = token;
    }

    @Override
    public String token() {
        return token;
    }
}

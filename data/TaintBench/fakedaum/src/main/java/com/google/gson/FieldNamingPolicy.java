package com.google.gson;

import com.google.gson.ModifyFirstLetterNamingPolicy.LetterModifier;

public enum FieldNamingPolicy {
    UPPER_CAMEL_CASE(new ModifyFirstLetterNamingPolicy(LetterModifier.UPPER)),
    UPPER_CAMEL_CASE_WITH_SPACES(new UpperCamelCaseSeparatorNamingPolicy(" ")),
    LOWER_CASE_WITH_UNDERSCORES(new LowerCamelCaseSeparatorNamingPolicy("_")),
    LOWER_CASE_WITH_DASHES(new LowerCamelCaseSeparatorNamingPolicy("-"));
    
    private final FieldNamingStrategy2 namingPolicy;

    private FieldNamingPolicy(FieldNamingStrategy2 namingPolicy) {
        this.namingPolicy = namingPolicy;
    }

    /* access modifiers changed from: 0000 */
    public FieldNamingStrategy2 getFieldNamingPolicy() {
        return this.namingPolicy;
    }
}

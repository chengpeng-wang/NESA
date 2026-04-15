package com.google.gson;

final class LowerCamelCaseSeparatorNamingPolicy extends CompositionFieldNamingPolicy {
    public LowerCamelCaseSeparatorNamingPolicy(String separatorString) {
        super(new CamelCaseSeparatorNamingPolicy(separatorString), new LowerCaseNamingPolicy());
    }
}

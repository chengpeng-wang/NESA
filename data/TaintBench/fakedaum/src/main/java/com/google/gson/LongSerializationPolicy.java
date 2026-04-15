package com.google.gson;

public enum LongSerializationPolicy {
    DEFAULT(new DefaultStrategy()),
    STRING(new StringStrategy());
    
    private final Strategy strategy;

    private interface Strategy {
        JsonElement serialize(Long l);
    }

    private static class DefaultStrategy implements Strategy {
        private DefaultStrategy() {
        }

        public JsonElement serialize(Long value) {
            return new JsonPrimitive((Number) value);
        }
    }

    private static class StringStrategy implements Strategy {
        private StringStrategy() {
        }

        public JsonElement serialize(Long value) {
            return new JsonPrimitive(String.valueOf(value));
        }
    }

    private LongSerializationPolicy(Strategy strategy) {
        this.strategy = strategy;
    }

    public JsonElement serialize(Long value) {
        return this.strategy.serialize(value);
    }
}

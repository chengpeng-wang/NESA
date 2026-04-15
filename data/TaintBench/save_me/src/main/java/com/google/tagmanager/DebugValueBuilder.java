package com.google.tagmanager;

import com.google.analytics.containertag.proto.MutableDebug.MacroEvaluationInfo;
import com.google.analytics.midtier.proto.containertag.MutableTypeSystem.Value;
import com.google.analytics.midtier.proto.containertag.MutableTypeSystem.Value.Type;
import com.google.analytics.midtier.proto.containertag.TypeSystem;

class DebugValueBuilder implements ValueBuilder {
    private Value value;

    private static class TypeMismatchException extends IllegalStateException {
        public TypeMismatchException(String operation, Type t) {
            super("Attempted operation: " + operation + " on object of type: " + t);
        }
    }

    public DebugValueBuilder(Value value) {
        this.value = value;
    }

    private void validateType(Type expected, Type actual, String message) {
        if (!expected.equals(actual)) {
            throw new TypeMismatchException(message, actual);
        }
    }

    public static Value copyImmutableValue(TypeSystem.Value value) {
        Value result = Value.newMessage();
        if (!result.mergeFrom(value.toByteArray())) {
            Log.e("Failed to copy runtime value into debug value");
        }
        return result;
    }

    public ValueBuilder getListItem(int index) {
        validateType(Type.LIST, this.value.getType(), "add new list item");
        return new DebugValueBuilder(this.value.getListItem(index));
    }

    public ValueBuilder getMapKey(int index) {
        validateType(Type.MAP, this.value.getType(), "add new map key");
        return new DebugValueBuilder(this.value.getMapKey(index));
    }

    public ValueBuilder getMapValue(int index) {
        validateType(Type.MAP, this.value.getType(), "add new map value");
        return new DebugValueBuilder(this.value.getMapValue(index));
    }

    public ValueBuilder getTemplateToken(int index) {
        validateType(Type.TEMPLATE, this.value.getType(), "add template token");
        return new DebugValueBuilder(this.value.getTemplateToken(index));
    }

    public MacroEvaluationInfoBuilder createValueMacroEvaluationInfoExtension() {
        validateType(Type.MACRO_REFERENCE, this.value.getType(), "set macro evaluation extension");
        return new DebugMacroEvaluationInfoBuilder((MacroEvaluationInfo) this.value.getMutableExtension(MacroEvaluationInfo.macro));
    }
}

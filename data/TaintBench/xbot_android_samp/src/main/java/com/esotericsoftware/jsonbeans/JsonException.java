package com.esotericsoftware.jsonbeans;

import org.objectweb.asm.Opcodes;

public class JsonException extends RuntimeException {
    private StringBuffer trace;

    public JsonException(String str, Throwable th) {
        super(str, th);
    }

    public JsonException(String str) {
        super(str);
    }

    public JsonException(Throwable th) {
        super("", th);
    }

    public boolean causedBy(Class cls) {
        return causedBy(this, cls);
    }

    private boolean causedBy(Throwable th, Class cls) {
        Throwable cause = th.getCause();
        if (cause == null || cause == th) {
            return false;
        }
        if (cls.isAssignableFrom(cause.getClass())) {
            return true;
        }
        return causedBy(cause, cls);
    }

    public String getMessage() {
        if (this.trace == null) {
            return super.getMessage();
        }
        StringBuffer stringBuffer = new StringBuffer(Opcodes.ACC_INTERFACE);
        stringBuffer.append(super.getMessage());
        if (stringBuffer.length() > 0) {
            stringBuffer.append(10);
        }
        stringBuffer.append("Serialization trace:");
        stringBuffer.append(this.trace);
        return stringBuffer.toString();
    }

    public void addTrace(String str) {
        if (str == null) {
            throw new IllegalArgumentException("info cannot be null.");
        }
        if (this.trace == null) {
            this.trace = new StringBuffer(Opcodes.ACC_INTERFACE);
        }
        this.trace.append(10);
        this.trace.append(str);
    }
}

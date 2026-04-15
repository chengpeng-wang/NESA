package org.springframework.beans;

import org.springframework.core.NestedRuntimeException;
import org.springframework.util.ObjectUtils;

public abstract class BeansException extends NestedRuntimeException {
    private static final long serialVersionUID = 1;

    public BeansException(String msg) {
        super(msg);
    }

    public BeansException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof BeansException)) {
            return false;
        }
        BeansException otherBe = (BeansException) other;
        if (getMessage().equals(otherBe.getMessage()) && ObjectUtils.nullSafeEquals(getCause(), otherBe.getCause())) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return getMessage().hashCode();
    }
}

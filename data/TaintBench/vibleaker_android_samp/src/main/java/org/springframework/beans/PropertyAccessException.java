package org.springframework.beans;

import java.beans.PropertyChangeEvent;
import org.springframework.core.ErrorCoded;

public abstract class PropertyAccessException extends BeansException implements ErrorCoded {
    private static final long serialVersionUID = 1;
    private transient PropertyChangeEvent propertyChangeEvent;

    public PropertyAccessException(PropertyChangeEvent propertyChangeEvent, String msg, Throwable cause) {
        super(msg, cause);
        this.propertyChangeEvent = propertyChangeEvent;
    }

    public PropertyAccessException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public PropertyChangeEvent getPropertyChangeEvent() {
        return this.propertyChangeEvent;
    }

    public String getPropertyName() {
        return this.propertyChangeEvent != null ? this.propertyChangeEvent.getPropertyName() : null;
    }

    public Object getValue() {
        return this.propertyChangeEvent != null ? this.propertyChangeEvent.getNewValue() : null;
    }
}

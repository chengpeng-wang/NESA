package org.objenesis.instantiator.basic;

import org.objenesis.ObjenesisException;
import org.objenesis.instantiator.ObjectInstantiator;

public class NewInstanceInstantiator implements ObjectInstantiator {
    private final Class type;

    public NewInstanceInstantiator(Class type) {
        this.type = type;
    }

    public Object newInstance() {
        try {
            return this.type.newInstance();
        } catch (Exception e) {
            throw new ObjenesisException(e);
        }
    }
}

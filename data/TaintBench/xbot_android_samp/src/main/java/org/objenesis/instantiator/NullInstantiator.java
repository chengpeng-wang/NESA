package org.objenesis.instantiator;

public class NullInstantiator implements ObjectInstantiator {
    public Object newInstance() {
        return null;
    }
}

package org.objenesis.instantiator.basic;

public class AccessibleInstantiator extends ConstructorInstantiator {
    public AccessibleInstantiator(Class type) {
        super(type);
        if (this.constructor != null) {
            this.constructor.setAccessible(true);
        }
    }
}

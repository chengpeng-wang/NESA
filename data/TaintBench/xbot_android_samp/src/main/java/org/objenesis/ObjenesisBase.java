package org.objenesis;

import java.util.HashMap;
import java.util.Map;
import org.objenesis.instantiator.ObjectInstantiator;
import org.objenesis.strategy.InstantiatorStrategy;

public class ObjenesisBase implements Objenesis {
    protected Map cache;
    protected final InstantiatorStrategy strategy;

    public ObjenesisBase(InstantiatorStrategy strategy) {
        this(strategy, true);
    }

    public ObjenesisBase(InstantiatorStrategy strategy, boolean useCache) {
        if (strategy == null) {
            throw new IllegalArgumentException("A strategy can't be null");
        }
        this.strategy = strategy;
        this.cache = useCache ? new HashMap() : null;
    }

    public String toString() {
        return new StringBuffer().append(getClass().getName()).append(" using ").append(this.strategy.getClass().getName()).append(this.cache == null ? " without" : " with").append(" caching").toString();
    }

    public Object newInstance(Class clazz) {
        return getInstantiatorOf(clazz).newInstance();
    }

    public synchronized ObjectInstantiator getInstantiatorOf(Class clazz) {
        ObjectInstantiator instantiator;
        if (this.cache == null) {
            instantiator = this.strategy.newInstantiatorOf(clazz);
        } else {
            instantiator = (ObjectInstantiator) this.cache.get(clazz.getName());
            if (instantiator == null) {
                instantiator = this.strategy.newInstantiatorOf(clazz);
                this.cache.put(clazz.getName(), instantiator);
            }
        }
        return instantiator;
    }
}

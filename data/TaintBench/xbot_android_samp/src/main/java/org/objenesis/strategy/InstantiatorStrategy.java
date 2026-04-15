package org.objenesis.strategy;

import org.objenesis.instantiator.ObjectInstantiator;

public interface InstantiatorStrategy {
    ObjectInstantiator newInstantiatorOf(Class cls);
}

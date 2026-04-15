package org.objenesis;

import org.objenesis.instantiator.ObjectInstantiator;

public interface Objenesis {
    ObjectInstantiator getInstantiatorOf(Class cls);

    Object newInstance(Class cls);
}

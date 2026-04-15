package org.springframework.core;

import java.util.Map;

@Deprecated
public interface ConcurrentMap extends Map {
    Object putIfAbsent(Object obj, Object obj2);

    boolean remove(Object obj, Object obj2);

    Object replace(Object obj, Object obj2);

    boolean replace(Object obj, Object obj2, Object obj3);
}

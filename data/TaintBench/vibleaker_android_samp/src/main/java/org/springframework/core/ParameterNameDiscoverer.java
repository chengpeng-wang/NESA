package org.springframework.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public interface ParameterNameDiscoverer {
    String[] getParameterNames(Constructor<?> constructor);

    String[] getParameterNames(Method method);
}

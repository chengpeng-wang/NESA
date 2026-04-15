package org.springframework.core;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

public abstract class BridgeMethodResolver {
    public static Method findBridgedMethod(Method bridgeMethod) {
        if (bridgeMethod == null || !bridgeMethod.isBridge()) {
            return bridgeMethod;
        }
        List<Method> candidateMethods = new ArrayList();
        for (Method candidateMethod : ReflectionUtils.getAllDeclaredMethods(bridgeMethod.getDeclaringClass())) {
            if (isBridgedCandidateFor(candidateMethod, bridgeMethod)) {
                candidateMethods.add(candidateMethod);
            }
        }
        if (candidateMethods.size() == 1) {
            return (Method) candidateMethods.get(0);
        }
        Method bridgedMethod = searchCandidates(candidateMethods, bridgeMethod);
        if (bridgedMethod != null) {
            return bridgedMethod;
        }
        return bridgeMethod;
    }

    private static Method searchCandidates(List<Method> candidateMethods, Method bridgeMethod) {
        if (candidateMethods.isEmpty()) {
            return null;
        }
        Map<TypeVariable, Type> typeParameterMap = GenericTypeResolver.getTypeVariableMap(bridgeMethod.getDeclaringClass());
        Method previousMethod = null;
        boolean sameSig = true;
        for (Method candidateMethod : candidateMethods) {
            if (isBridgeMethodFor(bridgeMethod, candidateMethod, typeParameterMap)) {
                return candidateMethod;
            }
            if (previousMethod != null) {
                if (sameSig && Arrays.equals(candidateMethod.getGenericParameterTypes(), previousMethod.getGenericParameterTypes())) {
                    sameSig = true;
                } else {
                    sameSig = false;
                }
            }
            previousMethod = candidateMethod;
        }
        return sameSig ? (Method) candidateMethods.get(0) : null;
    }

    private static boolean isBridgedCandidateFor(Method candidateMethod, Method bridgeMethod) {
        return !candidateMethod.isBridge() && !candidateMethod.equals(bridgeMethod) && candidateMethod.getName().equals(bridgeMethod.getName()) && candidateMethod.getParameterTypes().length == bridgeMethod.getParameterTypes().length;
    }

    static boolean isBridgeMethodFor(Method bridgeMethod, Method candidateMethod, Map<TypeVariable, Type> typeVariableMap) {
        if (isResolvedTypeMatch(candidateMethod, bridgeMethod, typeVariableMap)) {
            return true;
        }
        Method method = findGenericDeclaration(bridgeMethod);
        if (method == null || !isResolvedTypeMatch(method, candidateMethod, typeVariableMap)) {
            return false;
        }
        return true;
    }

    private static Method findGenericDeclaration(Method bridgeMethod) {
        Method method;
        Class superclass = bridgeMethod.getDeclaringClass().getSuperclass();
        while (superclass != null && !Object.class.equals(superclass)) {
            method = searchForMatch(superclass, bridgeMethod);
            if (method != null && !method.isBridge()) {
                return method;
            }
            superclass = superclass.getSuperclass();
        }
        for (Class ifc : ClassUtils.getAllInterfacesForClass(bridgeMethod.getDeclaringClass())) {
            method = searchForMatch(ifc, bridgeMethod);
            if (method != null && !method.isBridge()) {
                return method;
            }
        }
        return null;
    }

    private static boolean isResolvedTypeMatch(Method genericMethod, Method candidateMethod, Map<TypeVariable, Type> typeVariableMap) {
        Type[] genericParameters = genericMethod.getGenericParameterTypes();
        Class[] candidateParameters = candidateMethod.getParameterTypes();
        if (genericParameters.length != candidateParameters.length) {
            return false;
        }
        for (int i = 0; i < genericParameters.length; i++) {
            Type genericParameter = genericParameters[i];
            Class candidateParameter = candidateParameters[i];
            if (candidateParameter.isArray()) {
                Type rawType = GenericTypeResolver.getRawType(genericParameter, typeVariableMap);
                if (rawType instanceof GenericArrayType) {
                    if (!candidateParameter.getComponentType().equals(GenericTypeResolver.resolveType(((GenericArrayType) rawType).getGenericComponentType(), typeVariableMap))) {
                        return false;
                    }
                    return true;
                }
            }
            if (!candidateParameter.equals(GenericTypeResolver.resolveType(genericParameter, typeVariableMap))) {
                return false;
            }
        }
        return true;
    }

    private static Method searchForMatch(Class type, Method bridgeMethod) {
        return ReflectionUtils.findMethod(type, bridgeMethod.getName(), bridgeMethod.getParameterTypes());
    }

    public static boolean isVisibilityBridgeMethodPair(Method bridgeMethod, Method bridgedMethod) {
        if (bridgeMethod == bridgedMethod) {
            return true;
        }
        if (Arrays.equals(bridgeMethod.getParameterTypes(), bridgedMethod.getParameterTypes()) && bridgeMethod.getReturnType().equals(bridgedMethod.getReturnType())) {
            return true;
        }
        return false;
    }
}

package org.springframework.core;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.HashMap;
import java.util.Map;
import org.springframework.util.Assert;
import org.springframework.util.ConcurrentReferenceHashMap;

public abstract class GenericTypeResolver {
    private static final Map<Class, Map<TypeVariable, Type>> typeVariableCache = new ConcurrentReferenceHashMap();

    public static Type getTargetType(MethodParameter methodParam) {
        Assert.notNull(methodParam, "MethodParameter must not be null");
        if (methodParam.getConstructor() != null) {
            return methodParam.getConstructor().getGenericParameterTypes()[methodParam.getParameterIndex()];
        }
        if (methodParam.getParameterIndex() >= 0) {
            return methodParam.getMethod().getGenericParameterTypes()[methodParam.getParameterIndex()];
        }
        return methodParam.getMethod().getGenericReturnType();
    }

    public static Class<?> resolveParameterType(MethodParameter methodParam, Class<?> clazz) {
        Type genericType = getTargetType(methodParam);
        Assert.notNull(clazz, "Class must not be null");
        Map<TypeVariable, Type> typeVariableMap = getTypeVariableMap(clazz);
        Type rawType = getRawType(genericType, typeVariableMap);
        Class<?> result = rawType instanceof Class ? (Class) rawType : methodParam.getParameterType();
        methodParam.setParameterType(result);
        methodParam.typeVariableMap = typeVariableMap;
        return result;
    }

    public static Class<?> resolveReturnType(Method method, Class<?> clazz) {
        Assert.notNull(method, "Method must not be null");
        Type genericType = method.getGenericReturnType();
        Assert.notNull(clazz, "Class must not be null");
        Type rawType = getRawType(genericType, getTypeVariableMap(clazz));
        return rawType instanceof Class ? (Class) rawType : method.getReturnType();
    }

    @Deprecated
    public static Class<?> resolveReturnTypeForGenericMethod(Method method, Object[] args) {
        Assert.notNull(method, "Method must not be null");
        Assert.notNull(args, "Argument array must not be null");
        TypeVariable<Method>[] declaredTypeVariables = method.getTypeParameters();
        Type genericReturnType = method.getGenericReturnType();
        Type[] methodArgumentTypes = method.getGenericParameterTypes();
        if (declaredTypeVariables.length == 0) {
            return method.getReturnType();
        }
        if (args.length < methodArgumentTypes.length) {
            return null;
        }
        int i$;
        int len$;
        boolean locallyDeclaredTypeVariableMatchesReturnType = false;
        for (TypeVariable<Method> currentTypeVariable : declaredTypeVariables) {
            if (currentTypeVariable.equals(genericReturnType)) {
                locallyDeclaredTypeVariableMatchesReturnType = true;
                break;
            }
        }
        if (locallyDeclaredTypeVariableMatchesReturnType) {
            for (int i = 0; i < methodArgumentTypes.length; i++) {
                Type currentMethodArgumentType = methodArgumentTypes[i];
                if (currentMethodArgumentType.equals(genericReturnType)) {
                    return args[i].getClass();
                }
                if (currentMethodArgumentType instanceof ParameterizedType) {
                    Type[] arr$ = ((ParameterizedType) currentMethodArgumentType).getActualTypeArguments();
                    len$ = arr$.length;
                    i$ = 0;
                    while (i$ < len$) {
                        if (!arr$[i$].equals(genericReturnType)) {
                            i$++;
                        } else if (args[i] instanceof Class) {
                            return (Class) args[i];
                        } else {
                            return method.getReturnType();
                        }
                    }
                    continue;
                }
            }
        }
        return method.getReturnType();
    }

    public static Class<?> resolveReturnTypeArgument(Method method, Class<?> genericIfc) {
        Assert.notNull(method, "method must not be null");
        Type returnType = method.getReturnType();
        Type genericReturnType = method.getGenericReturnType();
        if (returnType.equals(genericIfc)) {
            if (!(genericReturnType instanceof ParameterizedType)) {
                return null;
            }
            Type typeArg = ((ParameterizedType) genericReturnType).getActualTypeArguments()[0];
            if (!(typeArg instanceof WildcardType)) {
                return (Class) typeArg;
            }
        }
        return resolveTypeArgument((Class) returnType, genericIfc);
    }

    public static Class<?> resolveTypeArgument(Class<?> clazz, Class<?> genericIfc) {
        Class<?>[] typeArgs = resolveTypeArguments(clazz, genericIfc);
        if (typeArgs == null) {
            return null;
        }
        if (typeArgs.length == 1) {
            return typeArgs[0];
        }
        throw new IllegalArgumentException("Expected 1 type argument on generic interface [" + genericIfc.getName() + "] but found " + typeArgs.length);
    }

    public static Class<?>[] resolveTypeArguments(Class<?> clazz, Class<?> genericIfc) {
        return doResolveTypeArguments((Class) clazz, (Class) clazz, (Class) genericIfc);
    }

    private static Class<?>[] doResolveTypeArguments(Class<?> ownerClass, Class<?> classToIntrospect, Class<?> genericIfc) {
        while (classToIntrospect != null) {
            Class<?>[] result;
            if (genericIfc.isInterface()) {
                for (Type ifc : classToIntrospect.getGenericInterfaces()) {
                    result = doResolveTypeArguments((Class) ownerClass, ifc, (Class) genericIfc);
                    if (result != null) {
                        return result;
                    }
                }
                continue;
            } else {
                try {
                    result = doResolveTypeArguments((Class) ownerClass, classToIntrospect.getGenericSuperclass(), (Class) genericIfc);
                    if (result != null) {
                        return result;
                    }
                } catch (MalformedParameterizedTypeException e) {
                    return null;
                }
            }
            classToIntrospect = classToIntrospect.getSuperclass();
        }
        return null;
    }

    private static Class<?>[] doResolveTypeArguments(Class<?> ownerClass, Type ifc, Class<?> genericIfc) {
        if (ifc instanceof ParameterizedType) {
            ParameterizedType paramIfc = (ParameterizedType) ifc;
            Type rawType = paramIfc.getRawType();
            if (genericIfc.equals(rawType)) {
                Type[] typeArgs = paramIfc.getActualTypeArguments();
                Class<?>[] clsArr = new Class[typeArgs.length];
                for (int i = 0; i < typeArgs.length; i++) {
                    clsArr[i] = extractClass(ownerClass, typeArgs[i]);
                }
                return clsArr;
            } else if (genericIfc.isAssignableFrom((Class) rawType)) {
                return doResolveTypeArguments((Class) ownerClass, (Class) rawType, (Class) genericIfc);
            }
        } else if (ifc != null && genericIfc.isAssignableFrom((Class) ifc)) {
            return doResolveTypeArguments((Class) ownerClass, (Class) ifc, (Class) genericIfc);
        }
        return null;
    }

    private static Class<?> extractClass(Class<?> ownerClass, Type arg) {
        if (arg instanceof ParameterizedType) {
            return extractClass(ownerClass, ((ParameterizedType) arg).getRawType());
        }
        if (arg instanceof GenericArrayType) {
            return Array.newInstance(extractClass(ownerClass, ((GenericArrayType) arg).getGenericComponentType()), 0).getClass();
        }
        if (arg instanceof TypeVariable) {
            TypeVariable tv = (TypeVariable) arg;
            arg = (Type) getTypeVariableMap(ownerClass).get(tv);
            if (arg != null) {
                return extractClass(ownerClass, arg);
            }
            arg = extractBoundForTypeVariable(tv);
            if (arg instanceof ParameterizedType) {
                return extractClass(ownerClass, ((ParameterizedType) arg).getRawType());
            }
        }
        return arg instanceof Class ? (Class) arg : Object.class;
    }

    public static Class<?> resolveType(Type genericType, Map<TypeVariable, Type> typeVariableMap) {
        Type resolvedType = getRawType(genericType, typeVariableMap);
        if (resolvedType instanceof GenericArrayType) {
            resolvedType = Array.newInstance(resolveType(((GenericArrayType) resolvedType).getGenericComponentType(), typeVariableMap), 0).getClass();
        }
        return resolvedType instanceof Class ? (Class) resolvedType : Object.class;
    }

    static Type getRawType(Type genericType, Map<TypeVariable, Type> typeVariableMap) {
        Type resolvedType = genericType;
        if (genericType instanceof TypeVariable) {
            TypeVariable tv = (TypeVariable) genericType;
            resolvedType = (Type) typeVariableMap.get(tv);
            if (resolvedType == null) {
                resolvedType = extractBoundForTypeVariable(tv);
            }
        }
        if (resolvedType instanceof ParameterizedType) {
            return ((ParameterizedType) resolvedType).getRawType();
        }
        return resolvedType;
    }

    public static Map<TypeVariable, Type> getTypeVariableMap(Class<?> clazz) {
        Map<TypeVariable, Type> typeVariableMap = (Map) typeVariableCache.get(clazz);
        if (typeVariableMap == null) {
            Type genericType;
            typeVariableMap = new HashMap();
            extractTypeVariablesFromGenericInterfaces(clazz.getGenericInterfaces(), typeVariableMap);
            Class<?> type = clazz;
            while (type.getSuperclass() != null && !Object.class.equals(type.getSuperclass())) {
                try {
                    genericType = type.getGenericSuperclass();
                    if (genericType instanceof ParameterizedType) {
                        populateTypeMapFromParameterizedType((ParameterizedType) genericType, typeVariableMap);
                    }
                    extractTypeVariablesFromGenericInterfaces(type.getSuperclass().getGenericInterfaces(), typeVariableMap);
                    type = type.getSuperclass();
                } catch (MalformedParameterizedTypeException e) {
                }
            }
            type = clazz;
            while (type.isMemberClass()) {
                try {
                    genericType = type.getGenericSuperclass();
                    if (genericType instanceof ParameterizedType) {
                        populateTypeMapFromParameterizedType((ParameterizedType) genericType, typeVariableMap);
                    }
                    type = type.getEnclosingClass();
                } catch (MalformedParameterizedTypeException e2) {
                }
            }
            typeVariableCache.put(clazz, typeVariableMap);
        }
        return typeVariableMap;
    }

    static Type extractBoundForTypeVariable(TypeVariable typeVariable) {
        Type[] bounds = typeVariable.getBounds();
        if (bounds.length == 0) {
            return Object.class;
        }
        Type bound = bounds[0];
        if (bound instanceof TypeVariable) {
            return extractBoundForTypeVariable((TypeVariable) bound);
        }
        return bound;
    }

    private static void extractTypeVariablesFromGenericInterfaces(Type[] genericInterfaces, Map<TypeVariable, Type> typeVariableMap) {
        for (Type genericInterface : genericInterfaces) {
            if (genericInterface instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) genericInterface;
                populateTypeMapFromParameterizedType(pt, typeVariableMap);
                if (pt.getRawType() instanceof Class) {
                    extractTypeVariablesFromGenericInterfaces(((Class) pt.getRawType()).getGenericInterfaces(), typeVariableMap);
                }
            } else if (genericInterface instanceof Class) {
                extractTypeVariablesFromGenericInterfaces(((Class) genericInterface).getGenericInterfaces(), typeVariableMap);
            }
        }
    }

    private static void populateTypeMapFromParameterizedType(ParameterizedType type, Map<TypeVariable, Type> typeVariableMap) {
        if (type.getRawType() instanceof Class) {
            Type[] actualTypeArguments = type.getActualTypeArguments();
            TypeVariable[] typeVariables = ((Class) type.getRawType()).getTypeParameters();
            for (int i = 0; i < actualTypeArguments.length; i++) {
                Type actualTypeArgument = actualTypeArguments[i];
                TypeVariable variable = typeVariables[i];
                if (actualTypeArgument instanceof Class) {
                    typeVariableMap.put(variable, actualTypeArgument);
                } else if (actualTypeArgument instanceof GenericArrayType) {
                    typeVariableMap.put(variable, actualTypeArgument);
                } else if (actualTypeArgument instanceof ParameterizedType) {
                    typeVariableMap.put(variable, actualTypeArgument);
                } else if (actualTypeArgument instanceof TypeVariable) {
                    TypeVariable typeVariableArgument = (TypeVariable) actualTypeArgument;
                    Type resolvedType = (Type) typeVariableMap.get(typeVariableArgument);
                    if (resolvedType == null) {
                        resolvedType = extractBoundForTypeVariable(typeVariableArgument);
                    }
                    typeVariableMap.put(variable, resolvedType);
                }
            }
        }
    }
}

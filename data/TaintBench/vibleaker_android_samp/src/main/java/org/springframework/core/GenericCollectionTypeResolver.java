package org.springframework.core;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Collection;
import java.util.Map;

public abstract class GenericCollectionTypeResolver {
    public static Class<?> getCollectionType(Class<? extends Collection> collectionClass) {
        return extractTypeFromClass(collectionClass, Collection.class, 0);
    }

    public static Class<?> getMapKeyType(Class<? extends Map> mapClass) {
        return extractTypeFromClass(mapClass, Map.class, 0);
    }

    public static Class<?> getMapValueType(Class<? extends Map> mapClass) {
        return extractTypeFromClass(mapClass, Map.class, 1);
    }

    public static Class<?> getCollectionFieldType(Field collectionField) {
        return getGenericFieldType(collectionField, Collection.class, 0, null, 1);
    }

    public static Class<?> getCollectionFieldType(Field collectionField, int nestingLevel) {
        return getGenericFieldType(collectionField, Collection.class, 0, null, nestingLevel);
    }

    public static Class<?> getCollectionFieldType(Field collectionField, int nestingLevel, Map<Integer, Integer> typeIndexesPerLevel) {
        return getGenericFieldType(collectionField, Collection.class, 0, typeIndexesPerLevel, nestingLevel);
    }

    public static Class<?> getMapKeyFieldType(Field mapField) {
        return getGenericFieldType(mapField, Map.class, 0, null, 1);
    }

    public static Class<?> getMapKeyFieldType(Field mapField, int nestingLevel) {
        return getGenericFieldType(mapField, Map.class, 0, null, nestingLevel);
    }

    public static Class<?> getMapKeyFieldType(Field mapField, int nestingLevel, Map<Integer, Integer> typeIndexesPerLevel) {
        return getGenericFieldType(mapField, Map.class, 0, typeIndexesPerLevel, nestingLevel);
    }

    public static Class<?> getMapValueFieldType(Field mapField) {
        return getGenericFieldType(mapField, Map.class, 1, null, 1);
    }

    public static Class<?> getMapValueFieldType(Field mapField, int nestingLevel) {
        return getGenericFieldType(mapField, Map.class, 1, null, nestingLevel);
    }

    public static Class<?> getMapValueFieldType(Field mapField, int nestingLevel, Map<Integer, Integer> typeIndexesPerLevel) {
        return getGenericFieldType(mapField, Map.class, 1, typeIndexesPerLevel, nestingLevel);
    }

    public static Class<?> getCollectionParameterType(MethodParameter methodParam) {
        return getGenericParameterType(methodParam, Collection.class, 0);
    }

    public static Class<?> getMapKeyParameterType(MethodParameter methodParam) {
        return getGenericParameterType(methodParam, Map.class, 0);
    }

    public static Class<?> getMapValueParameterType(MethodParameter methodParam) {
        return getGenericParameterType(methodParam, Map.class, 1);
    }

    public static Class<?> getCollectionReturnType(Method method) {
        return getGenericReturnType(method, Collection.class, 0, 1);
    }

    public static Class<?> getCollectionReturnType(Method method, int nestingLevel) {
        return getGenericReturnType(method, Collection.class, 0, nestingLevel);
    }

    public static Class<?> getMapKeyReturnType(Method method) {
        return getGenericReturnType(method, Map.class, 0, 1);
    }

    public static Class<?> getMapKeyReturnType(Method method, int nestingLevel) {
        return getGenericReturnType(method, Map.class, 0, nestingLevel);
    }

    public static Class<?> getMapValueReturnType(Method method) {
        return getGenericReturnType(method, Map.class, 1, 1);
    }

    public static Class<?> getMapValueReturnType(Method method, int nestingLevel) {
        return getGenericReturnType(method, Map.class, 1, nestingLevel);
    }

    private static Class<?> getGenericParameterType(MethodParameter methodParam, Class<?> source, int typeIndex) {
        return extractType(GenericTypeResolver.getTargetType(methodParam), source, typeIndex, methodParam.typeVariableMap, methodParam.typeIndexesPerLevel, methodParam.getNestingLevel(), 1);
    }

    private static Class<?> getGenericFieldType(Field field, Class<?> source, int typeIndex, Map<Integer, Integer> typeIndexesPerLevel, int nestingLevel) {
        return extractType(field.getGenericType(), source, typeIndex, null, typeIndexesPerLevel, nestingLevel, 1);
    }

    private static Class<?> getGenericReturnType(Method method, Class<?> source, int typeIndex, int nestingLevel) {
        return extractType(method.getGenericReturnType(), source, typeIndex, null, null, nestingLevel, 1);
    }

    private static Class<?> extractType(Type type, Class<?> source, int typeIndex, Map<TypeVariable, Type> typeVariableMap, Map<Integer, Integer> typeIndexesPerLevel, int nestingLevel, int currentLevel) {
        Type resolvedType = type;
        if ((type instanceof TypeVariable) && typeVariableMap != null) {
            Type mappedType = (Type) typeVariableMap.get(type);
            if (mappedType != null) {
                resolvedType = mappedType;
            }
        }
        if (resolvedType instanceof ParameterizedType) {
            return extractTypeFromParameterizedType((ParameterizedType) resolvedType, source, typeIndex, typeVariableMap, typeIndexesPerLevel, nestingLevel, currentLevel);
        }
        if (resolvedType instanceof Class) {
            return extractTypeFromClass((Class) resolvedType, source, typeIndex, typeVariableMap, typeIndexesPerLevel, nestingLevel, currentLevel);
        }
        if (!(resolvedType instanceof GenericArrayType)) {
            return null;
        }
        return extractType(((GenericArrayType) resolvedType).getGenericComponentType(), source, typeIndex, typeVariableMap, typeIndexesPerLevel, nestingLevel, currentLevel + 1);
    }

    private static Class<?> extractTypeFromParameterizedType(ParameterizedType ptype, Class<?> source, int typeIndex, Map<TypeVariable, Type> typeVariableMap, Map<Integer, Integer> typeIndexesPerLevel, int nestingLevel, int currentLevel) {
        if (!(ptype.getRawType() instanceof Class)) {
            return null;
        }
        Class rawType = (Class) ptype.getRawType();
        Type[] paramTypes = ptype.getActualTypeArguments();
        if (nestingLevel - currentLevel > 0) {
            int nextLevel = currentLevel + 1;
            Integer currentTypeIndex = typeIndexesPerLevel != null ? (Integer) typeIndexesPerLevel.get(Integer.valueOf(nextLevel)) : null;
            return extractType(paramTypes[currentTypeIndex != null ? currentTypeIndex.intValue() : paramTypes.length - 1], source, typeIndex, typeVariableMap, typeIndexesPerLevel, nestingLevel, nextLevel);
        } else if (source != null && !source.isAssignableFrom(rawType)) {
            return null;
        } else {
            Class<?> fromSuperclassOrInterface = extractTypeFromClass(rawType, source, typeIndex, typeVariableMap, typeIndexesPerLevel, nestingLevel, currentLevel);
            if (fromSuperclassOrInterface != null) {
                return fromSuperclassOrInterface;
            }
            if (paramTypes == null || typeIndex >= paramTypes.length) {
                return null;
            }
            Type paramType = paramTypes[typeIndex];
            if ((paramType instanceof TypeVariable) && typeVariableMap != null) {
                Type mappedType = (Type) typeVariableMap.get(paramType);
                if (mappedType != null) {
                    paramType = mappedType;
                }
            }
            if (paramType instanceof WildcardType) {
                WildcardType wildcardType = (WildcardType) paramType;
                Type[] upperBounds = wildcardType.getUpperBounds();
                if (upperBounds == null || upperBounds.length <= 0 || Object.class.equals(upperBounds[0])) {
                    Type[] lowerBounds = wildcardType.getLowerBounds();
                    if (!(lowerBounds == null || lowerBounds.length <= 0 || Object.class.equals(lowerBounds[0]))) {
                        paramType = lowerBounds[0];
                    }
                } else {
                    paramType = upperBounds[0];
                }
            }
            if (paramType instanceof ParameterizedType) {
                paramType = ((ParameterizedType) paramType).getRawType();
            }
            if (paramType instanceof GenericArrayType) {
                Type compType = ((GenericArrayType) paramType).getGenericComponentType();
                if (compType instanceof Class) {
                    return Array.newInstance((Class) compType, 0).getClass();
                }
            } else if (paramType instanceof Class) {
                return (Class) paramType;
            }
            return null;
        }
    }

    private static Class<?> extractTypeFromClass(Class<?> clazz, Class<?> source, int typeIndex) {
        return extractTypeFromClass(clazz, source, typeIndex, null, null, 1, 1);
    }

    private static Class<?> extractTypeFromClass(Class<?> clazz, Class<?> source, int typeIndex, Map<TypeVariable, Type> typeVariableMap, Map<Integer, Integer> typeIndexesPerLevel, int nestingLevel, int currentLevel) {
        if (clazz.getName().startsWith("java.util.")) {
            return null;
        }
        if (clazz.getSuperclass() != null && isIntrospectionCandidate(clazz.getSuperclass())) {
            try {
                return extractType(clazz.getGenericSuperclass(), source, typeIndex, typeVariableMap, typeIndexesPerLevel, nestingLevel, currentLevel);
            } catch (MalformedParameterizedTypeException e) {
            }
        }
        Type[] ifcs = clazz.getGenericInterfaces();
        if (ifcs != null) {
            for (Type ifc : ifcs) {
                Type rawType = ifc;
                if (ifc instanceof ParameterizedType) {
                    rawType = ((ParameterizedType) ifc).getRawType();
                }
                if ((rawType instanceof Class) && isIntrospectionCandidate((Class) rawType)) {
                    return extractType(ifc, source, typeIndex, typeVariableMap, typeIndexesPerLevel, nestingLevel, currentLevel);
                }
            }
        }
        return null;
    }

    private static boolean isIntrospectionCandidate(Class clazz) {
        return Collection.class.isAssignableFrom(clazz) || Map.class.isAssignableFrom(clazz);
    }
}

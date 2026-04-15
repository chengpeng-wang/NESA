package org.apache.log4j.lf5.util;

import java.io.InputStream;
import java.net.URL;

public class ResourceUtils {
    public static InputStream getResourceAsStream(Object object, Resource resource) {
        ClassLoader loader = object.getClass().getClassLoader();
        if (loader != null) {
            return loader.getResourceAsStream(resource.getName());
        }
        return ClassLoader.getSystemResourceAsStream(resource.getName());
    }

    public static URL getResourceAsURL(Object object, Resource resource) {
        ClassLoader loader = object.getClass().getClassLoader();
        if (loader != null) {
            return loader.getResource(resource.getName());
        }
        return ClassLoader.getSystemResource(resource.getName());
    }
}

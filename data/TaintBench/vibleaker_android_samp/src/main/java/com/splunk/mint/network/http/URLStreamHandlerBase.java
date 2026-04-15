package com.splunk.mint.network.http;

import com.splunk.mint.Logger;
import com.splunk.mint.network.util.ReflectionUtil;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public abstract class URLStreamHandlerBase extends URLStreamHandler {
    static final Class<?>[] PROXY_FIELD_TYPES = new Class[]{URL.class, Integer.TYPE, Proxy.class};
    static final Class<?>[] SIMPLE_FIELD_TYPES = new Class[]{URL.class, Integer.TYPE};
    private Constructor<?> proxyConstructor;
    private Constructor<?> simpleConstructor;

    public abstract int getDefaultPort();

    public abstract String getProtocol();

    public URLStreamHandlerBase(String[] factoryClasses) throws ClassNotFoundException {
        initConstructors(factoryClasses);
        if (this.proxyConstructor == null || this.simpleConstructor == null) {
            throw new ClassNotFoundException("No implementation detected");
        }
    }

    private void initConstructors(String[] factoryClasses) {
        String[] arr$ = factoryClasses;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            String className = arr$[i$];
            try {
                this.proxyConstructor = ReflectionUtil.findConstructor(className, PROXY_FIELD_TYPES);
                if (this.proxyConstructor != null) {
                }
                this.simpleConstructor = ReflectionUtil.findConstructor(className, SIMPLE_FIELD_TYPES);
                if (this.simpleConstructor != null) {
                }
                this.proxyConstructor.setAccessible(true);
                this.simpleConstructor.setAccessible(true);
                return;
            } catch (ClassNotFoundException e) {
                this.proxyConstructor = null;
                this.simpleConstructor = null;
                i$++;
            }
        }
    }

    /* access modifiers changed from: protected */
    public URLConnection openConnection(URL url) throws IOException {
        try {
            return (URLConnection) this.simpleConstructor.newInstance(new Object[]{url, Integer.valueOf(getDefaultPort())});
        } catch (InstantiationException e) {
            Logger.logError("Error initializing connection - can't instantiate object: " + e.getMessage());
            throw new IOException();
        } catch (IllegalAccessException e2) {
            Logger.logError("Error initializing connection - can't access constructor: " + e2.getMessage());
            throw new IOException();
        } catch (IllegalArgumentException e3) {
            Logger.logError("Error initializing connection - invalid argument: " + e3.getMessage());
            throw new IOException();
        } catch (InvocationTargetException e4) {
            Logger.logError("Error initializing connection - can't invoke target: " + e4.getMessage());
            throw new IOException();
        }
    }

    /* access modifiers changed from: protected */
    public URLConnection openConnection(URL url, Proxy proxy) throws IOException {
        if (proxy == null) {
            return openConnection(url);
        }
        try {
            return (URLConnection) this.proxyConstructor.newInstance(new Object[]{url, Integer.valueOf(getDefaultPort()), proxy});
        } catch (InstantiationException e) {
            Logger.logError("Error initializing connection - can't instantiate object: " + e.getMessage());
            throw new IOException();
        } catch (IllegalAccessException e2) {
            Logger.logError("Error initializing connection - can't access constructor: " + e2.getMessage());
            throw new IOException();
        } catch (IllegalArgumentException e3) {
            Logger.logError("Error initializing connection - invalid argument: " + e3.getMessage());
            throw new IOException();
        } catch (InvocationTargetException e4) {
            Logger.logError("Error initializing connection - can't invoke target: " + e4.getMessage());
            throw new IOException();
        }
    }
}

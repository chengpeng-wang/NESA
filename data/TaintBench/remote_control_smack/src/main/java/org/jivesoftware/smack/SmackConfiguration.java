package org.jivesoftware.smack;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;

public final class SmackConfiguration {
    private static final String SMACK_VERSION = "3.0.0";
    private static int keepAliveInterval;
    private static int packetReplyTimeout;

    static {
        packetReplyTimeout = 5000;
        keepAliveInterval = 30000;
        try {
            for (ClassLoader classLoader : getClassLoaders()) {
                Enumeration configEnum = classLoader.getResources("META-INF/smack-config.xml");
                while (configEnum.hasMoreElements()) {
                    InputStream systemStream = null;
                    try {
                        systemStream = ((URL) configEnum.nextElement()).openStream();
                        XmlPullParser parser = new KXmlParser();
                        parser.setFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces", true);
                        parser.setInput(systemStream, StringEncodings.UTF8);
                        int eventType = parser.getEventType();
                        do {
                            if (eventType == 2) {
                                if (parser.getName().equals("className")) {
                                    parseClassToLoad(parser);
                                } else if (parser.getName().equals("packetReplyTimeout")) {
                                    packetReplyTimeout = parseIntProperty(parser, packetReplyTimeout);
                                } else if (parser.getName().equals("keepAliveInterval")) {
                                    keepAliveInterval = parseIntProperty(parser, keepAliveInterval);
                                }
                            }
                            eventType = parser.next();
                        } while (eventType != 1);
                        try {
                            systemStream.close();
                        } catch (Exception e) {
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        try {
                            systemStream.close();
                        } catch (Exception e3) {
                        }
                    } catch (Throwable th) {
                        try {
                            systemStream.close();
                        } catch (Exception e4) {
                        }
                        throw th;
                    }
                }
            }
        } catch (Exception e22) {
            e22.printStackTrace();
        }
    }

    private SmackConfiguration() {
    }

    public static String getVersion() {
        return SMACK_VERSION;
    }

    public static int getPacketReplyTimeout() {
        if (packetReplyTimeout <= 0) {
            packetReplyTimeout = 5000;
        }
        return packetReplyTimeout;
    }

    public static void setPacketReplyTimeout(int timeout) {
        if (timeout <= 0) {
            throw new IllegalArgumentException();
        }
        packetReplyTimeout = timeout;
    }

    public static int getKeepAliveInterval() {
        return keepAliveInterval;
    }

    public static void setKeepAliveInterval(int interval) {
        keepAliveInterval = interval;
    }

    private static void parseClassToLoad(XmlPullParser parser) throws Exception {
        String className = parser.nextText();
        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            System.err.println("Error! A startup class specified in smack-config.xml could not be loaded: " + className);
        }
    }

    private static int parseIntProperty(XmlPullParser parser, int defaultValue) throws Exception {
        try {
            return Integer.parseInt(parser.nextText());
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            return defaultValue;
        }
    }

    private static ClassLoader[] getClassLoaders() {
        ClassLoader[] classLoaders = new ClassLoader[]{SmackConfiguration.class.getClassLoader(), Thread.currentThread().getContextClassLoader()};
        List<ClassLoader> loaders = new ArrayList();
        for (ClassLoader classLoader : classLoaders) {
            if (classLoader != null) {
                loaders.add(classLoader);
            }
        }
        return (ClassLoader[]) loaders.toArray(new ClassLoader[loaders.size()]);
    }
}

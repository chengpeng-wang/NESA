package org.apache.log4j.lf5;

import java.io.IOException;
import java.net.URL;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.spi.Configurator;
import org.apache.log4j.spi.LoggerRepository;

public class DefaultLF5Configurator implements Configurator {
    static Class class$org$apache$log4j$lf5$DefaultLF5Configurator;

    private DefaultLF5Configurator() {
    }

    public static void configure() throws IOException {
        Class class$;
        String resource = "/org/apache/log4j/lf5/config/defaultconfig.properties";
        if (class$org$apache$log4j$lf5$DefaultLF5Configurator == null) {
            class$ = class$("org.apache.log4j.lf5.DefaultLF5Configurator");
            class$org$apache$log4j$lf5$DefaultLF5Configurator = class$;
        } else {
            class$ = class$org$apache$log4j$lf5$DefaultLF5Configurator;
        }
        URL configFileResource = class$.getResource(resource);
        if (configFileResource != null) {
            PropertyConfigurator.configure(configFileResource);
            return;
        }
        throw new IOException(new StringBuffer().append("Error: Unable to open the resource").append(resource).toString());
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    public void doConfigure(URL configURL, LoggerRepository repository) {
        throw new IllegalStateException("This class should NOT be instantiated!");
    }
}

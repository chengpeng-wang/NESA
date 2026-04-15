package org.apache.log4j.spi;

import java.net.URL;

public interface Configurator {
    public static final String INHERITED = "inherited";
    public static final String NULL = "null";

    void doConfigure(URL url, LoggerRepository loggerRepository);
}

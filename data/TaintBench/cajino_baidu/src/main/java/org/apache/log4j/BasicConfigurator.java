package org.apache.log4j;

public class BasicConfigurator {
    protected BasicConfigurator() {
    }

    public static void configure() {
        Logger.getRootLogger().addAppender(new ConsoleAppender(new PatternLayout("%r [%t] %p %c %x - %m%n")));
    }

    public static void configure(Appender appender) {
        Logger.getRootLogger().addAppender(appender);
    }

    public static void resetConfiguration() {
        LogManager.resetConfiguration();
    }
}

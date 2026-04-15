package org.apache.log4j.spi;

import org.apache.log4j.Appender;
import org.apache.log4j.Logger;

public interface ErrorHandler extends OptionHandler {
    void error(String str);

    void error(String str, Exception exception, int i);

    void error(String str, Exception exception, int i, LoggingEvent loggingEvent);

    void setAppender(Appender appender);

    void setBackupAppender(Appender appender);

    void setLogger(Logger logger);
}

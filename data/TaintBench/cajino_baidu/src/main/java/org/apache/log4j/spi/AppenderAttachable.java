package org.apache.log4j.spi;

import java.util.Enumeration;
import org.apache.log4j.Appender;

public interface AppenderAttachable {
    void addAppender(Appender appender);

    Enumeration getAllAppenders();

    Appender getAppender(String str);

    boolean isAttached(Appender appender);

    void removeAllAppenders();

    void removeAppender(String str);

    void removeAppender(Appender appender);
}

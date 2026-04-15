package org.apache.log4j.spi;

import java.util.Enumeration;
import org.apache.log4j.Appender;
import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public interface LoggerRepository {
    void addHierarchyEventListener(HierarchyEventListener hierarchyEventListener);

    void emitNoAppenderWarning(Category category);

    Logger exists(String str);

    void fireAddAppenderEvent(Category category, Appender appender);

    Enumeration getCurrentCategories();

    Enumeration getCurrentLoggers();

    Logger getLogger(String str);

    Logger getLogger(String str, LoggerFactory loggerFactory);

    Logger getRootLogger();

    Level getThreshold();

    boolean isDisabled(int i);

    void resetConfiguration();

    void setThreshold(String str);

    void setThreshold(Level level);

    void shutdown();
}

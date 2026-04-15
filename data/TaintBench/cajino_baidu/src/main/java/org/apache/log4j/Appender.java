package org.apache.log4j;

import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;

public interface Appender {
    void addFilter(Filter filter);

    void clearFilters();

    void close();

    void doAppend(LoggingEvent loggingEvent);

    ErrorHandler getErrorHandler();

    Filter getFilter();

    Layout getLayout();

    String getName();

    boolean requiresLayout();

    void setErrorHandler(ErrorHandler errorHandler);

    void setLayout(Layout layout);

    void setName(String str);
}

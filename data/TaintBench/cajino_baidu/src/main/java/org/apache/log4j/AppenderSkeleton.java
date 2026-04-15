package org.apache.log4j;

import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.OnlyOnceErrorHandler;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.OptionHandler;

public abstract class AppenderSkeleton implements Appender, OptionHandler {
    protected boolean closed = false;
    protected ErrorHandler errorHandler = new OnlyOnceErrorHandler();
    protected Filter headFilter;
    protected Layout layout;
    protected String name;
    protected Filter tailFilter;
    protected Priority threshold;

    public abstract void append(LoggingEvent loggingEvent);

    public abstract void close();

    public abstract boolean requiresLayout();

    protected AppenderSkeleton(boolean isActive) {
    }

    public void activateOptions() {
    }

    public void addFilter(Filter newFilter) {
        if (this.headFilter == null) {
            this.tailFilter = newFilter;
            this.headFilter = newFilter;
            return;
        }
        this.tailFilter.setNext(newFilter);
        this.tailFilter = newFilter;
    }

    public void clearFilters() {
        this.tailFilter = null;
        this.headFilter = null;
    }

    public void finalize() {
        if (!this.closed) {
            LogLog.debug(new StringBuffer().append("Finalizing appender named [").append(this.name).append("].").toString());
            close();
        }
    }

    public ErrorHandler getErrorHandler() {
        return this.errorHandler;
    }

    public Filter getFilter() {
        return this.headFilter;
    }

    public final Filter getFirstFilter() {
        return this.headFilter;
    }

    public Layout getLayout() {
        return this.layout;
    }

    public final String getName() {
        return this.name;
    }

    public Priority getThreshold() {
        return this.threshold;
    }

    public boolean isAsSevereAsThreshold(Priority priority) {
        return this.threshold == null || priority.isGreaterOrEqual(this.threshold);
    }

    public synchronized void doAppend(LoggingEvent event) {
        if (this.closed) {
            LogLog.error(new StringBuffer().append("Attempted to append to closed appender named [").append(this.name).append("].").toString());
        } else if (isAsSevereAsThreshold(event.getLevel())) {
            Filter f = this.headFilter;
            while (f != null) {
                switch (f.decide(event)) {
                    case -1:
                        break;
                    case 0:
                        f = f.getNext();
                        continue;
                    case 1:
                        break;
                    default:
                        continue;
                }
                append(event);
            }
            append(event);
        }
    }

    public synchronized void setErrorHandler(ErrorHandler eh) {
        if (eh == null) {
            LogLog.warn("You have tried to set a null error-handler.");
        } else {
            this.errorHandler = eh;
        }
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setThreshold(Priority threshold) {
        this.threshold = threshold;
    }
}

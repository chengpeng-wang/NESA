package org.apache.log4j;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.helpers.AppenderAttachableImpl;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.AppenderAttachable;
import org.apache.log4j.spi.LoggingEvent;

public class AsyncAppender extends AppenderSkeleton implements AppenderAttachable {
    public static final int DEFAULT_BUFFER_SIZE = 128;
    AppenderAttachableImpl aai = this.appenders;
    private final AppenderAttachableImpl appenders = new AppenderAttachableImpl();
    private boolean blocking = true;
    private final List buffer = new ArrayList();
    private int bufferSize = 128;
    private final Map discardMap = new HashMap();
    private final Thread dispatcher = new Thread(new Dispatcher(this, this.buffer, this.discardMap, this.appenders));
    private boolean locationInfo = false;

    private static final class DiscardSummary {
        private int count = 1;
        private LoggingEvent maxEvent;

        public DiscardSummary(LoggingEvent event) {
            this.maxEvent = event;
        }

        public void add(LoggingEvent event) {
            if (event.getLevel().toInt() > this.maxEvent.getLevel().toInt()) {
                this.maxEvent = event;
            }
            this.count++;
        }

        public LoggingEvent createEvent() {
            return new LoggingEvent("org.apache.log4j.AsyncAppender.DONT_REPORT_LOCATION", Logger.getLogger(this.maxEvent.getLoggerName()), this.maxEvent.getLevel(), MessageFormat.format("Discarded {0} messages due to full event buffer including: {1}", new Object[]{new Integer(this.count), this.maxEvent.getMessage()}), null);
        }
    }

    private static class Dispatcher implements Runnable {
        private final AppenderAttachableImpl appenders;
        private final List buffer;
        private final Map discardMap;
        private final AsyncAppender parent;

        public Dispatcher(AsyncAppender parent, List buffer, Map discardMap, AppenderAttachableImpl appenders) {
            this.parent = parent;
            this.buffer = buffer;
            this.appenders = appenders;
            this.discardMap = discardMap;
        }

        public void run() {
            boolean isActive = true;
            while (isActive) {
                LoggingEvent[] events = null;
                try {
                    synchronized (this.buffer) {
                        int bufferSize = this.buffer.size();
                        isActive = !this.parent.closed;
                        while (bufferSize == 0 && isActive) {
                            this.buffer.wait();
                            bufferSize = this.buffer.size();
                            isActive = !this.parent.closed;
                        }
                        if (bufferSize > 0) {
                            events = new LoggingEvent[(this.discardMap.size() + bufferSize)];
                            this.buffer.toArray(events);
                            int index = bufferSize;
                            int index2 = index;
                            for (DiscardSummary createEvent : this.discardMap.values()) {
                                index = index2 + 1;
                                events[index2] = createEvent.createEvent();
                                index2 = index;
                            }
                            this.buffer.clear();
                            this.discardMap.clear();
                            this.buffer.notifyAll();
                        }
                    }
                    if (events != null) {
                        for (LoggingEvent appendLoopOnAppenders : events) {
                            synchronized (this.appenders) {
                                this.appenders.appendLoopOnAppenders(appendLoopOnAppenders);
                            }
                        }
                        continue;
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    public AsyncAppender() {
        this.dispatcher.setDaemon(true);
        this.dispatcher.setName(new StringBuffer().append("AsyncAppender-Dispatcher-").append(this.dispatcher.getName()).toString());
        this.dispatcher.start();
    }

    public void addAppender(Appender newAppender) {
        synchronized (this.appenders) {
            this.appenders.addAppender(newAppender);
        }
    }

    public void append(LoggingEvent event) {
        if (this.dispatcher == null || !this.dispatcher.isAlive() || this.bufferSize <= 0) {
            synchronized (this.appenders) {
                this.appenders.appendLoopOnAppenders(event);
            }
            return;
        }
        event.getNDC();
        event.getThreadName();
        event.getMDCCopy();
        if (this.locationInfo) {
            event.getLocationInformation();
        }
        event.getRenderedMessage();
        event.getThrowableStrRep();
        synchronized (this.buffer) {
            boolean discard;
            do {
                int previousSize = this.buffer.size();
                if (previousSize < this.bufferSize) {
                    this.buffer.add(event);
                    if (previousSize == 0) {
                        this.buffer.notifyAll();
                    }
                } else {
                    discard = true;
                    if (!(!this.blocking || Thread.interrupted() || Thread.currentThread() == this.dispatcher)) {
                        try {
                            this.buffer.wait();
                            discard = false;
                            continue;
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            continue;
                        }
                    }
                }
            } while (!discard);
            String loggerName = event.getLoggerName();
            DiscardSummary summary = (DiscardSummary) this.discardMap.get(loggerName);
            if (summary == null) {
                this.discardMap.put(loggerName, new DiscardSummary(event));
            } else {
                summary.add(event);
            }
        }
    }

    public void close() {
        synchronized (this.buffer) {
            this.closed = true;
            this.buffer.notifyAll();
        }
        try {
            this.dispatcher.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LogLog.error("Got an InterruptedException while waiting for the dispatcher to finish.", e);
        }
        synchronized (this.appenders) {
            Enumeration iter = this.appenders.getAllAppenders();
            if (iter != null) {
                while (iter.hasMoreElements()) {
                    Object next = iter.nextElement();
                    if (next instanceof Appender) {
                        ((Appender) next).close();
                    }
                }
            }
        }
    }

    public Enumeration getAllAppenders() {
        Enumeration allAppenders;
        synchronized (this.appenders) {
            allAppenders = this.appenders.getAllAppenders();
        }
        return allAppenders;
    }

    public Appender getAppender(String name) {
        Appender appender;
        synchronized (this.appenders) {
            appender = this.appenders.getAppender(name);
        }
        return appender;
    }

    public boolean getLocationInfo() {
        return this.locationInfo;
    }

    public boolean isAttached(Appender appender) {
        boolean isAttached;
        synchronized (this.appenders) {
            isAttached = this.appenders.isAttached(appender);
        }
        return isAttached;
    }

    public boolean requiresLayout() {
        return false;
    }

    public void removeAllAppenders() {
        synchronized (this.appenders) {
            this.appenders.removeAllAppenders();
        }
    }

    public void removeAppender(Appender appender) {
        synchronized (this.appenders) {
            this.appenders.removeAppender(appender);
        }
    }

    public void removeAppender(String name) {
        synchronized (this.appenders) {
            this.appenders.removeAppender(name);
        }
    }

    public void setLocationInfo(boolean flag) {
        this.locationInfo = flag;
    }

    public void setBufferSize(int size) {
        if (size < 0) {
            throw new NegativeArraySizeException("size");
        }
        synchronized (this.buffer) {
            if (size < 1) {
                size = 1;
            }
            this.bufferSize = size;
            this.buffer.notifyAll();
        }
    }

    public int getBufferSize() {
        return this.bufferSize;
    }

    public void setBlocking(boolean value) {
        synchronized (this.buffer) {
            this.blocking = value;
            this.buffer.notifyAll();
        }
    }

    public boolean getBlocking() {
        return this.blocking;
    }
}

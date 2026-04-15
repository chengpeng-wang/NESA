package com.trilead.ssh2.util;

import com.trilead.ssh2.log.Logger;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.LinkedList;

public class TimeoutService {
    /* access modifiers changed from: private|static|final */
    public static final Logger log = Logger.getLogger(TimeoutService.class);
    /* access modifiers changed from: private|static */
    public static Thread timeoutThread = null;
    /* access modifiers changed from: private|static|final */
    public static final LinkedList todolist = new LinkedList();

    private static class TimeoutThread extends Thread {
        private TimeoutThread() {
        }

        /* synthetic */ TimeoutThread(TimeoutThread timeoutThread) {
            this();
        }

        public void run() {
            synchronized (TimeoutService.todolist) {
                while (TimeoutService.todolist.size() != 0) {
                    long now = System.currentTimeMillis();
                    TimeoutToken tt = (TimeoutToken) TimeoutService.todolist.getFirst();
                    if (tt.runTime > now) {
                        try {
                            TimeoutService.todolist.wait(tt.runTime - now);
                        } catch (InterruptedException e) {
                        }
                    } else {
                        TimeoutService.todolist.removeFirst();
                        try {
                            tt.handler.run();
                        } catch (Exception e2) {
                            StringWriter sw = new StringWriter();
                            e2.printStackTrace(new PrintWriter(sw));
                            TimeoutService.log.log(20, "Exeception in Timeout handler:" + e2.getMessage() + "(" + sw.toString() + ")");
                        }
                    }
                }
                TimeoutService.timeoutThread = null;
            }
        }
    }

    public static class TimeoutToken implements Comparable {
        /* access modifiers changed from: private */
        public Runnable handler;
        /* access modifiers changed from: private */
        public long runTime;

        private TimeoutToken(long runTime, Runnable handler) {
            this.runTime = runTime;
            this.handler = handler;
        }

        /* synthetic */ TimeoutToken(long j, Runnable runnable, TimeoutToken timeoutToken) {
            this(j, runnable);
        }

        public int compareTo(Object o) {
            TimeoutToken t = (TimeoutToken) o;
            if (this.runTime > t.runTime) {
                return 1;
            }
            if (this.runTime == t.runTime) {
                return 0;
            }
            return -1;
        }
    }

    public static final TimeoutToken addTimeoutHandler(long runTime, Runnable handler) {
        TimeoutToken token = new TimeoutToken(runTime, handler, null);
        synchronized (todolist) {
            todolist.add(token);
            Collections.sort(todolist);
            if (timeoutThread != null) {
                timeoutThread.interrupt();
            } else {
                timeoutThread = new TimeoutThread();
                timeoutThread.setDaemon(true);
                timeoutThread.start();
            }
        }
        return token;
    }

    public static final void cancelTimeoutHandler(TimeoutToken token) {
        synchronized (todolist) {
            todolist.remove(token);
            if (timeoutThread != null) {
                timeoutThread.interrupt();
            }
        }
    }
}

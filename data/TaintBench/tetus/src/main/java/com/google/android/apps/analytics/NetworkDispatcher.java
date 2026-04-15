package com.google.android.apps.analytics;

import android.os.Build;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Locale;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.ParseException;
import org.apache.http.message.BasicHttpRequest;

class NetworkDispatcher implements Dispatcher {
    /* access modifiers changed from: private|static|final */
    public static final HttpHost GOOGLE_ANALYTICS_HOST = new HttpHost("www.google-analytics.com", 80);
    private static final int MAX_EVENTS_PER_PIPELINE = 30;
    private static final int MAX_SEQUENTIAL_REQUESTS = 5;
    private static final long MIN_RETRY_INTERVAL = 2;
    private static final String USER_AGENT_TEMPLATE = "GoogleAnalytics/%s (Linux; U; Android %s; %s-%s; %s; Build/%s)";
    private DispatcherThread dispatcherThread;
    private final String userAgent;

    private static class DispatcherThread extends HandlerThread {
        /* access modifiers changed from: private */
        public AsyncDispatchTask currentTask;
        private Handler handlerExecuteOnDispatcherThread;
        /* access modifiers changed from: private */
        public int lastStatusCode;
        /* access modifiers changed from: private */
        public int maxEventsPerRequest;
        /* access modifiers changed from: private|final */
        public final Handler messageHandler;
        /* access modifiers changed from: private|final */
        public final PipelinedRequester pipelinedRequester;
        /* access modifiers changed from: private|final */
        public final String referrer;
        /* access modifiers changed from: private */
        public long retryInterval;
        /* access modifiers changed from: private|final */
        public final String userAgent;

        private class AsyncDispatchTask implements Runnable {
            private final LinkedList<Event> events = new LinkedList();

            public AsyncDispatchTask(Event[] eventArr) {
                Collections.addAll(this.events, eventArr);
            }

            private void dispatchSomePendingEvents() throws IOException, ParseException, HttpException {
                int i = 0;
                while (true) {
                    int i2 = i;
                    if (i2 >= this.events.size() || i2 >= DispatcherThread.this.maxEventsPerRequest) {
                        DispatcherThread.this.pipelinedRequester.sendRequests();
                    } else {
                        Event event = (Event) this.events.get(i2);
                        BasicHttpRequest basicHttpRequest = new BasicHttpRequest("GET", "__##GOOGLEPAGEVIEW##__".equals(event.category) ? NetworkRequestUtil.constructPageviewRequestPath(event, DispatcherThread.this.referrer) : NetworkRequestUtil.constructEventRequestPath(event, DispatcherThread.this.referrer));
                        basicHttpRequest.addHeader("Host", NetworkDispatcher.GOOGLE_ANALYTICS_HOST.getHostName());
                        basicHttpRequest.addHeader("User-Agent", DispatcherThread.this.userAgent);
                        DispatcherThread.this.pipelinedRequester.addRequest(basicHttpRequest);
                        i = i2 + 1;
                    }
                }
                DispatcherThread.this.pipelinedRequester.sendRequests();
            }

            public Event removeNextEvent() {
                return (Event) this.events.poll();
            }

            public void run() {
                String str;
                String str2 = GoogleAnalyticsTracker.TRACKER_TAG;
                DispatcherThread.this.currentTask = this;
                int i = 0;
                while (i < NetworkDispatcher.MAX_SEQUENTIAL_REQUESTS && this.events.size() > 0) {
                    long j = 0;
                    try {
                        if (DispatcherThread.this.lastStatusCode == 500 || DispatcherThread.this.lastStatusCode == 503) {
                            j = (long) (Math.random() * ((double) DispatcherThread.this.retryInterval));
                            if (DispatcherThread.this.retryInterval < 256) {
                                DispatcherThread.access$630(DispatcherThread.this, NetworkDispatcher.MIN_RETRY_INTERVAL);
                            }
                        } else {
                            DispatcherThread.this.retryInterval = NetworkDispatcher.MIN_RETRY_INTERVAL;
                        }
                        Thread.sleep(j * 1000);
                        dispatchSomePendingEvents();
                        i++;
                    } catch (InterruptedException e) {
                        str = GoogleAnalyticsTracker.TRACKER_TAG;
                        Log.w(str2, "Couldn't sleep.", e);
                    } catch (IOException e2) {
                        str = GoogleAnalyticsTracker.TRACKER_TAG;
                        Log.w(str2, "Problem with socket or streams.", e2);
                    } catch (HttpException e3) {
                        str = GoogleAnalyticsTracker.TRACKER_TAG;
                        Log.w(str2, "Problem with http streams.", e3);
                    }
                }
                DispatcherThread.this.pipelinedRequester.finishedCurrentRequests();
                DispatcherThread.this.messageHandler.sendMessage(DispatcherThread.this.messageHandler.obtainMessage(Dispatcher.MSG_DISPATCHED_FINISHED));
                DispatcherThread.this.currentTask = null;
            }
        }

        private class RequesterCallbacks implements Callbacks {
            private RequesterCallbacks() {
            }

            public void pipelineModeChanged(boolean z) {
                if (z) {
                    DispatcherThread.this.maxEventsPerRequest = NetworkDispatcher.MAX_EVENTS_PER_PIPELINE;
                } else {
                    DispatcherThread.this.maxEventsPerRequest = 1;
                }
            }

            public void requestSent() {
                if (DispatcherThread.this.currentTask != null) {
                    Event removeNextEvent = DispatcherThread.this.currentTask.removeNextEvent();
                    if (removeNextEvent != null) {
                        DispatcherThread.this.messageHandler.sendMessage(DispatcherThread.this.messageHandler.obtainMessage(Dispatcher.MSG_EVENT_DISPATCHED, new Long(removeNextEvent.eventId)));
                    }
                }
            }

            public void serverError(int i) {
                DispatcherThread.this.lastStatusCode = i;
            }
        }

        private DispatcherThread(Handler handler, PipelinedRequester pipelinedRequester, String str, String str2) {
            super("DispatcherThread");
            this.maxEventsPerRequest = NetworkDispatcher.MAX_EVENTS_PER_PIPELINE;
            this.currentTask = null;
            this.messageHandler = handler;
            this.referrer = str;
            this.userAgent = str2;
            this.pipelinedRequester = pipelinedRequester;
            this.pipelinedRequester.installCallbacks(new RequesterCallbacks());
        }

        private DispatcherThread(Handler handler, String str, String str2) {
            this(handler, new PipelinedRequester(NetworkDispatcher.GOOGLE_ANALYTICS_HOST), str, str2);
        }

        static /* synthetic */ long access$630(DispatcherThread dispatcherThread, long j) {
            long j2 = dispatcherThread.retryInterval * j;
            dispatcherThread.retryInterval = j2;
            return j2;
        }

        public void dispatchEvents(Event[] eventArr) {
            if (this.handlerExecuteOnDispatcherThread != null) {
                this.handlerExecuteOnDispatcherThread.post(new AsyncDispatchTask(eventArr));
            }
        }

        /* access modifiers changed from: protected */
        public void onLooperPrepared() {
            this.handlerExecuteOnDispatcherThread = new Handler();
        }
    }

    public NetworkDispatcher() {
        Locale locale = Locale.getDefault();
        String str = USER_AGENT_TEMPLATE;
        Object[] objArr = new Object[6];
        objArr[0] = GoogleAnalyticsTracker.VERSION;
        objArr[1] = VERSION.RELEASE;
        objArr[2] = locale.getLanguage() != null ? locale.getLanguage().toLowerCase() : "en";
        objArr[3] = locale.getCountry() != null ? locale.getCountry().toLowerCase() : "";
        objArr[4] = Build.MODEL;
        objArr[MAX_SEQUENTIAL_REQUESTS] = Build.ID;
        this.userAgent = String.format(str, objArr);
    }

    public void dispatchEvents(Event[] eventArr) {
        this.dispatcherThread.dispatchEvents(eventArr);
    }

    public void init(Handler handler, PipelinedRequester pipelinedRequester, String str) {
        stop();
        this.dispatcherThread = new DispatcherThread(handler, pipelinedRequester, str, this.userAgent);
        this.dispatcherThread.start();
    }

    public void init(Handler handler, String str) {
        stop();
        this.dispatcherThread = new DispatcherThread(handler, str, this.userAgent);
        this.dispatcherThread.start();
    }

    public void stop() {
        if (this.dispatcherThread != null && this.dispatcherThread.getLooper() != null) {
            this.dispatcherThread.getLooper().quit();
        }
    }

    public void waitForThreadLooper() {
        this.dispatcherThread.getLooper();
    }
}

package com.splunk.mint;

import java.util.concurrent.ThreadFactory;

class LowPriorityThreadFactory implements ThreadFactory {
    LowPriorityThreadFactory() {
    }

    public Thread newThread(Runnable r) {
        Thread t = new Thread(r);
        t.setPriority(1);
        return t;
    }
}

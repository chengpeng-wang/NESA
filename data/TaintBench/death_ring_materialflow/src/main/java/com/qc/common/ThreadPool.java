package com.qc.common;

import com.qc.util.ShareProDBHelper;
import java.util.LinkedList;
import java.util.List;

public class ThreadPool {
    public static final int PRIORITY_HIGH = 2;
    public static final int PRIORITY_LOW = 0;
    public static final int PRIORITY_MAX_HIGH = 10;
    public static final int PRIORITY_NORMAL = 1;
    private static ThreadPool instance = null;
    private boolean _debug;
    private List[] _idxThreads;
    private boolean _shutdown;
    private int _threadCount;

    public static ThreadPool getInstance() {
        if (instance == null) {
            instance = new ThreadPool();
        }
        return instance;
    }

    private ThreadPool() {
        this._shutdown = false;
        this._threadCount = 0;
        this._debug = false;
        this._idxThreads = new List[]{new LinkedList(), new LinkedList(), new LinkedList()};
        this._threadCount = 0;
    }

    /* access modifiers changed from: protected|declared_synchronized */
    public synchronized void repool(Pooled repool) {
        if (this._shutdown) {
            if (this._debug) {
                System.out.println("ThreadPool.repool():重设中……");
            }
            switch (repool.getPriority()) {
                case 1:
                    this._idxThreads[0].add(repool);
                    break;
                case ShareProDBHelper.FLOATVALUE /*5*/:
                    this._idxThreads[1].add(repool);
                    break;
                case PRIORITY_MAX_HIGH /*10*/:
                    this._idxThreads[2].add(repool);
                    break;
                default:
                    throw new IllegalStateException("没有此种级别");
            }
            notifyAll();
        } else {
            if (this._debug) {
                System.out.println("ThreadPool.repool():注销中……");
            }
            repool.shutDown();
        }
        if (this._debug) {
            System.out.println("ThreadPool.repool():完成");
        }
    }

    public void setDebug(boolean debug) {
        this._debug = debug;
    }

    public synchronized void shutDown() {
        this._shutdown = true;
        if (this._debug) {
            System.out.println("ThreadPool.shutDown():关闭中……");
        }
        for (int index = 0; index <= 1; index++) {
            List threads = this._idxThreads[index];
            for (int threadIndex = 0; threadIndex < threads.size(); threadIndex++) {
                ((Pooled) threads.get(threadIndex)).shutDown();
            }
        }
        notifyAll();
    }

    /* JADX WARNING: Missing block: B:7:0x001d, code skipped:
            return;
     */
    public synchronized void start(java.lang.Runnable r9, int r10) {
        /*
        r8 = this;
        monitor-enter(r8);
        r4 = 0;
        r6 = r8._idxThreads;	 Catch:{ all -> 0x005a }
        r1 = r6[r10];	 Catch:{ all -> 0x005a }
        r2 = r1.size();	 Catch:{ all -> 0x005a }
        if (r2 <= 0) goto L_0x001e;
    L_0x000c:
        r3 = r2 + -1;
        r6 = r1.get(r3);	 Catch:{ all -> 0x005a }
        r0 = r6;
        r0 = (com.qc.common.Pooled) r0;	 Catch:{ all -> 0x005a }
        r4 = r0;
        r1.remove(r1);	 Catch:{ all -> 0x005a }
        r4.setTarget(r9);	 Catch:{ all -> 0x005a }
    L_0x001c:
        monitor-exit(r8);
        return;
    L_0x001e:
        r6 = r8._threadCount;	 Catch:{ all -> 0x005a }
        r6 = r6 + 1;
        r8._threadCount = r6;	 Catch:{ all -> 0x005a }
        r5 = new com.qc.common.Pooled;	 Catch:{ all -> 0x005a }
        r6 = new java.lang.StringBuilder;	 Catch:{ all -> 0x005a }
        r7 = "Pooled->";
        r6.<init>(r7);	 Catch:{ all -> 0x005a }
        r7 = r8._threadCount;	 Catch:{ all -> 0x005a }
        r6 = r6.append(r7);	 Catch:{ all -> 0x005a }
        r6 = r6.toString();	 Catch:{ all -> 0x005a }
        r5.m61init(r9, r6, r8);	 Catch:{ all -> 0x005a }
        switch(r10) {
            case 0: goto L_0x0046;
            case 1: goto L_0x004f;
            case 2: goto L_0x0054;
            default: goto L_0x003d;
        };
    L_0x003d:
        r6 = 5;
        r5.setPriority(r6);	 Catch:{ all -> 0x004b }
    L_0x0041:
        r5.start();	 Catch:{ all -> 0x004b }
        r4 = r5;
        goto L_0x001c;
    L_0x0046:
        r6 = 1;
        r5.setPriority(r6);	 Catch:{ all -> 0x004b }
        goto L_0x0041;
    L_0x004b:
        r6 = move-exception;
        r4 = r5;
    L_0x004d:
        monitor-exit(r8);
        throw r6;
    L_0x004f:
        r6 = 5;
        r5.setPriority(r6);	 Catch:{ all -> 0x004b }
        goto L_0x0041;
    L_0x0054:
        r6 = 10;
        r5.setPriority(r6);	 Catch:{ all -> 0x004b }
        goto L_0x0041;
    L_0x005a:
        r6 = move-exception;
        goto L_0x004d;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.qc.common.ThreadPool.start(java.lang.Runnable, int):void");
    }

    public int getThreadsCount() {
        return this._threadCount;
    }
}

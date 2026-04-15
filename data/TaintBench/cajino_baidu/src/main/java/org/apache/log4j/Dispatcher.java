package org.apache.log4j;

import org.apache.log4j.helpers.AppenderAttachableImpl;
import org.apache.log4j.helpers.BoundedFIFO;

class Dispatcher extends Thread {
    private AppenderAttachableImpl aai;
    private BoundedFIFO bf;
    AsyncAppender container;
    private boolean interrupted = false;

    Dispatcher(BoundedFIFO bf, AsyncAppender container) {
        this.bf = bf;
        this.container = container;
        this.aai = container.aai;
        setDaemon(true);
        setPriority(1);
        setName(new StringBuffer().append("Dispatcher-").append(getName()).toString());
    }

    /* access modifiers changed from: 0000 */
    public void close() {
        synchronized (this.bf) {
            this.interrupted = true;
            if (this.bf.length() == 0) {
                this.bf.notify();
            }
        }
    }

    /* JADX WARNING: Missing block: B:17:0x002f, code skipped:
            r3 = r4.container.aai;
     */
    /* JADX WARNING: Missing block: B:18:0x0033, code skipped:
            monitor-enter(r3);
     */
    /* JADX WARNING: Missing block: B:21:0x0036, code skipped:
            if (r4.aai == null) goto L_0x003f;
     */
    /* JADX WARNING: Missing block: B:22:0x0038, code skipped:
            if (r1 == null) goto L_0x003f;
     */
    /* JADX WARNING: Missing block: B:23:0x003a, code skipped:
            r4.aai.appendLoopOnAppenders(r1);
     */
    /* JADX WARNING: Missing block: B:24:0x003f, code skipped:
            monitor-exit(r3);
     */
    public void run() {
        /*
        r4 = this;
    L_0x0000:
        r3 = r4.bf;
        monitor-enter(r3);
        r2 = r4.bf;	 Catch:{ all -> 0x0047 }
        r2 = r2.length();	 Catch:{ all -> 0x0047 }
        if (r2 != 0) goto L_0x001b;
    L_0x000b:
        r2 = r4.interrupted;	 Catch:{ all -> 0x0047 }
        if (r2 == 0) goto L_0x0016;
    L_0x000f:
        monitor-exit(r3);	 Catch:{ all -> 0x0047 }
    L_0x0010:
        r2 = r4.aai;
        r2.removeAllAppenders();
        return;
    L_0x0016:
        r2 = r4.bf;	 Catch:{ InterruptedException -> 0x0044 }
        r2.wait();	 Catch:{ InterruptedException -> 0x0044 }
    L_0x001b:
        r2 = r4.bf;	 Catch:{ all -> 0x0047 }
        r1 = r2.get();	 Catch:{ all -> 0x0047 }
        r2 = r4.bf;	 Catch:{ all -> 0x0047 }
        r2 = r2.wasFull();	 Catch:{ all -> 0x0047 }
        if (r2 == 0) goto L_0x002e;
    L_0x0029:
        r2 = r4.bf;	 Catch:{ all -> 0x0047 }
        r2.notify();	 Catch:{ all -> 0x0047 }
    L_0x002e:
        monitor-exit(r3);	 Catch:{ all -> 0x0047 }
        r2 = r4.container;
        r3 = r2.aai;
        monitor-enter(r3);
        r2 = r4.aai;	 Catch:{ all -> 0x0041 }
        if (r2 == 0) goto L_0x003f;
    L_0x0038:
        if (r1 == 0) goto L_0x003f;
    L_0x003a:
        r2 = r4.aai;	 Catch:{ all -> 0x0041 }
        r2.appendLoopOnAppenders(r1);	 Catch:{ all -> 0x0041 }
    L_0x003f:
        monitor-exit(r3);	 Catch:{ all -> 0x0041 }
        goto L_0x0000;
    L_0x0041:
        r2 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x0041 }
        throw r2;
    L_0x0044:
        r0 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x0047 }
        goto L_0x0010;
    L_0x0047:
        r2 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x0047 }
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.log4j.Dispatcher.run():void");
    }
}

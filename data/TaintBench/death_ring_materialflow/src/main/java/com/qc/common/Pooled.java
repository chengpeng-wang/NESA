package com.qc.common;

public class Pooled extends Thread {
    private boolean _idle = false;
    private ThreadPool _pool;
    private boolean _shutdown = false;
    private Runnable _target;

    public Pooled(Runnable target) {
        super(target);
    }

    public Pooled(Runnable target, String name) {
        super(target, name);
    }

    public Pooled(Runnable target, String name, ThreadPool pool) {
        super(name);
        this._pool = pool;
        this._target = target;
    }

    public Pooled(String name) {
        super(name);
    }

    public Pooled(ThreadGroup group, Runnable target) {
        super(group, target);
    }

    public Pooled(ThreadGroup group, Runnable target, String name) {
        super(group, target, name);
    }

    public Pooled(ThreadGroup group, String name) {
        super(group, name);
    }

    public Runnable getTarget() {
        return this._target;
    }

    public boolean isIdle() {
        return this._idle;
    }

    public void run() {
        while (!this._shutdown) {
            this._idle = false;
            if (this._target != null) {
                this._target.run();
            }
            this._idle = true;
            try {
                this._pool.repool(this);
                synchronized (this) {
                    wait();
                }
            } catch (InterruptedException ex) {
                System.err.println(ex.getMessage());
            }
            this._idle = false;
        }
    }

    public synchronized void setTarget(Runnable target) {
        this._target = target;
        notifyAll();
    }

    public synchronized void shutDown() {
        this._shutdown = true;
        notifyAll();
    }
}

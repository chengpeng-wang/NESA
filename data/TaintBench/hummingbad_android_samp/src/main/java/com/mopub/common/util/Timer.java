package com.mopub.common.util;

import java.util.concurrent.TimeUnit;

public class Timer {
    private long mStartTimeNanos;
    private State mState = State.STOPPED;
    private long mStopTimeNanos;

    private enum State {
        STARTED,
        STOPPED
    }

    public void start() {
        this.mStartTimeNanos = System.nanoTime();
        this.mState = State.STARTED;
    }

    public void stop() {
        if (this.mState != State.STARTED) {
            throw new IllegalStateException("EventTimer was not started.");
        }
        this.mState = State.STOPPED;
        this.mStopTimeNanos = System.nanoTime();
    }

    public long getTime() {
        long nanoTime;
        if (this.mState == State.STARTED) {
            nanoTime = System.nanoTime();
        } else {
            nanoTime = this.mStopTimeNanos;
        }
        return TimeUnit.MILLISECONDS.convert(nanoTime - this.mStartTimeNanos, TimeUnit.NANOSECONDS);
    }
}

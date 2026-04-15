package com.splunk.mint.network;

import java.util.concurrent.atomic.AtomicLong;

public class Counter extends Metric<Long> {
    private AtomicLong count = new AtomicLong();

    public Counter(String name) {
        super(name);
    }

    public Long getValue() {
        return Long.valueOf(this.count.get());
    }

    public void inc() {
        this.count.incrementAndGet();
    }

    public void inc(long delta) {
        this.count.addAndGet(delta);
    }

    public void dec() {
        this.count.decrementAndGet();
    }

    public void dec(long delta) {
        this.count.getAndAdd(-delta);
    }
}

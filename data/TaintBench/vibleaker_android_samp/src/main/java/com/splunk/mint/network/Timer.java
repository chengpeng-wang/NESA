package com.splunk.mint.network;

public class Timer extends Metric<Long> {
    private Long end = Long.valueOf(0);
    private Long start = null;

    public Timer(String name) {
        super(name + "-timer");
    }

    public void start() {
        if (this.start == null) {
            this.start = Long.valueOf(System.currentTimeMillis());
        }
    }

    public void done() {
        this.end = Long.valueOf(System.currentTimeMillis());
    }

    public long getStartValue() {
        if (this.start == null) {
            return 0;
        }
        return this.start.longValue();
    }

    public long getStopValue() {
        return this.end.longValue();
    }

    public Long getValue() {
        if (this.start == null) {
            return null;
        }
        if (this.end != null) {
            return Long.valueOf(this.end.longValue() - this.start.longValue());
        }
        return Long.valueOf(System.currentTimeMillis() - this.start.longValue());
    }
}

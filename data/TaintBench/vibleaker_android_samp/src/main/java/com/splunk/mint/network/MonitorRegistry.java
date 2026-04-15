package com.splunk.mint.network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MonitorRegistry {
    private Set<Metric<?>> metrics = Collections.synchronizedSet(new HashSet());

    public void add(Metric<?> m) {
        this.metrics.add(m);
    }

    public synchronized ArrayList<Metric<?>> getMetricsForName(String name) {
        ArrayList<Metric<?>> returnedMetrics;
        returnedMetrics = new ArrayList();
        synchronized (this.metrics) {
            Iterator<Metric<?>> it = this.metrics.iterator();
            while (it.hasNext()) {
                Metric<?> metric = (Metric) it.next();
                if (name.contains(metric.getName().substring(0, metric.getName().indexOf("-")))) {
                    returnedMetrics.add(metric);
                }
                it.remove();
            }
        }
        return returnedMetrics;
    }

    public String toString() {
        StringBuffer strBuf = new StringBuffer();
        for (Metric<?> m : this.metrics) {
            strBuf.append(m.getName() + " = " + m.getValue() + "\n");
        }
        return strBuf.toString();
    }
}

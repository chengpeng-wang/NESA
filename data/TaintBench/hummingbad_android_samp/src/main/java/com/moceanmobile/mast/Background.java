package com.moceanmobile.mast;

import java.util.concurrent.ScheduledThreadPoolExecutor;

public class Background {
    private static final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

    public static ScheduledThreadPoolExecutor getExecutor() {
        return executor;
    }
}

package com.splunk.mint;

import java.util.concurrent.ExecutorService;

class BaseExecutor {
    protected static volatile ExecutorService executor = null;

    BaseExecutor() {
    }
}

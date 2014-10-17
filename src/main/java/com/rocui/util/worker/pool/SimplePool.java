package com.rocui.util.worker.pool;

import com.rocui.util.worker.Jtask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public abstract class SimplePool {

    public static final String POOL_TYPE_SINGLE = "single";
    public static final String POOL_TYPE_CACHE = "cache";
    public static final String POOL_TYPE_FIXED = "fixed";
    protected ExecutorService pool;
    protected String type;
    protected String name;

    public void addTask(Jtask task) {
        Future<?> result=(Future<?>) this.pool.submit(task);
    }

    public void stop() {
        this.pool.shutdown();
    }
}

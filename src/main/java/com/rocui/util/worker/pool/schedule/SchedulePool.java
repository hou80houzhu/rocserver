package com.rocui.util.worker.pool.schedule;

import com.rocui.timmer.JtimmerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SchedulePool {

    private ScheduledExecutorService pool;

    public SchedulePool(int size) {
        pool = Executors.newScheduledThreadPool(size);
    }

    public void addTask(JtimmerTask task) {
        if (task.getType().equals(JtimmerTask.TIMMER_TYPE_CIRCLE)) {
            this.pool.scheduleAtFixedRate(task, task.getDelay(), task.getCircle(), TimeUnit.SECONDS);
        } else if (task.getType().equals(JtimmerTask.TIMMER_TYPE_DELAY)) {
            this.pool.schedule(task, task.getDelay(), TimeUnit.SECONDS);
        } else {
            this.pool.scheduleWithFixedDelay(task, task.getDelay(), task.getCircle(), TimeUnit.SECONDS);
        }
    }

    public void stop() {
        this.pool.shutdown();
    }
}

package com.rocui.util.worker;

import com.rocui.timmer.JtimmerTask;
import com.rocui.util.worker.pool.CachePool;
import com.rocui.util.worker.pool.Fixedpool;
import com.rocui.util.worker.pool.SimplePool;
import com.rocui.util.worker.pool.SinglePool;
import com.rocui.util.worker.pool.schedule.SchedulePool;
import java.util.HashMap;

public class JworkerContainer {

    private static JworkerContainer container = null;

    private final HashMap<String, SimplePool> pool = new HashMap<String, SimplePool>();
    private final HashMap<String, SchedulePool> schedulePool = new HashMap<String, SchedulePool>();

    private static synchronized void syncInit() {
        if (JworkerContainer.container == null) {
            JworkerContainer.container = new JworkerContainer();
        }
    }

    public static JworkerContainer getContainer() {
        if (JworkerContainer.container == null) {
            syncInit();
        }
        return JworkerContainer.container;
    }

    public SimplePool getSinglePool() {
        if (!this.pool.containsKey("defaultSingle")) {
            synchronized (this.pool) {
                this.pool.put("defaultSingle", new SinglePool("defaultSingle"));
            }
        }
        return this.pool.get("defaultSingle");
    }

    public SimplePool getSinglePool(String name) {
        if (!this.pool.containsKey(name)) {
            synchronized (this.pool) {
                this.pool.put(name, new SinglePool(name));
            }
        }
        return this.pool.get(name);
    }

    public SimplePool getCachePool() {
        if (!this.pool.containsKey("defaultCache")) {
            synchronized (this.pool) {
                this.pool.put("defaultCache", new CachePool("defaultCache"));
            }
        }
        return this.pool.get("defaultCache");
    }

    public SimplePool getCachePool(String name) {
        if (!this.pool.containsKey(name)) {
            synchronized (this.pool) {
                this.pool.put(name, new CachePool(name));
            }
        }
        return this.pool.get(name);
    }

    public SimplePool getFixedPool() {
        if (!this.pool.containsKey("defaultFixed")) {
            synchronized (this.pool) {
                this.pool.put("defaultFixed", new Fixedpool(5, "defaultFixed"));
            }
        }
        return this.pool.get("defaultFixed");
    }

    public SimplePool getFixedPool(String name, int size) {
        if (!this.pool.containsKey(name)) {
            synchronized (this.pool) {
                this.pool.put(name, new Fixedpool(size, name));
            }
        }
        return this.pool.get(name);
    }

    public SimplePool getFixedPool(String name) {
        if (!this.pool.containsKey(name)) {
            synchronized (this.pool) {
                this.pool.put(name, new Fixedpool(5, name));
            }
        }
        return this.pool.get(name);
    }

    public SchedulePool getSchedulePool() {
        if (!this.schedulePool.containsKey("default")) {
            synchronized (this.schedulePool) {
                this.schedulePool.put("default", new SchedulePool(5));
            }
        }
        return this.schedulePool.get("default");
    }

    public SchedulePool getSchedulePool(String name, int size) {
        if (!this.schedulePool.containsKey(name)) {
            synchronized (this.schedulePool) {
                this.schedulePool.put(name, new SchedulePool(size));
            }
        }
        return this.schedulePool.get(name);
    }

    public SchedulePool getSchedulePool(String name) {
        if (!this.schedulePool.containsKey(name)) {
            synchronized (this.schedulePool) {
                this.schedulePool.put(name, new SchedulePool(5));
            }
        }
        return this.schedulePool.get(name);
    }

    public static void main(String[] args) {
        JworkerContainer.getContainer().getSchedulePool().addTask(new JtimmerTask(JtimmerTask.TIMMER_TYPE_CIRCLE, 5, 5) {
            @Override
            public void excute() {
                System.out.println("I am a timmer...");
            }
        });
    }
}

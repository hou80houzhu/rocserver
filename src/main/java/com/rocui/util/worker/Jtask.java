package com.rocui.util.worker;

public abstract class Jtask implements Runnable {

    private Object cache;

    @Override
    public void run() {
        this.excute();
    }

    public abstract void excute();

    public Object getCache() {
        return cache;
    }

    public void setCache(Object cache) {
        this.cache = cache;
    }

}

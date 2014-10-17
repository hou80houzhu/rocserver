package com.rocui.util.worker.pool;

import java.util.concurrent.Executors;

/**
 *
 * @author Jinliang
 */
public class CachePool extends SimplePool {

    public CachePool(String name) {
        this.type = SimplePool.POOL_TYPE_CACHE;
        this.pool=Executors.newCachedThreadPool();
        this.name=name;
    }
}

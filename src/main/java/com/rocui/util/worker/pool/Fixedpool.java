package com.rocui.util.worker.pool;

import com.rocui.util.worker.Jtask;
import java.util.concurrent.Executors;

/**
 *
 * @author Jinliang
 */
public class Fixedpool extends SimplePool {

    public Fixedpool(int size, String name) {
        this.type = SimplePool.POOL_TYPE_FIXED;
        this.pool = Executors.newFixedThreadPool(size);
        this.name = name;
    }
}

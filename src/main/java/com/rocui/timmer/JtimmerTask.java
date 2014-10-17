package com.rocui.timmer;

import com.rocui.util.worker.Jtask;

public abstract class JtimmerTask extends Jtask {

    private int delay = 0;
    private int circle = 1000000;
    public static final String TIMMER_TYPE_DELAY = "delay";
    public static final String TIMMER_TYPE_CIRCLE = "circle";
    public static final String TIMMER_TYPE_DCIRCLE = "dcircle";
    private String type = "delay";

    public JtimmerTask(String type, int delay, int circle) {
        this.delay = delay;
        this.circle = circle;
        if (type != null && !type.equals("")) {
            this.type = type;
        }
    }

    public JtimmerTask() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDelay() {
        return delay;
    }

    public int getCircle() {
        return circle;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void setCircle(int circle) {
        this.circle = circle;
    }
    
}

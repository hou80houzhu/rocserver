package com.rocui.util.base.map;

public abstract class JmapEach {

    protected Object[] arguments;

    public JmapEach(Object... args) {
        this.arguments = args;
    }

    public abstract boolean each(String key, Object value);
}

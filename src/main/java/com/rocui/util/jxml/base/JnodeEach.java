package com.rocui.util.jxml.base;

public abstract class JnodeEach {

    protected Object[] arguments;

    public JnodeEach(Object... args) {
        this.arguments = args;
    }

    public abstract boolean each(Jnode node, int index);
}

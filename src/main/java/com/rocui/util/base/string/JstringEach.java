package com.rocui.util.base.string;

public abstract class JstringEach {

    protected Object[] arguments;

    public JstringEach(Object... args) {
        this.arguments = args;
    }

    public abstract boolean each(String k, int index);
}

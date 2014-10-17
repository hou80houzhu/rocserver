package com.rocui.util.jxml.base;

import com.rocui.util.jxml.base.Jquery;

public abstract class JqueryEach {

    protected Object[] arguments;

    public JqueryEach(Object... args) {
        this.arguments = args;
    }

    public abstract boolean each(Jquery node, int index);
}

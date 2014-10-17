package com.rocui.util.base.reflect.each;

import java.lang.reflect.Method;

public abstract class JmethodEach {

    protected Object[] arguments;

    public JmethodEach(Object... args) {
        this.arguments = args;
    }

    public abstract boolean each(Method method) throws Exception;
}

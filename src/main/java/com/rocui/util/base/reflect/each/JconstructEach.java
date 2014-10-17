package com.rocui.util.base.reflect.each;

import java.lang.reflect.Constructor;

public abstract class JconstructEach {

    protected Object[] arguments;

    public JconstructEach(Object... args) {
        this.arguments = args;
    }

    public abstract boolean each(Constructor constructor) throws Exception;
}

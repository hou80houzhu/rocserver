package com.rocui.util.base.reflect.each;

import java.lang.reflect.Field;

public abstract class JfieldEach {

    protected Object[] arguments;

    public JfieldEach(Object... args) {
        this.arguments = args;
    }

    public abstract boolean each(Field method) throws Exception;
}

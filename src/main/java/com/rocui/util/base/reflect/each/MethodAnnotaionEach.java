package com.rocui.util.base.reflect.each;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public abstract class MethodAnnotaionEach {

    protected Object[] arguments;

    public MethodAnnotaionEach(Object... args) {
        this.arguments = args;
    }

    public abstract boolean each(Method method, Annotation annotion) throws Exception;
}

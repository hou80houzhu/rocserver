package com.rocui.util.base.reflect.each;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public abstract class FieldAnnotaionEach {

    protected Object[] arguments;

    public FieldAnnotaionEach(Object... args) {
        this.arguments = args;
    }

    public abstract boolean each(Field field, Annotation annotion) throws Exception;
}

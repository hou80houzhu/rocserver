package com.rocui.util.base.reflect.each;

import java.lang.annotation.Annotation;

public abstract class JannotaionEach {

    protected Object[] arguments;

    public JannotaionEach(Object... args) {
        this.arguments = args;
    }

    public abstract boolean each(Annotation annotion) throws Exception;
}

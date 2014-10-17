package com.rocui.util.base.reflect;

import java.lang.reflect.InvocationHandler;

public abstract class BaseProxy implements InvocationHandler {

    protected Object agent;

    public void setAgent(Object agent) {
        this.agent = agent;
    }
}

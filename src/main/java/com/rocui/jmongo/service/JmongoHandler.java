package com.rocui.jmongo.service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class JmongoHandler implements InvocationHandler {

    private final Object target;

    public JmongoHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        this.before();
        Object x= method.invoke(target,args);
        this.after();
        return x;
    }

    public void before() {
        System.out.println("----before invoke----");
    }

    public void after() {
        System.out.println("----after invoke----");
    }
}

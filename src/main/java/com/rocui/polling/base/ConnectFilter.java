package com.rocui.polling.base;

public abstract class ConnectFilter {

    protected Object[] arguments;

    public ConnectFilter(Object... arguments) {
        this.arguments = arguments;
    }

    public abstract boolean filter(ConnectManager.PollingConnect connect, int index);
}

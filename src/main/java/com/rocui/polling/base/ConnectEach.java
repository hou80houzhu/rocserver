package com.rocui.polling.base;

import java.util.HashMap;

public abstract class ConnectEach {

    protected Object[] arguments;

    public ConnectEach(Object... arguments) {
        this.arguments = arguments;
    }

    public abstract boolean each(ConnectManager.PollingConnect connect, int index, HashMap<String, ConnectManager.PollingConnect> connects);
}

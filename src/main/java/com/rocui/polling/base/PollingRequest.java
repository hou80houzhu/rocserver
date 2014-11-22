package com.rocui.polling.base;

import com.rocui.polling.base.ConnectManager.PollingConnect;
import com.rocui.util.jsonx.Jsonx;

public class PollingRequest {

    private final Jsonx content;
    private final PollingConnect connect;

    public PollingRequest(String serviceType, String content, PollingConnect connect) throws Exception {
        this.connect = connect;
        this.content = Jsonx.create(content);
    }

    public String getParameter(String key) {
        return this.content.get(key).toString();
    }

    public PollingSession getSession() {
        return this.connect.getSession();
    }

    public PollingConnect getConnect() {
        return this.connect;
    }
}

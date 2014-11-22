package com.rocui.polling.base.message;

import com.rocui.polling.base.ConnectFilter;
import java.util.ArrayList;
import java.util.List;

public class MessageWrapper {

    private final String routerType;
    private final String to;
    private final List<String> tos;
    private final PollingMessage message;
    private final ConnectFilter filter;

    public MessageWrapper(String routerType, String to, PollingMessage message) {
        this.routerType = routerType;
        this.to = to;
        this.message = message;
        this.tos = new ArrayList<>();
        this.filter = null;
    }

    public MessageWrapper(String routerType, List<String> tos, PollingMessage message) {
        this.routerType = routerType;
        this.to = "";
        this.message = message;
        this.tos = tos;
        this.filter = null;
    }

    public MessageWrapper(String routerType, ConnectFilter filter, PollingMessage message) {
        this.routerType = routerType;
        this.to = "";
        this.message = message;
        this.tos = new ArrayList<>();
        this.filter = filter;
    }

    public String getRouterType() {
        return routerType;
    }

    public String getTo() {
        return to;
    }

    public PollingMessage getMessage() {
        return message;
    }

    public List<String> getTos() {
        return tos;
    }

    public ConnectFilter getFilter() {
        return filter;
    }

}

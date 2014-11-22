package com.rocui.polling.base;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class PollingSession {

    private static final ConcurrentHashMap<String, Object> globalmapping = new ConcurrentHashMap<>();
    private final HashMap<String, Object> map;

    public PollingSession() {
        this.map = new HashMap<>();
    }

    public Object getAttribute(String key) {
        return this.map.get(key);
    }

    public void setAttribute(String key, Object value) {
        this.map.put(key, value);
    }

    public void removeAttribute(String key) {
        this.map.remove(key);
    }

    public void removeAllAttributes() {
        this.map.clear();
    }

    public HashMap<String, Object> getMap() {
        return this.map;
    }

    public ConcurrentHashMap getGlobalSession() {
        return globalmapping;
    }

    public Object getGlobalAttribute(String key) {
        return globalmapping.get(key);
    }

    public void setGlobalAttribute(String key, Object value) {
        globalmapping.put(key, value);
    }

    public void removeGlobalAttribute(String key) {
        globalmapping.remove(key);
    }

    public void removeAllGlobalAttributes() {
        globalmapping.clear();
    }
}

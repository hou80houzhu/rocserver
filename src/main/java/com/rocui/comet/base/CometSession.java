package com.rocui.comet.base;

import java.util.HashMap;

public class CometSession {

    private HashMap<String, Object> map;

    public CometSession() {
        this.map = new HashMap<String, Object>();
    }

    public CometSession setAttribute(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

    public Object getAttribute(String key) {
        return this.map.get(key);
    }

    public HashMap<String, Object> getMap() {
        return this.map;
    }
}

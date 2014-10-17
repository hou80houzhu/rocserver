package com.rocui.comet.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CometResultSet {

    public static final String RESULT_SUCCESS = "1";
    public static final String RESULT_ERROR = "0";

    private String code = "1";
    private Object data;
    private String msg = "";
    private String type = "";
    private long time;
    private List<String> toIds = new ArrayList<String>();

    public CometResultSet() {
        this.toIds = new ArrayList<String>();
    }

    public CometResultSet(List<String> toIds, Object data, String type) {
        this.toIds = toIds;
        this.data = data;
        this.type = type;
    }

    public CometResultSet(String to, Object data, String type) {
        this.toIds.add(to);
        this.data = data;
        this.type = type;
    }

    public HashMap<String, Object> getInfo() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("code", this.code);
        map.put("data", this.data);
        map.put("msg", this.msg);
        map.put("type", this.type);
        map.put("time", this.time);
        return map;
    }

    public CometResultSet code(String code) {
        this.code = code;
        return this;
    }

    public String code() {
        return this.code;
    }

    public CometResultSet data(Object data) {
        this.data = data;
        return this;
    }

    public Object data() {
        return this.data;
    }

    public CometResultSet msg(String msg) {
        this.msg = msg;
        return this;
    }

    public String msg() {
        return this.msg;
    }

    public CometResultSet to(String id) {
        this.toIds.add(id);
        return this;
    }

    public CometResultSet tos(List<String> ids) {
        this.toIds.addAll(ids);
        return this;
    }

    public List<String> to() {
        return this.toIds;
    }

    public String type() {
        return this.type;
    }

    public CometResultSet type(String type) {
        this.type = type;
        return this;
    }

    protected CometResultSet time(long time) {
        this.time = time;
        return this;
    }

    public long time() {
        return this.time;
    }
}

package com.rocui.util.base.map;

import com.rocui.util.base.reflect.ObjectSnooper;
import com.rocui.util.jsonx.Jsonx;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

public class Jmap {

    private Map<String, Object> map = new HashMap<String, Object>();

    public static Jmap with(Properties properties) {
        Map<String, Object> mp = new HashMap<String, Object>();
        Enumeration er = properties.propertyNames();
        while (er.hasMoreElements()) {
            String paramName = (String) er.nextElement();
            mp.put(paramName, properties.getProperty(paramName));
        }
        return new Jmap(mp);
    }

    public static Jmap with(String jsonstr) throws Exception {
        HashMap<String, Object> x = Jsonx.create(jsonstr).toHashMap();
        return new Jmap(x);
    }

    public static Jmap with(String key, Object value) {
        return new Jmap(key, value);
    }

    public static Jmap with(HashMap<String, Object> map) {
        return new Jmap(map);
    }

    public static Jmap with(Map<String, Object> map) {
        return new Jmap(map);
    }

    public static Jmap with(Object object) {
        return new Jmap(object);
    }

    private Jmap(Object object) {
        Map<String, Object> x = ObjectSnooper.snoop(object).toHashMap();
        this.map.putAll(x);
    }

    private Jmap(String key, Object value) {
        this.put(key, value);
    }

    private Jmap(HashMap<String, Object> map) {
        this.map.putAll(map);
    }

    private Jmap(Map<String, Object> map) {
        this.map.putAll(map);
    }

    public Jmap each(JmapEach each) {
        for (Entry<String, Object> set : this.map.entrySet()) {
            boolean isbreak = each.each(set.getKey(), set.getValue());
            if (isbreak) {
                break;
            }
        }
        return this;
    }

    public Jmap put(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

    public Jmap put(HashMap<String, Object> map) {
        this.map.putAll(map);
        return this;
    }

    public Jmap put(Map<String, Object> map) {
        this.map.putAll(map);
        return this;
    }

    public Jmap put(Object object) {
        Map<String, Object> map = ObjectSnooper.snoop(object).toHashMap();
        this.map.putAll(map);
        return this;
    }

    public boolean has(String key) {
        return this.map.containsKey(key);
    }

    public Jmap remove(String key) {
        if (this.has(key)) {
            this.map.remove(key);
        }
        return this;
    }

    public Jmap clear() {
        this.map.clear();
        return this;
    }

    public Object get(String key) {
        return this.map.get(key);
    }

    public Jmap set(Object object) {
        ObjectSnooper.snoop(object).set(this.hashMap());
        return this;
    }

    public Map<String, Object> map() {
        return this.map;
    }

    public HashMap<String, Object> hashMap() {
        return (HashMap<String, Object>) this.map;
    }

    public String toJson() {
        return Jsonx.create(this.map).toString();
    }

    public Properties toProperties() {
        Properties props = new Properties();
        for (Entry<String, Object> m : this.map.entrySet()) {
            props.setProperty(m.getKey(), m.getValue().toString());
        }
        return props;
    }

    public static void main(String[] args) {
        HashMap<String, Object> c = new HashMap<String, Object>();
//        Jmap map = new Jmap(c);
    }
}

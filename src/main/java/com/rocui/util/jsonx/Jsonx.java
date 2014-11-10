package com.rocui.util.jsonx;

import com.mongodb.DBObject;
import com.rocui.util.base.reflect.ObjectSnooper;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Jsonx {

    public static final String TYPE_ARRAY = "a";
    public static final String TYPE_OBJECT = "o";
    public static final String TYPE_END = "e";
    private Object cache = null;
    private String type;
    private JSONObject jsonobject;
    private JSONArray jsonarray;
    private Jsonx parent = null;
    private Object result;

    public static Jsonx create(String str) throws Exception {
        Jsonx flow;
        if (str != "" && str.length() > 0) {
            if (str.charAt(0) == '[') {
                JSONArray c = JSONArray.fromObject(str);
                flow = new Jsonx(Jsonx.TYPE_ARRAY, c, null);
            } else if (str.charAt(0) == '{') {
                JSONObject c = JSONObject.fromObject(str);
                flow = new Jsonx(Jsonx.TYPE_OBJECT, c, null);
            } else {
                flow = new Jsonx(Jsonx.TYPE_END, str, null);
            }
        } else {
            JSONObject c = JSONObject.fromObject("{}");
            flow = new Jsonx(Jsonx.TYPE_OBJECT, c, null);
        }
        return flow;
    }

    public static Jsonx create(File file) throws Exception {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file.getAbsolutePath()), "UTF-8"));
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return Jsonx.create(sb.toString());
    }

    public static Jsonx create(File file, String encoding) throws Exception {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file.getAbsolutePath()), encoding));
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return Jsonx.create(sb.toString());
    }

    public static Jsonx create() throws Exception {
        JSONObject c = JSONObject.fromObject("{}");
        Jsonx flow = new Jsonx(Jsonx.TYPE_OBJECT, c, null);
        return flow;
    }

    public static Jsonx create(Map<String, Object> map) {
        JSONObject c = JSONObject.fromObject(map);
        Jsonx flow = new Jsonx(Jsonx.TYPE_OBJECT, c, null);
        return flow;
    }

    public static Jsonx create(Jsonx flow) throws Exception {
        Jsonx flowx;
        String str = flow.toString();
        if (str != "" && str.length() > 0) {
            if (str.charAt(0) == '[') {
                JSONArray c = JSONArray.fromObject(str);
                flowx = new Jsonx(Jsonx.TYPE_ARRAY, c, null);
            } else if (str.charAt(0) == '{') {
                JSONObject c = JSONObject.fromObject(str);
                flowx = new Jsonx(Jsonx.TYPE_OBJECT, c, null);
            } else {
                flowx = new Jsonx(Jsonx.TYPE_END, str, null);
            }
        } else {
            JSONObject c = JSONObject.fromObject("{}");
            flowx = new Jsonx(Jsonx.TYPE_OBJECT, c, null);
        }
        return flowx;
    }

    public static Jsonx create(List<?> list) throws Exception {
        JSONArray array = JSONArray.fromObject("[]");
        for (Object t : list) {
            array.add(t);
        }
        Jsonx flow = new Jsonx(Jsonx.TYPE_ARRAY, array, null);
        return flow;
    }

    public static Jsonx create(Object bean) {
        JSONObject c = JSONObject.fromObject(bean);
        Jsonx flow = new Jsonx(Jsonx.TYPE_OBJECT, c, null);
        return flow;
    }

    public static Jsonx create(Object bean, boolean includesuperclass) {
        JSONObject c = JSONObject.fromObject(bean);
        Jsonx flow = new Jsonx(Jsonx.TYPE_OBJECT, c, null);
        return flow;
    }

    public static Jsonx create(JSONArray array) {
        Jsonx flow = new Jsonx(Jsonx.TYPE_ARRAY, array, null);
        return flow;
    }

    private Jsonx(String type, JSONObject obj, Jsonx parent) {
        this.type = type;
        this.jsonobject = obj;
        this.cache = obj;
        this.parent = parent;
    }

    private Jsonx(String type, JSONArray array, Jsonx parent) {
        this.type = type;
        this.jsonarray = array;
        this.cache = array;
        this.parent = parent;
    }

    private Jsonx(String type, Object result, Jsonx parent) {
        this.type = type;
        this.result = result;
        this.cache = result;
        this.parent = parent;
    }

    private Jsonx(String type, Jsonx parent) {
        this.type = type;
        this.result = null;
        this.cache = null;
        this.parent = parent;
    }

    public Jsonx get(String key) {
        Jsonx flow = this;
        if (this.type.equals(Jsonx.TYPE_OBJECT)) {
            if (this.jsonobject.has(key)) {
                Object c = this.jsonobject.get(key);
                if (c instanceof JSONArray) {
                    flow = new Jsonx(Jsonx.TYPE_ARRAY, (JSONArray) c, this);
                } else if (c instanceof JSONObject) {
                    flow = new Jsonx(Jsonx.TYPE_OBJECT, (JSONObject) c, this);
                } else {
                    flow = new Jsonx(Jsonx.TYPE_END, c, this);
                }
            } else {
                flow = new Jsonx(Jsonx.TYPE_END, this);
            }
        } else {
            flow = new Jsonx(Jsonx.TYPE_END, this);
        }
        return flow;
    }

    public Jsonx get(int index) throws Exception {
        Jsonx flow = this;
        if (this.type.equals(Jsonx.TYPE_ARRAY)) {
            Object c = this.jsonarray.get(index);
            if (c instanceof JSONArray) {
                flow = new Jsonx(Jsonx.TYPE_ARRAY, (JSONArray) c, this);
            } else if (c instanceof JSONObject) {
                flow = new Jsonx(Jsonx.TYPE_OBJECT, (JSONObject) c, this);
            } else {
                flow = new Jsonx(Jsonx.TYPE_END, c, this);
            }
        } else {
            flow = new Jsonx(Jsonx.TYPE_END, this);
        }
        return flow;
    }

    public boolean hasKey(String key) {
        if (this.type.equals(Jsonx.TYPE_OBJECT)) {
            return this.jsonobject.has(key);
        } else {
            return false;
        }
    }

    public Jsonx set(String key, Jsonx json) {
        if (this.type.equals(Jsonx.TYPE_OBJECT)) {
            if (json.type.equals(Jsonx.TYPE_ARRAY)) {
                this.jsonobject.put(key, json.jsonarray);
            } else if (json.type.equals(Jsonx.TYPE_OBJECT)) {
                this.jsonobject.put(key, json.jsonobject);
            } else {
                this.jsonobject.put(key, json.result);
            }
        }
        return this;
    }

    public Jsonx set(String key, Object value) throws Exception {
        if (this.type.equals(Jsonx.TYPE_OBJECT)) {
            if (value instanceof Jsonx) {
                Jsonx cdd = (Jsonx) value;
                if (cdd.type.equals(Jsonx.TYPE_ARRAY)) {
                    this.jsonobject.put(key, cdd.jsonarray);
                } else if (cdd.type.equals(Jsonx.TYPE_OBJECT)) {
                    this.jsonobject.put(key, cdd.jsonobject);
                } else {
                    this.jsonobject.put(key, cdd.result);
                }
            } else {
                this.jsonobject.put(key, value);
            }
        }
        return this;
    }

    public Jsonx set(HashMap<String, Object> map) throws Exception {
        if (this.type.equals(Jsonx.TYPE_OBJECT)) {
            for (Entry<String, Object> mapx : map.entrySet()) {
                Object cd = mapx.getValue();
                String key = mapx.getKey();
                if (cd instanceof Jsonx) {
                    Jsonx cdd = (Jsonx) cd;
                    if (cdd.type.equals(Jsonx.TYPE_ARRAY)) {
                        this.jsonobject.put(key, cdd.jsonarray);
                    } else if (cdd.type.equals(Jsonx.TYPE_OBJECT)) {
                        this.jsonobject.put(key, cdd.jsonobject);
                    } else {
                        this.jsonobject.put(key, cdd.result);
                    }
                } else {
                    this.jsonobject.put(key, cd);
                }
            }
        }
        return this;
    }

    public Jsonx set(String key, String jsonstr) throws Exception {
        if (jsonstr.charAt(0) == '{') {
            if (this.type.equals(Jsonx.TYPE_OBJECT)) {
                JSONObject q = JSONObject.fromObject(jsonstr);
                this.jsonobject.put(key, q);
            }
        } else if (jsonstr.charAt(0) == '[') {
            if (this.type.equals(Jsonx.TYPE_OBJECT)) {
                JSONArray q = JSONArray.fromObject(jsonstr);
                this.jsonobject.put(key, q);
            }
        } else {
            this.jsonobject.put(key, jsonstr);
        }
        return this;
    }

    public Jsonx add(List<HashMap<String, Object>> map) {
        if (this.type.equals(Jsonx.TYPE_ARRAY)) {
            for (HashMap<String, Object> c : map) {
                JSONObject obj = new JSONObject();
                for (Entry<String, Object> mapx : c.entrySet()) {
                    Object cd = mapx.getValue();
                    String key = mapx.getKey();
                    if (cd instanceof Jsonx) {
                        Jsonx cdd = (Jsonx) cd;
                        if (cdd.type.equals(Jsonx.TYPE_ARRAY)) {
                            obj.put(key, cdd.jsonarray);
                        } else if (cdd.type.equals(Jsonx.TYPE_OBJECT)) {
                            obj.put(key, cdd.jsonobject);
                        } else {
                            obj.put(key, cdd.result);
                        }
                    } else {
                        obj.put(key, cd);
                    }
                }
                this.jsonarray.add(obj);
            }
        }
        return this;
    }

    public Jsonx add(HashMap<String, Object> map) {
        if (this.type.equals(Jsonx.TYPE_ARRAY)) {
            JSONObject obj = new JSONObject();
            for (Entry<String, Object> mapx : map.entrySet()) {
                Object cd = mapx.getValue();
                String key = mapx.getKey();
                if (cd instanceof Jsonx) {
                    Jsonx cdd = (Jsonx) cd;
                    if (cdd.type.equals(Jsonx.TYPE_ARRAY)) {
                        obj.put(key, cdd.jsonarray);
                    } else if (cdd.type.equals(Jsonx.TYPE_OBJECT)) {
                        obj.put(key, cdd.jsonobject);
                    } else {
                        obj.put(key, cdd.result);
                    }
                } else {
                    obj.put(key, cd);
                }
            }
            this.jsonarray.add(obj);
        }
        return this;
    }

    public Jsonx add(Jsonx json) {
        if (this.type.equals(Jsonx.TYPE_ARRAY)) {
            if (json.type.equals(Jsonx.TYPE_OBJECT)) {
                this.jsonarray.add(json.jsonobject);
            } else if (json.type.equals(Jsonx.TYPE_ARRAY)) {
                this.jsonarray.addAll(json.jsonarray);
            }
        }
        return this;
    }

    public Jsonx add(String key, Object value) {
        if (this.type.equals(Jsonx.TYPE_ARRAY)) {
            JSONObject obj = new JSONObject();
            if (value instanceof Jsonx) {
                Jsonx m = (Jsonx) value;
                if (m.type.equals(Jsonx.TYPE_OBJECT)) {
                    obj.put(key, m.jsonobject);
                } else if (m.type.equals(Jsonx.TYPE_ARRAY)) {
                    obj.put(key, m.jsonarray);
                } else {
                    obj.put(key, m.result);
                }
            } else {
                obj.put(key, value);
            }
            this.jsonarray.add(obj);
        } else if (this.type.equals(Jsonx.TYPE_OBJECT)) {
            if (value instanceof Jsonx) {
                Jsonx m = (Jsonx) value;
                if (m.type.equals(Jsonx.TYPE_OBJECT)) {
                    this.jsonobject.put(key, m.jsonobject);
                } else if (m.type.equals(Jsonx.TYPE_ARRAY)) {
                    this.jsonobject.put(key, m.jsonarray);
                } else {
                    this.jsonobject.put(key, m.result);
                }
            } else {
                this.jsonobject.put(key, value);
            }
        }
        return this;
    }

    public Jsonx add(String jsonstr) throws Exception {
        if (jsonstr.charAt(0) == '{') {
            if (this.type.equals(Jsonx.TYPE_ARRAY)) {
                JSONObject o = JSONObject.fromObject(jsonstr);
                this.jsonarray.add(o);
            }
        } else if (jsonstr.charAt(0) == '[') {
            if (this.type.equals(Jsonx.TYPE_ARRAY)) {
                JSONArray o = JSONArray.fromObject(jsonstr);
                this.jsonarray.add(o);
            }
        }
        return this;
    }

    public Jsonx remove(String key) {
        if (this.type.equals(Jsonx.TYPE_OBJECT)) {
            this.jsonobject.remove(key);
        }
        return this;
    }

    public Jsonx remove(int index) {
        if (this.type.equals(Jsonx.TYPE_ARRAY)) {
            this.jsonarray.remove(index);
        }
        return this;
    }

    public Jsonx empty() {
        if (this.type.equals(Jsonx.TYPE_ARRAY)) {
            while (this.jsonarray.size() > 0) {
                this.jsonarray.remove(0);
            }
        } else if (this.type.equals(Jsonx.TYPE_OBJECT)) {
            for (Iterator iter = this.jsonobject.keys(); iter.hasNext();) {
                String key = (String) iter.next();
                this.jsonobject.remove(key);
            }
        }
        return this;
    }

    public Object data() throws Exception {
        return this.result;
    }

    public int toInt() {
        if (this.type.equals(Jsonx.TYPE_END)) {
            if (this.result != null) {
                return Integer.parseInt(this.result.toString());
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

    public Boolean toBoolean() {
        if (this.type.equals(Jsonx.TYPE_END)) {
            if (this.result != null) {
                return Boolean.parseBoolean(this.result.toString());
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public Long toLong() {
        if (this.type.equals(Jsonx.TYPE_END)) {
            if (this.result != null) {
                return Long.parseLong(this.result.toString());
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public Double toDouble() {
        if (this.type.equals(Jsonx.TYPE_END)) {
            if (this.result != null) {
                return Double.parseDouble(this.result.toString());
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public String type() {
        return this.type;
    }

    public boolean isNull() {
        return this.cache == null;
    }

    public Jsonx parent() {
        return this.parent;
    }

    public int length() {
        if (this.type.equals(Jsonx.TYPE_ARRAY)) {
            return this.jsonarray.size();
        } else if (this.type.equals(Jsonx.TYPE_OBJECT)) {
            return this.jsonobject.size();
        } else {
            return 0;
        }
    }

    public Jsonx each(JsonEach each) throws Exception {
        if (this.type.equals(Jsonx.TYPE_OBJECT)) {
            Iterator<?> iterator = this.jsonobject.keys();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                boolean isbreak = each.each(key, this.get(key));
                if (isbreak) {
                    break;
                }
            }
        }
        return this;
    }

    public Jsonx each(JsonEachArray array) throws Exception {
        if (this.type.equals(Jsonx.TYPE_ARRAY)) {
            for (int i = 0; i < this.jsonarray.size(); i++) {
                boolean isbreak = array.each(i, this.get(i));
                if (isbreak) {
                    break;
                }
            }
        }
        return this;
    }

    public String result() {
        return this.cache.toString();
    }

    @Override
    public String toString() {
        if (this.type.equals(Jsonx.TYPE_OBJECT)) {
            return this.jsonobject.toString();
        } else if (this.type.equals(Jsonx.TYPE_ARRAY)) {
            return this.jsonarray.toString();
        } else {
            if (this.result != null) {
                return this.result.toString();
            } else {
                return null;
            }
        }
    }

    public HashMap<String, Object> toHashMap() throws Exception {
        if (this.type.equals(Jsonx.TYPE_OBJECT)) {
            return this._toHashMap(this.jsonobject);
        } else {
            return null;
        }
    }

    public List<HashMap<String, Object>> toList() throws Exception {
        if (this.type.equals(Jsonx.TYPE_ARRAY)) {
            return this._toList(this.jsonarray);
        } else {
            return null;
        }
    }

    public DBObject toDBObject() {
        if (!this.type.equals(Jsonx.TYPE_END)) {
            return (DBObject) com.mongodb.util.JSON.parse(this.toString());
        } else {
            return null;
        }
    }

    public void toFile(String filepath, String charset) throws Exception {
        String charsetx = charset == null ? "utf-8" : charset;
        File file = new File(filepath);
        if (!file.exists()) {
            file.getParentFile().mkdir();
            file.createNewFile();
        }
        FileOutputStream out = new FileOutputStream(new File(filepath));
        out.write(this.toString().getBytes(charsetx));
        out.close();
    }

    public void toFile(String filepath) throws Exception {
        String charsetx = "utf-8";
        File file = new File(filepath);
        if (!file.exists()) {
            file.getParentFile().mkdir();
            file.createNewFile();
        }
        FileOutputStream out = new FileOutputStream(new File(filepath));
        out.write(this.toString().getBytes(charsetx));
        out.close();
    }

    public <T> T toObject(T obj) throws Exception {
        if (this.type.equals(Jsonx.TYPE_OBJECT)) {
            Iterator<?> iterator = this.jsonobject.keys();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                ObjectSnooper ow = ObjectSnooper.snoop(obj);
                ow.set(key, this.jsonobject.getString(key));
            }
        }
        return obj;
    }

    private HashMap<String, Object> _toHashMap(JSONObject jsonObject) throws Exception {
        HashMap<String, Object> result = new HashMap<String, Object>();
        Iterator<Object> iterator = jsonObject.keys();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            result.put(key, jsonObject.get(key).toString());
        }
        return result;
    }

    private List<HashMap<String, Object>> _toList(JSONArray array) throws Exception {
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < array.size(); i++) {
            list.add(this._toHashMap(array.getJSONObject(i)));
        }
        return list;
    }

    public boolean isArray() {
        return this.type.equals(Jsonx.TYPE_ARRAY);
    }

    public boolean isObject() {
        return this.type.equals(Jsonx.TYPE_OBJECT);
    }

    public void fullEach(String list, JsonFullEach each) throws Exception {
        if (this.isArray()) {
            this.forit(this.jsonarray, list, each);
        } else if (this.isObject()) {
            each.each(this);
        }
    }

    public List<Jsonx> find(String list, String withKey) throws Exception {
        List<Jsonx> xlist = new ArrayList<Jsonx>();
        this.fullEach(list, new JsonFullEach(xlist, withKey) {
            @Override
            public void each(Jsonx json) {
                List<Jsonx> xlist = (List<Jsonx>) this.arguments[0];
                String withKey = (String) this.arguments[1];
                if (json.hasKey(withKey)) {
                    xlist.add(json);
                }
            }
        });
        return xlist;
    }

    public List<Jsonx> find(String list, String key, String value) throws Exception {
        List<Jsonx> xlist = new ArrayList<Jsonx>();
        this.fullEach(list, new JsonFullEach(xlist, key, value) {
            @Override
            public void each(Jsonx json) {
                List<Jsonx> xlist = (List<Jsonx>) this.arguments[0];
                String withKey = (String) this.arguments[1];
                String value = (String) this.arguments[2];
                if (json.hasKey(withKey)) {
                    Jsonx n = json.get(withKey);
                    if (!n.isNull() && n.toString().equals(value)) {
                        xlist.add(json);
                    }
                }
            }
        });
        return xlist;
    }

    private void forit(Object k, String list, JsonFullEach each) throws Exception {
        if (k instanceof JSONArray) {
            JSONArray a = (JSONArray) k;
            for (int i = 0; i < a.size(); i++) {
                Object t = a.get(i);
                forit(t, list, each);
            }
        } else if (k instanceof JSONObject) {
            JSONObject x = (JSONObject) k;
            Object t = x.get(list);
            each.each(Jsonx.create(x));
            forit(t, list, each);
        }
    }

    public static void main(String[] args) throws Exception {
        List<Jsonx> list = Jsonx.create(new File("H:\\java\\web\\Jdoc\\build\\web\\WEB-INF\\classes\\menumapping.json")).find("list", "view", "wuliucom");
        for (Jsonx x : list) {
            System.out.println(x);
        }
    }
}

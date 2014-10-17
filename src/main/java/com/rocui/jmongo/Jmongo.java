package com.rocui.jmongo;

import com.mongodb.BasicDBList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.WriteResult;
import com.rocui.jmongo.base.Query;
import com.rocui.jmongo.base.Query.QueryInfo;
import com.rocui.jmongo.base.RowEach;
import com.rocui.jmongo.base.RowEachArray;
import com.rocui.util.jsonx.Jsonx;
import java.io.File;
import java.util.Map;

public class Jmongo {

    private DB db;
    private static final ThreadLocal localMongo = new ThreadLocal();
    private static String host = null;
    private static String port = null;
    private static String username = null;
    private static String password = null;
    private static String defaultDB = null;

    public static Jmongo db(String dbname) throws Exception {
        Jmongo.configPath(null);
        return new Jmongo(dbname);
    }

    public static Table withTable(String tableName) throws Exception {
        Jmongo.configPath(null);
        return new Jmongo(Jmongo.defaultDB).table(tableName);
    }

    public static void closeSession() throws Exception {
        Jmongo.configPath(null);
        new Jmongo(Jmongo.defaultDB).close();
    }

    public static void configPath(String filepath) throws Exception {
        if (filepath == null) {
            if (Jmongo.host == null) {
                File file = (new File(Jmongo.class.getClassLoader().getResource("database.json").getFile()));
                Jsonx json = Jsonx.create(file);
                Jmongo.host = json.get("host").toString();
                Jmongo.port = json.get("port").toString();
                Jmongo.username = json.get("username").toString();
                Jmongo.password = json.get("password").toString();
                Jmongo.defaultDB = json.get("defaultDB").toString();
            }
        } else {
            File file = new File(filepath);
            Jsonx json = Jsonx.create(file);
            Jmongo.host = json.get("host").toString();
            Jmongo.port = json.get("port").toString();
            Jmongo.username = json.get("username").toString();
            Jmongo.password = json.get("password").toString();
            Jmongo.defaultDB = json.get("defaultDB").toString();
        }
    }

    public Jmongo(String dbname) throws Exception {
        Mongo mongo = this.getSession();
        this.db = mongo.getDB(dbname);
        this.db.authenticate(Jmongo.username, Jmongo.password.toCharArray());
    }

    public Table table(String tablename) throws Exception {
        return new Table(tablename);
    }

    public DB getDB() {
        return this.db;
    }

    public final Mongo getSession() throws Exception {
        Object x = Jmongo.localMongo.get();
        Mongo mongo;
        if (x == null) {
            mongo = new Mongo(Jmongo.host, Integer.parseInt(Jmongo.port));
            Jmongo.localMongo.set(mongo);
        } else {
            mongo = (Mongo) x;
        }
        return mongo;
    }

    public List<String> tables() throws Exception {
        return this.getSession().getDatabaseNames();
    }

    public void close() throws Exception {
        this.getSession().close();
        Jmongo.localMongo.remove();
    }

    public class Table {

        public static final String ORDER_DESC = "-1";
        public static final String ORDER_ASC = "1";
        private DBCollection collection;
        private Result result;

        private Table(String tablename) {
            this.collection = db.getCollection(tablename);
        }

        private Table(DBCollection collection) {
            this.collection = collection;
        }

        public void close() throws Exception {
            Jmongo.this.getSession().close();
        }

        public Table drop() {
            this.collection.drop();
            return this;
        }

        public Table dropIndex(String index) {
            this.collection.dropIndex(index);
            return this;
        }

        public Table dropIndex(Query query) {
            this.collection.dropIndex(query.toDBObject());
            return this;
        }

        public Table dropAllIndex() {
            this.collection.dropIndexes();
            return this;
        }

        public Table add(String jsonstr) throws Exception {
            db.requestStart();
            WriteResult result = this.collection.insert(Jsonx.create(jsonstr).toDBObject());
            this.result = new Result(result);
            db.requestDone();
            return new Table(this.collection);
        }

        public Table add(Object obj) {
            db.requestStart();
            WriteResult result = this.collection.insert(Jsonx.create(obj).toDBObject());
            this.result = new Result(result);
            db.requestDone();
            return new Table(this.collection);
        }

        public Table add(String... jsonstrs) throws Exception {
            List<DBObject> list = new ArrayList<DBObject>();
            for (String jsonstr : jsonstrs) {
                list.add(Jsonx.create(jsonstr).toDBObject());
            }
            db.requestStart();
            WriteResult result = this.collection.insert(list);
            this.result = new Result(result);
            db.requestDone();
            return new Table(this.collection);
        }

        public Table add(HashMap<String, Object>... maps) {
            List<DBObject> list = new ArrayList<DBObject>();
            for (HashMap<String, Object> map : maps) {
                list.add(this.Map2DBObject(map));
            }
            db.requestStart();
            WriteResult result = this.collection.insert(list);
            this.result = new Result(result);
            db.requestDone();
            return new Table(this.collection);
        }

        public Table add(List<HashMap<String, Object>> list) {
            List<DBObject> x = this.list2DBObject(list);
            db.requestStart();
            WriteResult result = this.collection.insert(x);
            this.result = new Result(result);
            db.requestDone();
            return new Table(this.collection);
        }

        public QuerySet find(Query query) throws Exception {
            QueryInfo info = query.getQueryInfo();
            List<DBObject> list;
            if (info.getSort().size() > 0) {
                list = this.collection.find(query.toDBObject()).skip(info.getBegin()).limit(info.getSize()).sort(Jsonx.create(info.getSort()).toDBObject()).toArray();
            } else {
                list = this.collection.find(query.toDBObject()).skip(info.getBegin()).limit(info.getSize()).toArray();
            }
            return new QuerySet(this, list);
        }

        public QuerySet find() {
            return new QuerySet(this, this.collection.find().toArray());
        }

        public QuerySet findOne(Query query) {
            List<DBObject> list = new ArrayList<DBObject>();
            DBObject c = this.collection.findOne(query.toDBObject());
            list.add(c);
            return new QuerySet(this, list);
        }

        public Table update(Query query, Object value) {
            BasicDBObject o = new BasicDBObject("$set", value);
            db.requestStart();
            WriteResult result = this.collection.update(query.toDBObject(), o);
            this.result = new Result(result);
            db.requestDone();
            return new Table(this.collection);
        }

        public Table remove(Query query) {
            db.requestStart();
            WriteResult result = this.collection.remove(query.toDBObject());
            this.result = new Result(result);
            db.requestDone();
            return new Table(this.collection);
        }

        public Result result() {
            return this.result;
        }

        private DBObject Map2DBObject(HashMap<String, Object> map) {
            BasicDBObject obj = new BasicDBObject();
            if (map != null) {
                for (Entry<String, Object> x : map.entrySet()) {
                    obj.put(x.getKey(), x.getValue());
                }
            }
            return obj;
        }

        private List<DBObject> list2DBObject(List<HashMap<String, Object>> list) {
            List<DBObject> listx = new ArrayList<DBObject>();
            if (list != null) {
                for (HashMap<String, Object> map : list) {
                    listx.add(this.Map2DBObject(map));
                }
            }
            return listx;
        }

        public Long count() {
            return this.collection.count();
        }

        public Index index(String name) {
            return new Index(this.collection, name);
        }

        public int indexSize() {
            return this.collection.getIndexInfo().size();
        }
    }

    public class Index {

        private DBCollection collection;
        private String key;
        private int orderType;

        public Index(DBCollection collection, String key) {
            this.collection = collection;
            this.key = key;
        }

        public Index index(String key) {
            return new Index(this.collection, key);
        }

        public Index desc() {
            this.collection.createIndex(new BasicDBObject(this.key, -1));
            return this;
        }

        public Index asc() {
            this.collection.createIndex(new BasicDBObject(this.key, 1));
            return this;
        }

        public Index remove(String name) {
            this.collection.dropIndex(name);
            return this;
        }

        public void close() throws Exception {
            Jmongo.this.close();
        }
    }

    public class QuerySet {

        private List<DBObject> list;
        private Table table;

        public QuerySet(Table table, List<DBObject> list) {
            this.list = list;
            this.table = table;
        }

        public int size() {
            return this.list.size();
        }

        public Row get(int index) throws Exception {
            if (index >= 0 && index < this.list.size()) {
                return new Row(this.table, this.list.get(index));
            } else {
                return null;
            }
        }

        public QuerySet forEach(RowEachArray each) throws Exception {
            for (int i = 0; i < this.list.size(); i++) {
                boolean isbreak = each.each(new Row(this.table, this.list.get(i)), i);
                if (isbreak) {
                    break;
                }
            }
            return this;
        }

        public void close() throws Exception {
            Jmongo.this.close();
        }
    }

    public class Row {

        public static final String TYPE_OBJECT = "object";
        public static final String TYPE_LIST = "list";
        public static final String TYPE_END = "end";
        private final DBObject original;
        private Row parent;
        private final Table table;
        private DBObject row;
        private BasicDBList rows;
        private Object value;
        private final String type;

        public Row(Table table, DBObject row) throws Exception {
            this.table = table;
            this.row = row;
            this.type = Row.TYPE_OBJECT;
            this.original = row;
        }

        public Row(Table table, DBObject row, String type, DBObject original, Row parent) {
            this.table = table;
            this.row = row;
            this.type = type;
            this.original = original;
            this.parent = parent;
        }

        public Row(Table table, BasicDBList rows, String type, DBObject original, Row parent) {
            this.table = table;
            this.rows = rows;
            this.type = type;
            this.original = original;
            this.parent = parent;
        }

        public Row(Table table, Object value, String type, DBObject original, Row parent) {
            this.table = table;
            this.value = value;
            this.type = type;
            this.original = original;
            this.parent = parent;
        }

        public boolean isList() {
            return this.row instanceof BasicDBList;
        }

        public Row set(String key, Object value) throws Exception {
            if (this.type.equals(Row.TYPE_OBJECT)) {
                this.row.put(key, value);
            }
            return this;
        }

        public Row set(HashMap<String, Object> map) throws Exception {
            if (this.type.equals(Row.TYPE_OBJECT)) {
                this.row.putAll(map);
            }
            return this;
        }

        public Map<String, Object> toMap() {
            return this.row.toMap();
        }

        public Row add(HashMap<String, Object> map) {
            if (this.type.equals(Row.TYPE_LIST)) {
                this.rows.add(map);
            }
            return this;
        }

        public Row add(List<HashMap<String, Object>> list) {
            if (this.type.equals(Row.TYPE_LIST)) {
                this.rows.addAll(list);
            }
            return this;
        }

        public Row add(int index, HashMap<String, Object> map) {
            if (this.type.equals(Row.TYPE_OBJECT)) {
                this.rows.add(index, map);
            }
            return this;
        }

        public Row add(int index, List<HashMap<String, Object>> list) {
            if (this.type.equals(Row.TYPE_LIST)) {
                this.rows.addAll(index, list);
            }
            return this;
        }

        public int save() {
            WriteResult result = this.table.collection.save(this.original);
            return result.getN();
        }

        public Row get(String name) {
            Object k = this.row.get(name);
            if (k instanceof BasicDBList) {
                return new Row(this.table, (BasicDBList) k, Row.TYPE_LIST, this.original, this);
            } else if (k instanceof DBObject) {
                return new Row(this.table, (DBObject) k, Row.TYPE_OBJECT, this.original, this);
            } else {
                return new Row(this.table, k, Row.TYPE_END, this.original, this);
            }
        }

        public String type() {
            return this.type;
        }

        public Row get(int index) {
            if (this.type.equals(Row.TYPE_LIST)) {
                Object o = this.rows.get(index);
                if (o instanceof BasicDBList) {
                    return new Row(this.table, (BasicDBList) this.rows.get(index), Row.TYPE_LIST, this.original, this);
                } else if (o instanceof DBObject) {
                    return new Row(this.table, (DBObject) this.rows.get(index), Row.TYPE_OBJECT, this.original, this);
                } else {
                    return new Row(this.table, this.rows.get(index), Row.TYPE_END, this.original, this);
                }
            } else {
                return this;
            }
        }

        public String id() {
            return this.get("_id").toString();
        }

        public Row remove(String key) {
            if (this.type.equals(Row.TYPE_OBJECT)) {
                this.row.removeField(key);
            }
            return this;
        }

        public Row remove(int index) {
            if (this.type.equals(Row.TYPE_LIST)) {
                this.rows.remove(index);
            }
            return this;
        }

        public Row parent() {
            return this.parent;
        }

        public Boolean toBoolean() {
            if (this.type.equals(Row.TYPE_END)) {
                return Boolean.parseBoolean(this.value.toString());
            } else {
                return null;
            }
        }

        public int size() {
            if (this.type.equals(Row.TYPE_LIST)) {
                return this.rows.size();
            } else if (this.type.equals(Row.TYPE_OBJECT)) {
                return this.row.toMap().size();
            } else {
                return 0;
            }
        }

        public Integer toInt() {
            if (this.type.equals(Row.TYPE_END)) {
                return Integer.parseInt(this.value.toString());
            } else {
                return null;
            }
        }

        public Double toDouble() {
            if (this.type.equals(Row.TYPE_END)) {
                return Double.parseDouble(this.value.toString());
            } else {
                return null;
            }
        }

        public Long toLong() {
            if (this.type.equals(Row.TYPE_END)) {
                return Long.parseLong(this.value.toString());
            } else {
                return null;
            }
        }

        public List<Object> toList() {
            if (this.type.equals(Row.TYPE_LIST)) {
                return this.rows.subList(0, this.rows.size());
            } else {
                return null;
            }
        }

        public List<Object> subList(int fromIndex, int endIndex) {
            if (this.type.equals(Row.TYPE_LIST)) {
                return this.rows.subList(fromIndex, endIndex);
            } else {
                return null;
            }
        }

        public Object data() {
            if (this.type.equals(Row.TYPE_LIST)) {
                return this.rows;
            } else if (this.type.equals(Row.TYPE_OBJECT)) {
                return this.row;
            } else {
                return this.value;
            }
        }

        public DBObject toDBObject() {
            if (this.type.equals(Row.TYPE_LIST)) {
                return this.rows;
            } else if (this.type.equals(Row.TYPE_OBJECT)) {
                return this.row;
            } else {
                return null;
            }
        }

        public <T> T toObject(T obj) throws Exception {
            if (this.type.equals(Row.TYPE_OBJECT)) {
                return Jsonx.create(this.row.toString()).toObject(obj);
            } else {
                return null;
            }
        }

        public Row each(RowEach each) {
            if (this.type.equals(Row.TYPE_OBJECT)) {
                Map<String, Object> map = this.row.toMap();
                for (Entry<String, Object> x : map.entrySet()) {
                    boolean isbreak = each.each(this.get(x.getKey()), x.getKey());
                    if (isbreak) {
                        break;
                    }
                }
            }
            return this;
        }

        public Row each(final RowEachArray each) throws Exception {
            if (this.type.equals(Row.TYPE_LIST)) {
                for (int i = 0; i < this.rows.size(); i++) {
                    boolean isbreak = each.each(this.get(i), i);
                    if (isbreak) {
                        break;
                    }
                }
            }
            return this;
        }

        @Override
        public String toString() {
            if (this.type.equals(Row.TYPE_LIST)) {
                return this.rows.toString();
            } else if (this.type.equals(Row.TYPE_OBJECT)) {
                return this.row.toString();
            } else {
                return this.value.toString();
            }
        }

        public void close() throws Exception {
            Jmongo.this.close();
        }
    }

    public class Result {

        private final int num;
        private final Object err;
        private final Object ok;

        private Result(WriteResult result) {
            this.num = result.getN();
            this.err = result.getField("err");
            this.ok = result.getField("ok");
        }

        public int num() {
            return this.num;
        }

        public Object error() {
            return this.err;
        }

        public Object ok() {
            return this.ok;
        }

        public void close() throws Exception {
            Jmongo.this.getSession().close();
        }
    }
}

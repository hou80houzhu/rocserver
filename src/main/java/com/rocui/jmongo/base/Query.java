package com.rocui.jmongo.base;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import org.bson.types.ObjectId;

public class Query {

    private final HashMap<String, Factor> xft = new HashMap<String, Factor>();
    private DBObject factor = new BasicDBObject();
    private int begin = 0;
    private int size = 100;
    private final HashMap<String, Integer> sort = new HashMap<String, Integer>();

    public static Factor find(String fieldName) throws Exception {
        return new Query()._find(fieldName);
    }

    public static Factor id(String id) throws Exception {
        return new Query()._id(id);
    }

    public static Factor with(String jsonstr) throws Exception {
        DBObject obj = (DBObject) com.mongodb.util.JSON.parse(jsonstr);
        return new Query(jsonstr)._find(null);
    }

    public QueryInfo getQueryInfo() {
        return new QueryInfo(this.begin, this.size, this.sort);
    }

    public Query() {
    }

    public Query(String jsonstr) {
        this.factor = (DBObject) com.mongodb.util.JSON.parse(jsonstr);
    }

    protected Factor _find(String fieldName) throws Exception {
        return new Factor().find(fieldName);
    }

    protected Factor _id(String id) throws Exception {
        return new Factor().id(id);
    }

    public DBObject toDBObject() {
        DBObject xx = new BasicDBObject();
        xx.putAll(this.factor);
        for (Entry<String, Factor> map : this.xft.entrySet()) {
            Factor fat = map.getValue();
            if (fat.isset) {
                xx.put(fat.key, fat.object);
            }
        }
        return xx;
    }

    @Override
    public String toString() {
        return this.toDBObject().toString();
    }

    public class Factor {

        private DBObject object = new BasicDBObject();
        private String key;
        private boolean isset = true;

        public Factor() {
            isset = false;
        }

        public Factor(String key) {
            this.key = key;
        }

        public Factor(DBObject obj) {
            Query.this.factor = obj;
        }

        public Factor id(String id) throws Exception {
            Query.this.factor.put("_id", new ObjectId(id));
            return this;
        }

        public Factor eq(Object value) throws Exception {
            Query.this.factor.put(this.key, value);
            this.isset = false;
            return this;
        }

        public Factor gt(Object value) {
            this.object.put("$gt", value);
            return this;
        }

        public Factor lt(Object value) {
            this.object.put("$lt", value);
            return this;
        }

        public Factor gte(Object value) {
            this.object.put("$gte", value);
            return this;
        }

        public Factor lte(Object value) {
            this.object.put("$lte", value);
            return this;
        }

        public Factor ne(Object value) {
            this.object.put("$ne", value);
            return this;
        }

        public Factor mod(int value, int value2) {
            int[] x = new int[]{value, value2};
            this.object.put("$mod", x);
            return this;
        }

        public Factor all(List<Object> list) {
            this.object.put("$all", list);
            return this;
        }

        public Factor all(Object[] array) {
            this.object.put("$all", array);
            return this;
        }

        public Factor in(List<Object> list) {
            this.object.put("$in", list);
            return this;
        }

        public Factor nin(Object[] array) {
            this.object.put("$nin", array);
            return this;
        }

        public Factor nin(List<Object> list) {
            this.object.put("$nin", list);
            return this;
        }

        public Factor in(Object[] array) {
            this.object.put("$in", array);
            return this;
        }

        public Factor size(int size) {
            this.object.put("$size", size);
            return this;
        }

        public Factor exists() {
            this.object.put("$exists", true);
            return this;
        }

        public Factor unexists() {
            this.object.put("$exists", false);
            return this;
        }

        public Factor elemMatch(String querystr) {
            this.object.put("$elemMatch", querystr);
            return this;
        }

        public Factor elemMatch(Query query) {
            this.object.put("$elemMatch", query.toString());
            return this;
        }

        public Factor not(String querystr) {
            this.object.put("$not", querystr);
            return this;
        }

        public Factor not(Query query) {
            this.object.put("$not", query.toString());
            return this;
        }

        public Factor or(String querystr) {
            this.object.put("$or", querystr);
            return this;
        }

        public Factor or(Query query) {
            this.object.put("$or", query.toString());
            return this;
        }

        public Factor find(String key) throws Exception {
            Factor x = new Factor(key);
            Query.this.xft.put(key, x);
            return x;
        }

        public Factor limit(int begin, int size) {
            Query.this.begin = begin;
            Query.this.size = size;
            return this;
        }

        public Sort sort(String name) {
            return new Sort(name);
        }

        @Override
        public String toString() {
            return Query.this.toString();
        }

        public DBObject toDBObject() {
            return Query.this.toDBObject();
        }

        public Query query() {
            return Query.this;
        }
    }

    public class Sort {

        private String name;

        public Sort(String name) {
            this.name = name;
        }

        public Sort sort(String name) {
            return new Sort(name);
        }

        public Sort desc() {
            Query.this.sort.put(this.name, -1);
            return this;
        }

        public Sort asc() {
            Query.this.sort.put(this.name, 1);
            return this;
        }

        public Query query() {
            return Query.this;
        }

        @Override
        public String toString() {
            return Query.this.toString();
        }
    }

    public class QueryInfo {

        private int size;
        private int begin;
        private HashMap<String, Integer> sort;

        public QueryInfo(int begin, int size, HashMap<String, Integer> sort) {
            this.size = size;
            this.begin = begin;
            this.sort = sort;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getBegin() {
            return begin;
        }

        public void setBegin(int begin) {
            this.begin = begin;
        }

        public HashMap<String, Integer> getSort() {
            return sort;
        }

        public void setSort(HashMap<String, Integer> sort) {
            this.sort = sort;
        }
    }
}

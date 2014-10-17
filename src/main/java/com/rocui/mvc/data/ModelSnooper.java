package com.rocui.mvc.data;

import com.rocui.mvc.annotation.Id;
import com.rocui.mvc.annotation.Model;
import com.rocui.util.base.reflect.ObjectSnooper;
import com.rocui.util.base.reflect.each.JfieldEach;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ModelSnooper {

    private Object model;

    public ModelSnooper(Object model) {
        this.model = model;
    }

    public SQLInfo getUpdateSQLInfo() throws Exception {
        ObjectSnooper snooper = ObjectSnooper.snoop(model);
        final ModelInfos info = new ModelInfos();
        Model k = (Model) snooper.annotation(Model.class);
        info.tableName = k.name();
        snooper.fields(new JfieldEach() {
            @Override
            public boolean each(Field method) throws Exception {
                method.setAccessible(true);
                Object value = method.get(model);
                if (method.isAnnotationPresent(Id.class)) {
                    info.idName = method.getName();
                    info.idValue = value;
                }
                if (null != value) {
                    info.values.add(value);
                    info.fieldNames.add(method.getName());
                }
                return false;
            }
        });
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ");
        sb.append(info.tableName);
        sb.append(" SET ");
        for (String a : info.fieldNames) {
            sb.append(a);
            sb.append("=?");
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(" where ");
        sb.append(info.idName);
        sb.append("=?");
        info.values.add(info.idValue);
        System.out.println("==>" + sb.toString() + "==>" + info.values);
        SQLInfo iff = new SQLInfo();
        iff.sql = sb.toString();
        iff.values = info.values.toArray();
        iff.action = SQLInfo.ACTION_UPDATE;
        iff.tableFields = info.fieldNames;
        iff.tableName = info.tableName;
        iff.tableId = info.idName;
        return iff;
    }

    public SQLInfo getInsertSQLInfo() throws Exception {
        ObjectSnooper snooper = ObjectSnooper.snoop(model);
        final ModelInfos info = new ModelInfos();
        Model k = (Model) snooper.annotation(Model.class);
        info.tableName = k.name();
        snooper.fields(new JfieldEach() {
            @Override
            public boolean each(Field method) throws Exception {
                if (method.isAnnotationPresent(Id.class)) {
                    info.idName = method.getName();
                }
                method.setAccessible(true);
                Object value = method.get(model);
                if (null != value) {
                    info.values.add(value);
                    info.fieldNames.add(method.getName());
                }
                return false;
            }
        });
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ");
        sb.append(info.tableName);
        sb.append(" (");
        for (String a : info.fieldNames) {
            sb.append(a);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        sb.append(" VALUES(");
        for (String a : info.fieldNames) {
            sb.append("?");
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        System.out.println("==>" + sb.toString() + "==>" + info.values);
        SQLInfo iff = new SQLInfo();
        iff.sql = sb.toString();
        iff.values = info.values.toArray();
        iff.action = SQLInfo.ACTION_INSERT;
        iff.tableFields = info.fieldNames;
        iff.tableName = info.tableName;
        iff.tableId = info.idName;
        return iff;
    }

    public SQLInfo getDeleteSQLInfo() throws Exception {
        ObjectSnooper snooper = ObjectSnooper.snoop(model);
        final ModelInfos info = new ModelInfos();
        Model k = (Model) snooper.annotation(Model.class);
        info.tableName = k.name();
        snooper.fields(new JfieldEach() {
            @Override
            public boolean each(Field method) throws Exception {
                if (method.isAnnotationPresent(Id.class)) {
                    info.idName = method.getName();
                }
                method.setAccessible(true);
                Object value = method.get(model);
                if (null != value) {
                    info.values.add(value);
                    info.fieldNames.add(method.getName());
                }
                return false;
            }
        });
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ");
        sb.append(info.tableName);
        sb.append(" WHERE ");
        for (String a : info.fieldNames) {
            sb.append(a);
            sb.append("=? AND ");
        }
        sb.delete(sb.length() - 4, sb.length());
        System.out.println("==>" + sb.toString() + "==>" + info.values);
        SQLInfo iff = new SQLInfo();
        iff.sql = sb.toString();
        iff.values = info.values.toArray();
        iff.action = SQLInfo.ACTION_DELETE;
        iff.tableFields = info.fieldNames;
        iff.tableName = info.tableName;
        iff.tableId = info.idName;
        return iff;
    }

    public SQLInfo getSelectSQLInfo() throws Exception {
        ObjectSnooper snooper = ObjectSnooper.snoop(model);
        final ModelInfos info = new ModelInfos();
        final List<String> names = new ArrayList<>();
        Model k = (Model) snooper.annotation(Model.class);
        info.tableName = k.name();
        snooper.fields(new JfieldEach() {
            @Override
            public boolean each(Field method) throws Exception {
                if (method.isAnnotationPresent(Id.class)) {
                    info.idName = method.getName();
                }
                method.setAccessible(true);
                Object value = method.get(model);
                if (null != value) {
                    info.values.add(value);
                    info.fieldNames.add(method.getName());
                }
                names.add(method.getName());
                return false;
            }
        });

        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        for (String a : names) {
            sb.append(a);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(" from ");
        sb.append(info.tableName);
        sb.append(" where ");
        for (String a : info.fieldNames) {
            sb.append(a);
            sb.append("=? and ");
        }
        sb.delete(sb.length() - 4, sb.length());
        System.out.println("==>" + sb.toString() + "==>" + info.values);
        SQLInfo iff = new SQLInfo();
        iff.sql = sb.toString();
        iff.values = info.values.toArray();
        iff.action = SQLInfo.ACTION_SELECT;
        iff.tableFields = info.fieldNames;
        iff.tableName = info.tableName;
        iff.tableId = info.idName;
        return iff;
    }
    
    public SQLInfo getSelectAllSQLInfo() throws Exception {
        ObjectSnooper snooper = ObjectSnooper.snoop(model);
        final ModelInfos info = new ModelInfos();
        final List<String> names = new ArrayList<>();
        Model k = (Model) snooper.annotation(Model.class);
        info.tableName = k.name();
        snooper.fields(new JfieldEach() {
            @Override
            public boolean each(Field method) throws Exception {
                if (method.isAnnotationPresent(Id.class)) {
                    info.idName = method.getName();
                }
                method.setAccessible(true);
                Object value = method.get(model);
                if (null != value) {
//                    info.values.add(value);
                    info.fieldNames.add(method.getName());
                }
                names.add(method.getName());
                return false;
            }
        });

        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        for (String a : names) {
            sb.append(a);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(" from ");
        sb.append(info.tableName);
        System.out.println("==>" + sb.toString() + "==>" + info.values);
        SQLInfo iff = new SQLInfo();
        iff.sql = sb.toString();
        iff.values = info.values.toArray();
        iff.action = SQLInfo.ACTION_SELECT;
        iff.tableFields = info.fieldNames;
        iff.tableName = info.tableName;
        iff.tableId = info.idName;
        return iff;
    }

    public class SQLInfo {

        public static final String ACTION_SELECT = "select";
        public static final String ACTION_UPDATE = "update";
        public static final String ACTION_DELETE = "delete";
        public static final String ACTION_INSERT = "insert";
        private String sql;
        private Object[] values;
        private String tableName;
        private String tableId;
        private List<String> tableFields;
        private String action;

        public String getSQLString() {
            return this.sql;
        }

        public Object[] getValues() {
            return this.values;
        }

        public String getTableName() {
            return tableName;
        }

        public String getTableId() {
            return tableId;
        }

        public List<String> getTableFields() {
            return tableFields;
        }

        public String getAction() {
            return action;
        }
    }

    private class ModelInfos {

        private String idName;
        private String tableName;
        private Object idValue;
        private List<String> fieldNames = new ArrayList<String>();
        private List<Object> values = new ArrayList<Object>();
    }
}

package com.rocui.mvc.data.dao;

import com.rocui.mvc.ConnectionManager.BaseConnection;
import com.rocui.mvc.data.ModelSnooper;
import com.rocui.mvc.data.ModelSnooper.SQLInfo;
import com.rocui.util.base.reflect.ObjectSnooper;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MysqlDao extends BaseDao {

    public <T> T query(T model) throws Exception {
        ModelSnooper snooper = new ModelSnooper(model);
        SQLInfo info = snooper.getSelectSQLInfo();
        HashMap<String, Object> map = this.queryMap(info.getSQLString(), info.getValues());
        if (null != map) {
            ObjectSnooper.snoop(model).set(map);
            return model;
        } else {
            return null;
        }
    }

    public <T> T query(T obj, String sql, Object[] parlist) throws Exception {
        HashMap<String, Object> map = this.queryMap(sql, parlist);
        if (null != map) {
            ObjectSnooper.snoop(obj).set(map);
            return obj;
        } else {
            return null;
        }
    }

    public <T> T insert(T model) throws Exception {
        ModelSnooper snooper = new ModelSnooper(model);
        SQLInfo info = snooper.getInsertSQLInfo();
        ExcuteResult result = this.executeResult(info.getSQLString(), info.getValues());
        HashMap<String, Object> rt = result.getLastRow();
        if (null != rt) {
            rt.put(info.getTableId(), rt.get("GENERATED_KEY"));
            ObjectSnooper.snoop(model).set(rt);
        }
        return model;
    }

    public <T> T update(T model) throws Exception {
        ModelSnooper snooper = new ModelSnooper(model);
        SQLInfo info = snooper.getUpdateSQLInfo();
        this.executeResult(info.getSQLString(), info.getValues());
        return model;
    }

    public <T> T delete(T model) throws Exception {
        ModelSnooper snooper = new ModelSnooper(model);
        SQLInfo info = snooper.getDeleteSQLInfo();
        this.execute(info.getSQLString(), info.getValues());
        return model;
    }

    public boolean execute(String sql, Object[] parlist) throws Exception {
        BaseConnection con = this.getConnection();
        Boolean t = false;
        PreparedStatement pstmt = null;
        try {
            int count = this.times(sql);
            int count2 = parlist != null ? parlist.length : -1;
            pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            if (count == count2) {
                this.setPrepareStatement(pstmt, parlist);
                pstmt.executeUpdate();
                t = true;
            }
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            con.close();
        }
        return t;
    }

    public ExcuteResult executeResult(String sql, Object[] parlist) throws Exception {
        BaseConnection con = this.getConnection();
        PreparedStatement pstmt = null;
        try {
            int count = this.times(sql);
            int count2 = parlist != null ? parlist.length : -1;
            pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            if (count == count2) {
                this.setPrepareStatement(pstmt, parlist);
                pstmt.executeUpdate();
                ResultSet rs = pstmt.getGeneratedKeys();
                ExcuteResult xresult = new ExcuteResult();
                xresult.numberOfColumns = rs.getMetaData().getColumnCount();
                List<HashMap<String, Object>> lst = this.getMapFromResultSet(rs);
                if (lst.size() > 0) {
                    xresult.lastRow = lst.get(0);
                } else {
                    xresult.lastRow = null;
                }
                return xresult;
            } else {
                return null;
            }
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            con.close();
        }
    }

    public List<HashMap<String, Object>> queryList(String sql, Object[] parlist) throws Exception {
        int count = this.times(sql);
        int count2 = parlist != null ? parlist.length : 0;
        if (count == count2) {
            BaseConnection con = this.getConnection();
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                pstmt = con.prepareStatement(sql);
                this.setPrepareStatement(pstmt, parlist);
                rs = pstmt.executeQuery();
                return this.getMapFromResultSet(rs);
            } finally {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (rs != null) {
                    rs.close();
                }
                con.close();
            }
        } else {
            return null;
        }
    }

    public RowList queryList(Object model) throws Exception {
        ModelSnooper snooper = new ModelSnooper(model);
        SQLInfo info = snooper.getSelectSQLInfo();
        RowList rl = new RowList();
        rl.rows = this.queryList(info.getSQLString(), info.getValues());
        return rl;
    }

    public int queryCount(String sql, Object[] parlist) throws Exception {
        int result = -1;
        int count = this.times(sql);
        int count2 = parlist != null ? parlist.length : 0;
        if (count == count2) {
            BaseConnection con = this.getConnection();
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                pstmt = con.prepareStatement(sql);
                this.setPrepareStatement(pstmt, parlist);
                rs = pstmt.executeQuery();
                rs.last();
                result = rs.getRow();
            } finally {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (rs != null) {
                    rs.close();
                }
                con.close();
            }
        }
        return result;
    }

    public PageInfo queryPage(String sql, int from, int size, Object[] parlist) throws Exception {
        PageInfo info = new PageInfo();
        info.setTotal(this.queryCount(sql, parlist));
        sql += " limit ?,?";
        if (null == parlist) {
            parlist = new Object[]{from, size};
        } else {
            Object[] ne = new Object[parlist.length + 2];
            System.arraycopy(parlist, 0, ne, 0, parlist.length);
            ne[parlist.length] = from;
            ne[parlist.length + 1] = size;
            parlist=ne;
        }
        System.out.println("="+sql+":"+print(parlist));
        info.setRows(this.queryList(sql, parlist));
        return info;
    }

    private List<Object> print(Object[] k){
        List<Object> map=new ArrayList<>();
        for(int i=0;i<k.length;i++){
            map.add(k[i]);
        }
        return map;
    }
    
    public PageInfo queryPage(String sql, int from, int size) throws Exception {
        return this.queryPage(sql, from, size, new Object[]{});
    }

    public PageInfo queryPage(Object model, int from, int size) throws Exception {
        ModelSnooper snooper = new ModelSnooper(model);
        SQLInfo info = snooper.getSelectSQLInfo();
        return this.queryPage(info.getSQLString(), from, size, info.getValues());
    }

    public HashMap<String, Object> queryMap(String sql, Object[] parlist) throws Exception {
        List<HashMap<String, Object>> list = this.queryList(sql, parlist);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    private void setPrepareStatement(PreparedStatement pstmt, Object[] parlist) throws Exception {
        if (null != parlist) {
            for (int j = 0; j < parlist.length; j++) {
                if (parlist[j] instanceof Integer) {
                    pstmt.setInt(j + 1, Integer.parseInt(parlist[j].toString()));
                } else if (parlist[j] instanceof Long) {
                    pstmt.setLong(j + 1, Long.parseLong(parlist[j].toString()));
                } else if (parlist[j] instanceof Float) {
                    pstmt.setFloat(j + 1, Float.parseFloat(parlist[j].toString()));
                } else if (parlist[j] instanceof Double) {
                    pstmt.setDouble(j + 1, Double.parseDouble(parlist[j].toString()));
                } else {
                    pstmt.setString(j + 1, null != parlist[j] ? parlist[j].toString() : null);
                }
            }
        }
    }

    private int times(String str) {
        Matcher m;
        m = Pattern.compile("\\?").matcher(str);
        int i = 0;
        while (m.find()) {
            i++;
        }
        return i;
    }

    private List<HashMap<String, Object>> getMapFromResultSet(ResultSet rs) throws SQLException {
        List<HashMap<String, Object>> list = new ArrayList<>();
        ResultSetMetaData rsmd = rs.getMetaData();
        int numberOfColumns = rsmd.getColumnCount();
        while (rs.next()) {
            HashMap<String, Object> rsTree = new HashMap<>();
            for (int r = 1; r < numberOfColumns + 1; r++) {
                rsTree.put(rsmd.getColumnLabel(r), rs.getObject(r));
            }
            list.add(rsTree);
        }
        return list;
    }

    public class ExcuteResult {

        private int numberOfColumns = 0;
        private HashMap<String, Object> lastRow;

        public int getNumberOfColumns() {
            return numberOfColumns;
        }

        public HashMap<String, Object> getLastRow() {
            return lastRow;
        }
    }

    public class PageInfo {

        private List<HashMap<String, Object>> rows = new ArrayList<>();
        private int total;

        public PageInfo() {

        }

        public PageInfo(List<HashMap<String, Object>> rows, int total) {
            this.rows = rows;
            this.total = total;
        }

        public List<HashMap<String, Object>> getRows() {
            return rows;
        }

        public int getTotal() {
            return total;
        }

        public <T> List<T> toObjectList(T obj) {
            if (null != this.rows) {
                List<T> result = new ArrayList<>();
                for (HashMap<String, Object> map : this.rows) {
                    T a = ObjectSnooper.snoop(obj).newInstance();
                    ObjectSnooper.snoop(a).set(map);
                    result.add(a);
                }
                return result;
            } else {
                return null;
            }
        }

        private void setRows(List<HashMap<String, Object>> rows) {
            this.rows = rows;
        }

        private void setTotal(int total) {
            this.total = total;
        }
    }

    public class RowList {

        private List<HashMap<String, Object>> rows;

        public <T> List<T> toObjectList(T obj) {
            if (null != this.rows) {
                List<T> result = new ArrayList<>();
                for (HashMap<String, Object> map : this.rows) {
                    T a = ObjectSnooper.snoop(obj).newInstance();
                    ObjectSnooper.snoop(a).set(map);
                    result.add(a);
                }
                return result;
            } else {
                return null;
            }
        }

        public List<HashMap<String, Object>> getRows() {
            return this.rows;
        }
    }
}

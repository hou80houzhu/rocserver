package com.rocui.jmail.page;

//import com.jcnetwork.jcms.util.data.PageContent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.rocui.mvc.data.jdbc.pool.DBPool;
import com.rocui.jpager.base.PageContent;

public class JmailQuery {

    private Connection con;

    public JmailQuery() {
        try {
            this.con = DBPool.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() {
        return this.con;
    }

    protected void close() {
        try {
            this.con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PageContent queryPage(String sql, int page, int pageSize, Object[] parameters) throws Exception {
        PageContent con = new PageContent();
        con.setTotal(this.getRowCount(sql, parameters));
        Object[] ne;
        if (parameters != null) {
            ne = new Object[parameters.length + 2];
            System.arraycopy(parameters, 0, ne, 0, parameters.length);
            ne[parameters.length] = page;
            ne[parameters.length + 1] = pageSize;
        } else {
            ne = new Object[]{page, pageSize};
        }
        con.setRows(this.queryList(sql + " limit ?,?", ne));
        return con;
    }

    public PageContent queryPageCount(String sql, int page, int pageSize, Object[] parameters) throws Exception {
        PageContent con = new PageContent();
        con.setTotal(this.getRowCount(sql, parameters));
        Object[] ne;
        if (parameters != null) {
            ne = new Object[parameters.length + 2];
            System.arraycopy(parameters, 0, ne, 0, parameters.length);
            ne[parameters.length] = page * pageSize;
            ne[parameters.length + 1] = pageSize;
        } else {
            ne = new Object[]{page * pageSize, pageSize};
        }
        con.setRows(this.queryList(sql + " limit ?,?", ne));
        return con;
    }

    public HashMap<String, Object> queryTree(String sql, Object[] parameters, HashMap<String, Object> result) throws Exception {
        List<HashMap<String, Object>> list = this.queryListObject(sql, parameters);
        List<Object> clist = new ArrayList<Object>();
        if (list.size() > 0) {
            for (HashMap<String, Object> map : list) {
                clist.add(this.queryTree(sql, new Object[]{map.get("id")}, map));
            }
        }
        result.put("nodes", clist);
        return result;
    }

    public int getRowCount(String sql, Object[] parlist) throws Exception {
        int count = this.times(sql);
        int count2 = parlist != null ? parlist.length : 0;
        if (count == count2) {
            Connection con = this.getConnection();
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                pstmt = con.prepareStatement(sql);
                this.setPrepareStatement(pstmt, parlist);
                rs = pstmt.executeQuery();
                rs.last();
                return rs.getRow();
            } finally {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (rs != null) {
                    rs.close();
                }
            }
        } else {
            System.out.println("the arguments size can not match the sql.");
            return -1;
        }
    }

    public final List<HashMap<String, Object>> queryListObject(String sql, Object[] parlist) throws Exception {
        int count = this.times(sql);
        int count2 = parlist != null ? parlist.length : 0;
        if (count == count2) {
            Connection con = this.getConnection();
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            HashMap<String, Object> rsTree;
            List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
            try {
                pstmt = con.prepareStatement(sql);
                this.setPrepareStatement(pstmt, parlist);
                rs = pstmt.executeQuery();
                ResultSetMetaData rsmd = rs.getMetaData();
                int numberOfColumns = rsmd.getColumnCount();
                while (rs.next()) {
                    rsTree = new HashMap<String, Object>(numberOfColumns);
                    for (int r = 1; r < numberOfColumns + 1; r++) {
                        rsTree.put(rsmd.getColumnLabel(r), rs.getObject(r));
                    }
                    list.add(rsTree);
                }
                return list;
            } finally {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (rs != null) {
                    rs.close();
                }
            }
        } else {
            return null;
        }
    }

    public final List<HashMap<String, Object>> queryList(String sql, Object[] parlist) throws Exception {
        int count = this.times(sql);
        int count2 = parlist != null ? parlist.length :0;
        if (count == count2) {
            Connection con = this.getConnection();
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
            try {
                pstmt = con.prepareStatement(sql);
                this.setPrepareStatement(pstmt, parlist);
                rs = pstmt.executeQuery();
                ResultSetMetaData rsmd = rs.getMetaData();
                HashMap<String, Object> rsTree;
                int numberOfColumns = rsmd.getColumnCount();
                while (rs.next()) {
                    rsTree = new HashMap<String, Object>(numberOfColumns);
                    for (int r = 1; r < numberOfColumns + 1; r++) {
                        rsTree.put(rsmd.getColumnLabel(r), rs.getObject(r));
                    }
                    list.add(rsTree);
                }
                return list;
            } finally {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (rs != null) {
                    rs.close();
                }
            }
        } else {
            return null;
        }
    }

    public final HashMap<String, Object> queryMap(String sql, Object[] parlist) throws Exception {
        int count = this.times(sql);
        int count2 = parlist != null ? parlist.length :0;
        if (count == count2) {
            Connection con = this.getConnection();
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            HashMap<String, Object> rsTree = new HashMap<String, Object>();
            try {
                pstmt = con.prepareStatement(sql);
                this.setPrepareStatement(pstmt, parlist);
                rs = pstmt.executeQuery();
                ResultSetMetaData rsmd = rs.getMetaData();
                int numberOfColumns = rsmd.getColumnCount();
                if (rs.next()) {
                    for (int r = 1; r < numberOfColumns + 1; r++) {
                        rsTree.put(rsmd.getColumnLabel(r), rs.getObject(r));
                    }
                } else {
                    rsTree = new HashMap<String, Object>();
                }
            } finally {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (rs != null) {
                    rs.close();
                }
            }
            return rsTree;
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
                    pstmt.setString(j + 1, parlist[j].toString());
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
}

package com.rocui.CodeHelper.mvc;

import com.rocui.mvc.annotation.Dao;
import com.rocui.mvc.data.dao.MysqlDao;
import java.util.HashMap;
import java.util.List;

public class DBInfo implements IDBInfo {

    @Dao
    private MysqlDao dao;

    @Override
    public List<HashMap<String, Object>> getTableInfo(String dbName, String tableName) throws Exception {
        return dao.queryList("SHOW COLUMNS FROM " + tableName + " from " + dbName, null);
    }

    @Override
    public List<HashMap<String, Object>> getTables(String dbName) throws Exception {
        return dao.queryList("SHOW TABLES from " + dbName, null);
    }

}

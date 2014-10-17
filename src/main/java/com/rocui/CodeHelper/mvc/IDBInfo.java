package com.rocui.CodeHelper.mvc;

import java.util.HashMap;
import java.util.List;

public interface IDBInfo {

    public List<HashMap<String, Object>> getTableInfo(String dbName, String tableName) throws Exception;

    public List<HashMap<String, Object>> getTables(String dbName) throws Exception;
}

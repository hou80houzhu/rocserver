package com.rocui.mvc.data.jdbc.pool;

import java.sql.Connection;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DBPool {

    private static ComboPooledDataSource datasource = null;

    private DBPool() {
        datasource = new ComboPooledDataSource();
    }

    public static final ComboPooledDataSource getDataSource() {
        if (datasource == null) {
            new DBPool();
        }
        return datasource;
    }

    public synchronized static final Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }
    
    public static void getConnectionsSize(){
        try {
            System.out.println(getDataSource().getNumConnections());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}

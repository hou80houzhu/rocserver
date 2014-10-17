package com.rocui.mvc;

import com.rocui.mvc.data.jdbc.pool.DBPool;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;

public class ConnectionManager {

    protected BaseConnection bc;
    private int count = 0;
    private int commitCount = 0;

    public ConnectionManager() {
        try {
            this.bc = new BaseConnection();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public BaseConnection getConnection() throws SQLException {
        this.count++;
        return this.bc;
    }

    public class BaseConnection {

        private Connection connection;

        public BaseConnection() throws SQLException {
            System.out.println("==>connection connect");
            this.connection = DBPool.getConnection();
        }

        public void clearWarnings() throws SQLException {
            this.connection.clearWarnings();
        }

        public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
            return this.connection.createArrayOf(typeName, elements);
        }

        public Blob createBlob() throws SQLException {
            return this.connection.createBlob();
        }

        public Clob createClob() throws SQLException {
            return this.connection.createClob();
        }

        public NClob createNClob() throws SQLException {
            return this.connection.createNClob();
        }

        public SQLXML createSQLXML() throws SQLException {
            return this.connection.createSQLXML();
        }

        public Statement createStatement() throws SQLException {
            return this.connection.createStatement();
        }

        public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
            return this.connection.createStatement(resultSetType, resultSetConcurrency);
        }

        public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
            return this.connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
            return this.connection.createStruct(typeName, attributes);
        }

        public String getCatalog() throws SQLException {
            return this.connection.getCatalog();
        }

        public Properties getClientInfo() throws SQLException {
            return this.connection.getClientInfo();
        }

        public String getClientInfo(String name) throws SQLException {
            return this.connection.getClientInfo(name);
        }

        public int getHoldability() throws SQLException {
            return this.connection.getHoldability();
        }

        public DatabaseMetaData getMetaData() throws SQLException {
            return this.connection.getMetaData();
        }

        public int getTransactionIsolation() throws SQLException {
            return this.connection.getTransactionIsolation();
        }

        public Map<String, Class<?>> getTypeMap() throws SQLException {
            return this.connection.getTypeMap();
        }

        public SQLWarning getWarnings() throws SQLException {
            return this.connection.getWarnings();
        }

        public boolean isClosed() throws SQLException {
            return this.connection.isClosed();
        }

        public boolean isReadOnly() throws SQLException {
            return this.connection.isReadOnly();
        }

        public boolean isValid(int timeout) throws SQLException {
            return this.connection.isValid(timeout);
        }

        public String nativeSQL(String sql) throws SQLException {
            return this.connection.nativeSQL(sql);
        }

        public CallableStatement prepareCall(String sql) throws SQLException {
            return this.connection.prepareCall(sql);
        }

        public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
            return this.connection.prepareCall(sql, resultSetType, resultSetConcurrency);
        }

        public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
            return this.connection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        public PreparedStatement prepareStatement(String sql) throws SQLException {
            return this.connection.prepareStatement(sql);
        }

        public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
            return this.connection.prepareStatement(sql, autoGeneratedKeys);
        }

        public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
            return this.connection.prepareStatement(sql, columnIndexes);
        }

        public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
            return this.connection.prepareStatement(sql, resultSetType, resultSetConcurrency);
        }

        public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
            return this.connection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
            return this.connection.prepareStatement(sql, columnNames);
        }

        public void setCatalog(String catalog) throws SQLException {
            this.connection.setCatalog(catalog);
        }

        public void setClientInfo(Properties properties) throws SQLClientInfoException {
            this.connection.setClientInfo(properties);
        }

        public void setClientInfo(String name, String value) throws SQLClientInfoException {
            this.connection.setClientInfo(name, value);
        }

        public void setHoldability(int holdability) throws SQLException {
            this.connection.setHoldability(holdability);
        }

        public void setReadOnly(boolean readOnly) throws SQLException {
            this.connection.setReadOnly(readOnly);
        }

        public void setTransactionIsolation(int level) throws SQLException {
            this.connection.setTransactionIsolation(level);
        }

        public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
            this.connection.setTypeMap(map);
        }

        protected void releaseSavepoint(Savepoint savepoint) throws SQLException {
            this.connection.releaseSavepoint(savepoint);
        }

        protected void rollback(Savepoint savepoint) throws SQLException {
            this.connection.rollback(savepoint);
        }

        protected Savepoint setSavepoint() throws SQLException {
            return this.connection.setSavepoint();
        }

        protected Savepoint setSavepoint(String name) throws SQLException {
            return this.connection.setSavepoint(name);
        }

        protected void rollback() throws SQLException {
            System.out.println("==>connection rollback");
            commitCount--;
            this.connection.rollback();
        }

        protected void commit() throws SQLException {
            commitCount--;
            if (commitCount == 0) {
                System.out.println("==>connection commit");
                this.connection.commit();
            }
        }

        protected void setAutoCommit(boolean autoCommit) throws SQLException {
            if (commitCount==0) {
                System.out.println("==>connection unautocommit");
                this.connection.setAutoCommit(autoCommit);
            }
            commitCount++;
        }
        
        protected void setAutoCommitTrue() throws SQLException{
            this.connection.setAutoCommit(true);
        }

        public void close() throws SQLException {
            count--;
            if (count == 0&&commitCount==0) {
                this.connection.close();
                System.out.println("==>connection close");
            }
        }
    }
}

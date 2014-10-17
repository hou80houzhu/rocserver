package com.rocui.mvc.data.dao;

import com.rocui.mvc.ControlCenter;
import com.rocui.mvc.ConnectionManager.BaseConnection;
import com.rocui.mvc.SessionState;
import java.sql.SQLException;

public abstract class BaseDao {
    protected BaseConnection getConnection() throws SQLException{
        if (null == ControlCenter.sessionState.get()) {
            SessionState state = new SessionState();
            state.push();
            ControlCenter.sessionState.set(state);
        }
        return ((SessionState)ControlCenter.sessionState.get()).getConnectionManager().getConnection();
    };
}

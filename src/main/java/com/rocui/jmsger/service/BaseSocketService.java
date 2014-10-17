package com.rocui.jmsger.service;

import com.rocui.jmsger.base.SocketRequest;
import com.rocui.jmsger.base.SocketResponse;
import com.rocui.jmsger.base.connect.Connect;
import com.rocui.jmsger.base.connect.imp.Connections;
import com.rocui.jmsger.base.result.ResultSet;
import com.rocui.jmsger.router.BaseSocketRouter;

/**
 *
 * @author hou80houzhu
 */
public abstract class BaseSocketService {
    protected Connect connect;
    protected Connections connections;
    protected SocketRequest request;
    protected SocketResponse response=new SocketResponse();
    public abstract void doService() throws Exception;
    
    public void doRoutResult() throws Exception{
        this.doService();
        for(ResultSet resultSet:this.response.getResults()){
            BaseSocketRouter.getSocketRouter(resultSet.getRoutName()).doRout(resultSet, this.connect, this.connections);
        }
    }
    
    protected ResultSet getResultSet(){
        ResultSet set=new ResultSet();
        set.setCallbackIndex(this.request.getCallbackIndex());
        set.setGroupId(this.request.getGroupId());
        set.setOther(this.request.getOther());
        set.setTo(this.request.getTo());
        set.setSessionId(this.request.getSessionId());
        return set;
    }

    public Connect getConnect() {
        return connect;
    }

    public Connections getConnections() {
        return connections;
    }

    public void setConnect(Connect connect) {
        this.connect = connect;
    }

    public void setConnections(Connections connections) {
        this.connections = connections;
    }

    public SocketRequest getRequest() {
        return request;
    }

    public void setRequest(SocketRequest request) {
        this.request = request;
    }

    public SocketResponse getResponse() {
        return response;
    }

    private void setResponse(SocketResponse response) {
        this.response = response;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rocui.jmsger.server;

import com.rocui.jmsger.base.connect.imp.ConnectGroup;
import com.rocui.jmsger.base.connect.imp.Connections;
import com.rocui.jmsger.base.result.ResultSet;
import com.rocui.jmsger.router.BaseSocketRouter;
import com.rocui.jmsger.service.ClientServiceType;
import com.rocui.util.jsonx.Jsonx;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 *
 * @author Jinliang
 */
public class SocketAnywhere {

    public static void broadCastMessage(Object mes) {
        ResultSet result = new ResultSet();
        result.setService(ClientServiceType.SERVICE_BRODCAST);
        result.setCode(1);
        result.setData(mes);
        result.setSessionId("SERVER");
        result.setRoutName(BaseSocketRouter.ROUT_NAME_SIMPLE);
        result.setRoutType(BaseSocketRouter.ROUT_TO);
        Connections.getConnections().broadCastMessage(Jsonx.create(result).toString());
    }

    public static void sendMessage(String socketId, Object mes) {
        ResultSet result = new ResultSet();
        result.setService(ClientServiceType.SERVICE_SERVERCALL);
        result.setCode(1);
        result.setData(mes);
        result.setSessionId("SERVER");
        result.setTo(socketId);
        result.setRoutName(BaseSocketRouter.ROUT_NAME_SIMPLE);
        result.setRoutType(BaseSocketRouter.ROUT_TO);
        Connections.getConnections().sendToMessage(socketId, Jsonx.create(result.getResult()).toString());
    }
    
    public static void sendGroupMessage(String groupid, Object mes) {
        ResultSet result = new ResultSet();
        result.setService(ClientServiceType.SERVICE_SERVERCALL);
        result.setCode(1);
        result.setData(mes);
        result.setSessionId("SERVER");
        result.setTo(groupid);
        result.setRoutName(BaseSocketRouter.ROUT_NAMW_GROUP);
        result.setRoutType(BaseSocketRouter.ROUT_TO);
        ConnectGroup.getConnectGroup().sendToMessage(groupid, Jsonx.create(result.getResult()).toString());
    }
    
    public static String getGroupIdBySocketId(String socketid){
        String id=null;
        boolean isbreak=false;
        HashMap<String,List<String>> map=ConnectGroup.getConnectGroup().getMapping();
        for(Entry<String,List<String>> x:map.entrySet()){
            List<String> ls=x.getValue();
            for(String k:ls){
                if(k.equals(socketid)){
                    id=x.getKey();
                    isbreak=true;
                    break;
                }
            }
            if(isbreak){
                break;
            }
        }
        return id;
    }
}

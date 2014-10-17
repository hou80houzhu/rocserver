package com.rocui.jmsger.base.connect.imp;

import com.rocui.jmsger.base.connect.Connect;
import com.rocui.jmsger.base.connect.ConnectCollection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class ConnectGroup extends ConnectCollection {

    private HashMap<String, List<String>> map = new HashMap<String, List<String>>();
    private static ConnectGroup ths = null;
    private HashMap<String, Connect> conns = Connections.getConnections().getConnects();

    private ConnectGroup() {
    }

    public static ConnectGroup getConnectGroup() {
        if (ConnectGroup.ths == null) {
            ConnectGroup.ths = new ConnectGroup();
        }
        return ConnectGroup.ths;
    }

    public List<String> getConnects(String groupid) {
        return this.map.get(groupid);
    }
    
    public HashMap<String,List<String>> getMapping(){
        return this.map;
    }

    public List<String> getConnectsWidthOut(String groupid) {
        List<String> list = new ArrayList<String>();
        for (Entry<String, List<String>> x : this.map.entrySet()) {
            if (!x.getKey().equals(groupid)) {
                list.addAll(x.getValue());
            }
        }
        return list;
    }

    public ConnectGroup addConnect(String groupid, String connectid) {
        if (this.map.containsKey(groupid)) {
            List<String> list = this.map.get(groupid);
            if (!list.contains(connectid)) {
                list.add(connectid);
            }
        } else {
            List<String> m = new ArrayList<String>();
            m.add(connectid);
            this.map.put(groupid, m);
        }
        return this;
    }

    public void removeConnect(String groupid, String connectid) {
        if (this.map.containsKey(groupid)) {
            this.map.get(groupid).remove(connectid);
        }
    }

    public void removeConnects(String groupid) {
        if (this.map.containsKey(groupid)) {
            this.map.remove(groupid);
        }
    }

    @Override
    public void broadCastMessage(String str) {
        for (Entry<String, Connect> map : this.conns.entrySet()) {
            map.getValue().send(str);
        }
    }

    @Override
    public void sendToMessage(String groupid, String str) {
        List<String> list = this.getConnects(groupid);
        for (String id : list) {
            if (this.conns.containsKey(id)) {
                this.conns.get(id).send(str);
            }
        }
    }

    @Override
    public void sendMessageWidthOut(String groupid, String str) {
        List<String> list = this.getConnectsWidthOut(groupid);
        for (String id : list) {
            if (this.conns.containsKey(id)) {
                this.conns.get(id).send(str);
            }
        }
    }

    @Override
    public void sendToGroupMessage(List<String> ids, String str) {
        for (String id : ids) {
            this.sendToMessage(id, str);
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rocui.jmsger.base.connect.imp;

import com.rocui.jmsger.base.connect.Connect;
import com.rocui.jmsger.base.connect.ConnectCollection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import org.jboss.netty.channel.Channel;

/**
 *
 * @author hou80houzhu
 */
public class Connections extends ConnectCollection {

    private HashMap<String, Connect> cons = new HashMap<String, Connect>();
    private static Connections con = null;

    public static Connections getConnections() {
        if (Connections.con == null) {
            Connections.con = new Connections();
        }
        return Connections.con;
    }

    public HashMap<String, Connect> getConnects() {
        return this.cons;
    }

    public synchronized Connect add(Channel channel) {
        Connect connect = new Connect(channel);
        this.cons.put(channel.getId() + "", connect);
        return connect;
    }

    public Connect getConnect(String id) {
        return this.cons.get(id);
    }

    public void remove(String id) {
        this.cons.remove(id);
    }

    public void remove(Channel channel) {
        String key = "";
        for (Entry<String, Connect> map : this.cons.entrySet()) {
            if (map.getValue().getChannel().equals(channel)) {
                key = map.getKey();
            }
        }
        if (!key.equals("")) {
            this.cons.remove(key);
        }
    }

    public void remove(Connect connect) {
        for (Entry<String, Connect> map : this.cons.entrySet()) {
            if (map.getValue().equals(connect)) {
                this.cons.remove(map.getKey());
            }
        }
    }

    public int size() {
        return this.cons.size();
    }

    @Override
    public void broadCastMessage(String str) {
        for (Entry<String, Connect> map : this.cons.entrySet()) {
            map.getValue().send(str);
        }
    }

    @Override
    public void sendToMessage(String id, String str) {
        this.getConnect(id).send(str);
    }

    @Override
    public void sendMessageWidthOut(String id, String str) {
        for (Entry<String, Connect> map : this.cons.entrySet()) {
            if (!map.getKey().equals(id)) {
                map.getValue().send(str);
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rocui.jmsger.base.connect;

import com.rocui.jmsger.base.SocketSession;
import com.rocui.jmsger.base.connect.imp.Connections;
import com.rocui.util.jsonx.Jsonx;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 *
 * @author hou80houzhu
 */
public class Connect {

    private Channel channel;
    private int id;
    private String groupid;
    private Connections connections = Connections.getConnections();
    private SocketSession session=new SocketSession();

    public Connect(Channel channel) {
        this.channel = channel;
        this.id = channel.getId();
    }

    public String id() {
        return this.id + "";
    }

    public Connections getConnections() {
        return this.connections;
    }

    public Channel getChannel() {
        return this.channel;
    }

    public void send(String mes) {
        if (this.channel.isWritable()) {
            this.channel.write(new TextWebSocketFrame(mes));
        }
    }

    public void send(Object obj) {
        if (this.channel.isWritable()) {
            this.channel.write(new TextWebSocketFrame(obj.toString()));
        }
    }

    public void send(Jsonx json) {
        if (this.channel.isWritable()) {
            this.channel.write(json.toString());
        }
    }

    public void close() {
        this.channel.close();
    }
    
    public SocketSession getSession(){
        return this.session;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

}

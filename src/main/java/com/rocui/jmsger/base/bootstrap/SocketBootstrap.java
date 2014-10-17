package com.rocui.jmsger.base.bootstrap;

import com.rocui.jmsger.base.mapping.SocketControllerMapping;
import com.rocui.jmsger.base.mapping.SocketServiceMapping;
import com.rocui.jmsger.server.WebSocketServer;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hou80houzhu
 */
public class SocketBootstrap {

    private static SocketBootstrap boot;
    private HashMap<String, String> config;//service packagename,controller packagename,port

    private SocketBootstrap(HashMap<String, String> config) {
        this.config = config;
    }

    public static void boot(HashMap<String, String> config) {
        if (SocketBootstrap.boot == null) {
            SocketBootstrap.boot = new SocketBootstrap(config);
            try {
                SocketBootstrap.boot._boot();
            } catch (Exception ex) {
                Logger.getLogger(SocketBootstrap.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void _boot() throws Exception {
        SocketServiceMapping mapp=SocketServiceMapping.getSocketServiceMapping(this.config.get("servicepackage"));
        mapp.addMapping("login", "com.rocui.jmsger.service.imp.LoginService");
        mapp.addMapping("logout", "com.rocui.jmsger.service.imp.LogoutService");
        mapp.addMapping("message", "com.rocui.jmsger.service.imp.MessageService");
        mapp.addMapping("rpc", "com.rocui.jmsger.service.imp.RPCService");
        mapp.searchClass();
        SocketControllerMapping.getSocketControllerMapping(this.config.get("controllerpackage")).searchClass();
        new WebSocketServer(Integer.parseInt(this.config.get("port"))).run();
    }
}

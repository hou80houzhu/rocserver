package com.rocui.jmsger.base.bootstrap;

import java.util.HashMap;

public class SocketConfig {

    public static final String ROUTERTYPE_GROUP="group";
    public static final String ROUTERTYPE_SIMPLE="simple";
    private HashMap<String, String> map = new HashMap<String, String>();
    private static SocketConfig config = null;

    public static SocketConfig getConfig() {
        if (SocketConfig.config == null) {
            SocketConfig.config = new SocketConfig();
        }
        return SocketConfig.config;
    }

    public SocketConfig setServicePackage(String packageName) {
        this.map.put("servicepackage", packageName);
        return this;
    }
    
    public String getServicePackage(){
        return this.map.get("servicepackage");
    }

    public SocketConfig setControllerPackage(String packageName) {
        this.map.put("controllerpackage", packageName);
        return this;
    }
    public String getControllerPackage(){
        return this.map.get("controllerpackage");
    }
    
    public SocketConfig setRouterType(boolean isgroup){
        if(isgroup==true){
            this.map.put("routertype", SocketConfig.ROUTERTYPE_GROUP);
        }else{
            this.map.put("routertype", SocketConfig.ROUTERTYPE_SIMPLE);
        }
        return this;
    }
    
    public String getRouterType(){
        if(this.map.containsKey("routertype")){
            return this.map.get("routertype");
        }else{
            return SocketConfig.ROUTERTYPE_SIMPLE;
        }
    }

    public SocketConfig setSocketPort(String port) {
        this.map.put("port", port);
        return this;
    }
    
    public String getSocketPort(){
        return this.map.get("port");
    }
    public HashMap<String,String> getConfigMapping(){
        return this.map;
    }
}
